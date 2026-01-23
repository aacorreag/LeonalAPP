package com.leonal.ui.controller;

import com.leonal.application.dto.caja.CajaSessionDto;
import com.leonal.application.usecase.caja.AbrirCajaUseCase;
import com.leonal.application.usecase.caja.CerrarCajaUseCase;
import com.leonal.application.usecase.caja.ListarCajasUseCase;
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
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CajaController {
    private final AbrirCajaUseCase abrirCajaUseCase;
    private final CerrarCajaUseCase cerrarCajaUseCase;
    private final ListarCajasUseCase listarCajasUseCase;
    private final UserSession userSession;

    // Sección Apertura de Caja
    @FXML
    private TextField txtMontoInicial;
    @FXML
    private Label lblEstadoCaja;
    @FXML
    private Label lblHoraApertura;
    @FXML
    private Button btnAbrirCaja;
    @FXML
    private Button btnCerrarCaja;

    // Sección Cierre de Caja
    @FXML
    private TextField txtMontoFinal;
    @FXML
    private TextField txtDiferencia;
    @FXML
    private TextArea txtaObservacionesCierre;
    @FXML
    private Label lblTotalIngresos;
    @FXML
    private Label lblTotalEgresos;

    // Tabla de Cajas
    @FXML
    private TableView<CajaSessionDto> tblCajas;
    @FXML
    private TableColumn<CajaSessionDto, String> colCajero;
    @FXML
    private TableColumn<CajaSessionDto, String> colFecha;
    @FXML
    private TableColumn<CajaSessionDto, LocalDateTime> colHoraApertura;
    @FXML
    private TableColumn<CajaSessionDto, LocalDateTime> colHoraCierre;
    @FXML
    private TableColumn<CajaSessionDto, String> colEstado;
    @FXML
    private TableColumn<CajaSessionDto, BigDecimal> colMontoInicial;
    @FXML
    private TableColumn<CajaSessionDto, BigDecimal> colMontoFinal;

    private CajaSessionDto cajaActual;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarCajas();
        actualizarEstadoCaja();
    }

    private void configurarTabla() {
        colCajero.setCellValueFactory(new PropertyValueFactory<>("usuarioCajeroNombre"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHoraApertura.setCellValueFactory(new PropertyValueFactory<>("horaApertura"));
        colHoraCierre.setCellValueFactory(new PropertyValueFactory<>("horaCierre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colMontoInicial.setCellValueFactory(new PropertyValueFactory<>("montoInicial"));
        colMontoFinal.setCellValueFactory(new PropertyValueFactory<>("montoFinal"));
    }

    @FXML
    public void handleAbrirCaja() {
        try {
            BigDecimal montoInicial = txtMontoInicial.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtMontoInicial.getText());

            UUID usuarioCajeroId = userSession.getCurrentUser() != null ? userSession.getCurrentUser().getId() : UUID.randomUUID();
            String usuarioCajeroNombre = userSession.getCurrentUser() != null ? userSession.getCurrentUser().getNombreCompleto() : "Sistema";
            cajaActual = abrirCajaUseCase.execute(usuarioCajeroId, usuarioCajeroNombre, montoInicial);
            mostrarExito("Caja abierta exitosamente a las " + cajaActual.getHoraApertura());
            actualizarEstadoCaja();
            cargarCajas();
        } catch (Exception e) {
            mostrarError("Error al abrir caja: " + e.getMessage());
        }
    }

    @FXML
    public void handleCerrarCaja() {
        try {
            if (cajaActual == null) {
                mostrarError("No hay caja abierta");
                return;
            }

            BigDecimal montoFinal = new BigDecimal(txtMontoFinal.getText());

            cajaActual = cerrarCajaUseCase.execute(cajaActual.getId(), montoFinal, txtaObservacionesCierre.getText());
            mostrarExito("Caja cerrada exitosamente");
            limpiarFormulario();
            actualizarEstadoCaja();
            cargarCajas();
        } catch (Exception e) {
            mostrarError("Error al cerrar caja: " + e.getMessage());
        }
    }

    @FXML
    public void handleCalcularDiferencia() {
        try {
            if (cajaActual == null || cajaActual.getMontoInicial() == null) {
                mostrarError("Debe abrir caja primero");
                return;
            }

            BigDecimal montoFinal = new BigDecimal(txtMontoFinal.getText());
            BigDecimal diferencia = montoFinal.subtract(cajaActual.getMontoInicial());
            txtDiferencia.setText(diferencia.toPlainString());
        } catch (Exception e) {
            txtDiferencia.setText("Err");
        }
    }

    private void actualizarEstadoCaja() {
        List<CajaSessionDto> cajas = listarCajasUseCase.executeByEstado("ABIERTA");
        if (!cajas.isEmpty()) {
            cajaActual = cajas.get(0);
            lblEstadoCaja.setText("ABIERTA");
            lblHoraApertura.setText(cajaActual.getHoraApertura().toString());
            lblTotalIngresos.setText(cajaActual.getTotalIngresos().toPlainString());
            lblTotalEgresos.setText(cajaActual.getTotalEgresos().toPlainString());
            btnAbrirCaja.setDisable(true);
            btnCerrarCaja.setDisable(false);
            txtMontoInicial.setDisable(true);
        } else {
            cajaActual = null;
            lblEstadoCaja.setText("CERRADA");
            lblHoraApertura.setText("-");
            lblTotalIngresos.setText("0.00");
            lblTotalEgresos.setText("0.00");
            btnAbrirCaja.setDisable(false);
            btnCerrarCaja.setDisable(true);
            txtMontoInicial.setDisable(false);
        }
    }

    private void cargarCajas() {
        List<CajaSessionDto> cajas = listarCajasUseCase.execute();
        tblCajas.setItems(FXCollections.observableArrayList(cajas));
    }

    private void limpiarFormulario() {
        txtMontoInicial.clear();
        txtMontoFinal.clear();
        txtDiferencia.clear();
        txtaObservacionesCierre.clear();
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
