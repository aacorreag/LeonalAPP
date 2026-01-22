package com.leonal.ui.controller;

import com.leonal.application.dto.factura.CreateFacturaRequest;
import com.leonal.application.dto.factura.FacturaDto;
import com.leonal.application.usecase.factura.CrearFacturaUseCase;
import com.leonal.application.usecase.factura.ListarFacturasUseCase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class FacturacionController {
    private final CrearFacturaUseCase crearFacturaUseCase;
    private final ListarFacturasUseCase listarFacturasUseCase;

    @FXML
    private TextField txtNumeroOrden;
    @FXML
    private TextField txtPaciente;
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextArea txtaObservaciones;
    @FXML
    private TextField txtSubtotal;
    @FXML
    private TextField txtDescuento;
    @FXML
    private TextField txtImpuesto;
    @FXML
    private Label lblTotal;

    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TableView<FacturaDto> tblFacturas;
    @FXML
    private TableColumn<FacturaDto, String> colNumero;
    @FXML
    private TableColumn<FacturaDto, String> colFecha;
    @FXML
    private TableColumn<FacturaDto, String> colPaciente;
    @FXML
    private TableColumn<FacturaDto, String> colEstado;
    @FXML
    private TableColumn<FacturaDto, BigDecimal> colTotal;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnBuscar;

    private UUID ordenIdActual;
    private UUID pacienteIdActual;

    @FXML
    public void initialize() {
        configurarTabla();
        configurarComboBox();
        cargarFacturas();
    }

    private void configurarTabla() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void configurarComboBox() {
        cbEstado.setItems(FXCollections.observableArrayList(
                "BORRADOR", "EMITIDA", "PAGADA", "ANULADA"
        ));
    }

    @FXML
    public void handleGuardar() {
        try {
            BigDecimal subtotal = new BigDecimal(txtSubtotal.getText());
            BigDecimal descuento = txtDescuento.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtDescuento.getText());
            BigDecimal impuesto = txtImpuesto.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtImpuesto.getText());

            CreateFacturaRequest request = CreateFacturaRequest.builder()
                    .ordenId(ordenIdActual)
                    .subtotal(subtotal)
                    .descuento(descuento)
                    .impuesto(impuesto)
                    .observaciones(txtaObservaciones.getText())
                    .build();

            FacturaDto factura = crearFacturaUseCase.execute(request, pacienteIdActual, txtPaciente.getText(), txtDocumento.getText(), UUID.randomUUID());
            mostrarExito("Factura creada: " + factura.getNumero());
            limpiarFormulario();
            cargarFacturas();
        } catch (Exception e) {
            mostrarError("Error al crear factura: " + e.getMessage());
        }
    }

    @FXML
    public void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    public void handleBuscar() {
        String estadoSeleccionado = cbEstado.getValue();
        if (estadoSeleccionado != null && !estadoSeleccionado.isEmpty()) {
            List<FacturaDto> facturas = listarFacturasUseCase.executeByEstado(estadoSeleccionado);
            tblFacturas.setItems(FXCollections.observableArrayList(facturas));
        } else {
            cargarFacturas();
        }
    }

    private void cargarFacturas() {
        List<FacturaDto> facturas = listarFacturasUseCase.execute();
        tblFacturas.setItems(FXCollections.observableArrayList(facturas));
    }

    private void limpiarFormulario() {
        txtNumeroOrden.clear();
        txtPaciente.clear();
        txtDocumento.clear();
        txtSubtotal.clear();
        txtDescuento.clear();
        txtImpuesto.clear();
        txtaObservaciones.clear();
        lblTotal.setText("0.00");
        ordenIdActual = null;
        pacienteIdActual = null;
    }

    @FXML
    public void handleCalcularTotal() {
        try {
            BigDecimal subtotal = txtSubtotal.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtSubtotal.getText());
            BigDecimal descuento = txtDescuento.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtDescuento.getText());
            BigDecimal impuesto = txtImpuesto.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtImpuesto.getText());

            BigDecimal total = subtotal.subtract(descuento).add(impuesto);
            lblTotal.setText(total.toPlainString());
        } catch (Exception e) {
            lblTotal.setText("Err");
        }
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
