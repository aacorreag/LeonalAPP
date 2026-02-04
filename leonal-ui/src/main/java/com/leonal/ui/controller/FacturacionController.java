package com.leonal.ui.controller;

import com.leonal.application.dto.factura.CreateFacturaRequest;
import com.leonal.application.dto.factura.FacturaDto;
import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.application.usecase.factura.CrearFacturaUseCase;
import com.leonal.application.usecase.factura.ListarFacturasUseCase;
import com.leonal.application.usecase.orden.ListarOrdenesUseCase;
import com.leonal.application.usecase.orden.ObtenerOrdenPorIdUseCase;
import com.leonal.application.usecase.caja.ActualizarTotalCajaUseCase;
import com.leonal.application.usecase.caja.ListarCajasUseCase;
import com.leonal.ui.context.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class FacturacionController {
    private final CrearFacturaUseCase crearFacturaUseCase;
    private final ListarFacturasUseCase listarFacturasUseCase;
    private final ListarOrdenesUseCase listarOrdenesUseCase;
    private final com.leonal.application.usecase.orden.ListarOrdenesSinFacturaUseCase listarOrdenesSinFacturaUseCase;
    private final ObtenerOrdenPorIdUseCase obtenerOrdenPorIdUseCase;
    private final ActualizarTotalCajaUseCase actualizarTotalCajaUseCase;
    private final ListarCajasUseCase listarCajasUseCase;
    private final com.leonal.application.usecase.factura.GenerarReporteFacturaUseCase generarReporteFacturaUseCase;
    private final UserSession userSession;

    // Sección Selección de Orden
    @FXML
    private ComboBox<String> cbOrdenes;
    @FXML
    private Button btnCargarOrden;

    // Sección Datos de Factura
    @FXML
    private Label lblNumeroOrden;
    @FXML
    private Label lblPaciente;
    @FXML
    private Label lblDocumento;
    @FXML
    private Label lblSubtotal;
    @FXML
    private TextArea txtaObservaciones;
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
    @FXML
    private Button btnImprimir; // Botón nuevo o existente si se agrega al FXML

    private OrdenDto ordenActual;
    private Map<String, UUID> mapaOrdenes = new HashMap<>();

    @FXML
    public void initialize() {
        configurarTabla();
        configurarComboBox();
        cargarOrdenes();
        cargarFacturas();
        if (btnImprimir != null) {
            btnImprimir.setDisable(true); // Deshabilitado al inicio
        }

        // Listener para selección en tabla
        tblFacturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && btnImprimir != null) {
                btnImprimir.setDisable(false);
            }
        });
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
                "EMITIDA", "PAGADA", "ANULADA"));
    }

    @FXML
    public void handleCargarOrden() {
        String selectedOrden = cbOrdenes.getValue();
        if (selectedOrden == null || selectedOrden.isEmpty()) {
            mostrarError("Seleccione una orden");
            return;
        }

        try {
            UUID ordenId = mapaOrdenes.get(selectedOrden);
            ordenActual = obtenerOrdenPorIdUseCase.execute(ordenId);
            mostrarDatosOrden();
        } catch (Exception e) {
            mostrarError("Error al cargar orden: " + e.getMessage());
        }
    }

    private void mostrarDatosOrden() {
        lblNumeroOrden.setText(ordenActual.getCodigoOrden());
        lblPaciente.setText(ordenActual.getPacienteNombre());
        lblDocumento.setText(ordenActual.getPacienteDocumento());
        lblSubtotal.setText(ordenActual.getTotal().toPlainString());
        txtDescuento.clear();
        txtImpuesto.clear();
        handleCalcularTotal();
    }

    private void cargarOrdenes() {
        try {
            List<OrdenDto> ordenes = listarOrdenesSinFacturaUseCase.ejecutar();
            mapaOrdenes.clear();

            List<String> opcionesOrdenes = ordenes.stream()
                    .map(o -> {
                        String opcion = String.format("%s - %s", o.getCodigoOrden(), o.getPacienteNombre());
                        mapaOrdenes.put(opcion, o.getId());
                        return opcion;
                    })
                    .toList();

            cbOrdenes.setItems(FXCollections.observableArrayList(opcionesOrdenes));
        } catch (Exception e) {
            mostrarError("Error al cargar órdenes: " + e.getMessage());
        }
    }

    @FXML
    public void handleGuardar() {
        try {
            if (ordenActual == null) {
                mostrarError("Debe cargar una orden primero");
                return;
            }

            BigDecimal subtotal = ordenActual.getTotal();
            BigDecimal descuento = txtDescuento.getText().isEmpty() ? BigDecimal.ZERO
                    : new BigDecimal(txtDescuento.getText());
            BigDecimal impuesto = txtImpuesto.getText().isEmpty() ? BigDecimal.ZERO
                    : new BigDecimal(txtImpuesto.getText());

            CreateFacturaRequest request = CreateFacturaRequest.builder()
                    .ordenId(ordenActual.getId())
                    .subtotal(subtotal)
                    .descuento(descuento)
                    .impuesto(impuesto)
                    .observaciones(txtaObservaciones.getText())
                    .build();

            UUID usuarioId = userSession.getCurrentUser() != null ? userSession.getCurrentUser().getId()
                    : UUID.randomUUID();
            FacturaDto factura = crearFacturaUseCase.execute(request, ordenActual.getPacienteId(),
                    ordenActual.getPacienteNombre(), ordenActual.getPacienteDocumento(), usuarioId);

            // Actualizar totales de caja abierta si existe
            try {
                var cajasAbiertas = listarCajasUseCase.executeByEstado("ABIERTA");
                if (!cajasAbiertas.isEmpty()) {
                    actualizarTotalCajaUseCase.execute(cajasAbiertas.get(0).getId(), factura.getTotal());
                }
            } catch (Exception e) {
                // Si falla la actualización de caja, solo notificar pero no bloquear la
                // creación de factura
            }

            mostrarExito("Factura creada: " + factura.getNumero());

            // Preguntar si desea imprimir
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Imprimir Factura");
            alert.setHeaderText("Factura creada exitosamente");
            alert.setContentText("¿Desea imprimir la factura ahora?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                imprimirFactura(factura.getId(), factura.getNumero());
            }

            limpiarFormulario();
            cargarFacturas();
            cargarOrdenes();
        } catch (Exception e) {
            mostrarError("Error al crear factura: " + e.getMessage());
        }
    }

    @FXML
    public void handleImprimir() {
        FacturaDto selected = tblFacturas.getSelectionModel().getSelectedItem();
        if (selected != null) {
            imprimirFactura(selected.getId(), selected.getNumero());
        } else {
            mostrarError("Seleccione una factura de la tabla para imprimir");
        }
    }

    private void imprimirFactura(UUID facturaId, String numeroFactura) {
        try {
            byte[] pdfBytes = generarReporteFacturaUseCase.execute(facturaId);

            // Guardar archivo temporal
            String filename = "factura_" + numeroFactura + ".pdf";
            java.nio.file.Path tempPath = java.nio.file.Path.of(System.getProperty("java.io.tmpdir"), filename);
            java.nio.file.Files.write(tempPath, pdfBytes);

            // Abrir archivo
            java.awt.Desktop.getDesktop().open(tempPath.toFile());

        } catch (Exception e) {
            mostrarError("Error al imprimir factura: " + e.getMessage());
            e.printStackTrace();
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
        ordenActual = null;
        lblNumeroOrden.setText("-");
        lblPaciente.setText("-");
        lblDocumento.setText("-");
        lblSubtotal.setText("0.00");
        txtDescuento.clear();
        txtImpuesto.clear();
        txtaObservaciones.clear();
        lblTotal.setText("0.00");
        cbOrdenes.setValue(null);
    }

    @FXML
    public void handleCalcularTotal() {
        try {
            BigDecimal subtotal = new BigDecimal(lblSubtotal.getText());
            BigDecimal descuento = txtDescuento.getText().isEmpty() ? BigDecimal.ZERO
                    : new BigDecimal(txtDescuento.getText());
            BigDecimal impuesto = txtImpuesto.getText().isEmpty() ? BigDecimal.ZERO
                    : new BigDecimal(txtImpuesto.getText());

            BigDecimal total = subtotal.subtract(descuento).add(impuesto);
            lblTotal.setText(total.toPlainString());
        } catch (Exception e) {
            lblTotal.setText("0.00");
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
