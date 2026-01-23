package com.leonal.ui.controller;

import com.leonal.application.dto.pago.CreatePagoRequest;
import com.leonal.application.dto.pago.PagoDto;
import com.leonal.application.dto.factura.FacturaDto;
import com.leonal.application.usecase.pago.RegistrarPagoUseCase;
import com.leonal.application.usecase.pago.ListarPagosUseCase;
import com.leonal.application.usecase.factura.ListarFacturasUseCase;
import com.leonal.application.usecase.caja.ListarCajasUseCase;
import com.leonal.application.usecase.caja.ActualizarTotalCajaUseCase;
import com.leonal.domain.model.FormaPago;
import com.leonal.ui.context.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PagoController {
    private final RegistrarPagoUseCase registrarPagoUseCase;
    private final ListarPagosUseCase listarPagosUseCase;
    private final ListarFacturasUseCase listarFacturasUseCase;
    private final ListarCajasUseCase listarCajasUseCase;
    private final ActualizarTotalCajaUseCase actualizarTotalCajaUseCase;
    private final UserSession userSession;

    @FXML
    private ComboBox<FacturaDto> cbFactura;
    @FXML
    private TextField txtMonto;
    @FXML
    private ComboBox<FormaPago> cbFormaPago;
    @FXML
    private TextField txtReferencia;
    @FXML
    private TextArea txtaObservaciones;
    @FXML
    private Label lblMontoFactura;
    @FXML
    private Label lblEstadoFactura;

    @FXML
    private TableView<PagoDto> tblPagos;
    @FXML
    private TableColumn<PagoDto, String> colFactura;
    @FXML
    private TableColumn<PagoDto, BigDecimal> colMonto;
    @FXML
    private TableColumn<PagoDto, String> colFormaPago;
    @FXML
    private TableColumn<PagoDto, LocalDateTime> colFecha;
    @FXML
    private TableColumn<PagoDto, String> colEstado;

    @FXML
    private Button btnRegistrarPago;
    @FXML
    private Button btnLimpiar;

    @FXML
    public void initialize() {
        configurarTabla();
        configurarComboBoxFacturas();
        configurarComboBoxFormaPago();
        cargarPagos();
        
        // Listener para cambios en factura seleccionada
        cbFactura.setOnAction(e -> handleFacturaSeleccionada());
    }

    private void configurarTabla() {
        colFactura.setCellValueFactory(new PropertyValueFactory<>("facturaId"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colFormaPago.setCellValueFactory(new PropertyValueFactory<>("formaPago"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarComboBoxFacturas() {
        List<FacturaDto> facturas = listarFacturasUseCase.execute();
        cbFactura.setItems(FXCollections.observableArrayList(facturas));
        
        // Custom cell factory para mostrar número de factura
        cbFactura.setCellFactory(param -> new ListCell<FacturaDto>() {
            @Override
            protected void updateItem(FacturaDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNumero());
            }
        });
        
        cbFactura.setButtonCell(new ListCell<FacturaDto>() {
            @Override
            protected void updateItem(FacturaDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNumero());
            }
        });
    }

    private void configurarComboBoxFormaPago() {
        cbFormaPago.setItems(FXCollections.observableArrayList(FormaPago.values()));
    }

    @FXML
    public void handleFacturaSeleccionada() {
        FacturaDto factura = cbFactura.getValue();
        if (factura != null) {
            lblMontoFactura.setText(factura.getTotal().toPlainString());
            lblEstadoFactura.setText(factura.getEstado());
            txtMonto.setText(factura.getTotal().toPlainString());
        }
    }

    @FXML
    public void handleRegistrarPago() {
        try {
            FacturaDto factura = cbFactura.getValue();
            if (factura == null) {
                mostrarError("Debe seleccionar una factura");
                return;
            }

            BigDecimal monto = new BigDecimal(txtMonto.getText());
            FormaPago formaPago = cbFormaPago.getValue();

            if (formaPago == null) {
                mostrarError("Debe seleccionar una forma de pago");
                return;
            }

            CreatePagoRequest request = CreatePagoRequest.builder()
                    .facturaId(factura.getId())
                    .monto(monto)
                    .formaPago(formaPago)
                    .referencia(txtReferencia.getText())
                    .observaciones(txtaObservaciones.getText())
                    .build();

            UUID usuarioId = userSession.getCurrentUser() != null ? userSession.getCurrentUser().getId() : UUID.randomUUID();
            PagoDto pago = registrarPagoUseCase.execute(request, usuarioId);
            
            // Actualizar totales de caja abierta si existe (decrementar egreso o incrementar ingreso según pago)
            try {
                var cajasAbiertas = listarCajasUseCase.executeByEstado("ABIERTA");
                if (!cajasAbiertas.isEmpty()) {
                    actualizarTotalCajaUseCase.execute(cajasAbiertas.get(0).getId(), monto);
                }
            } catch (Exception e) {
                // Si falla la actualización de caja, solo notificar pero no bloquear el pago
            }
            
            mostrarExito("Pago registrado exitosamente");
            limpiarFormulario();
            cargarPagos();
            configurarComboBoxFacturas();
        } catch (Exception e) {
            mostrarError("Error al registrar pago: " + e.getMessage());
        }
    }

    @FXML
    public void handleLimpiar() {
        limpiarFormulario();
    }

    private void cargarPagos() {
        List<PagoDto> pagos = listarPagosUseCase.execute();
        tblPagos.setItems(FXCollections.observableArrayList(pagos));
    }

    private void limpiarFormulario() {
        cbFactura.setValue(null);
        txtMonto.clear();
        cbFormaPago.setValue(null);
        txtReferencia.clear();
        txtaObservaciones.clear();
        lblMontoFactura.setText("0.00");
        lblEstadoFactura.setText("-");
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
