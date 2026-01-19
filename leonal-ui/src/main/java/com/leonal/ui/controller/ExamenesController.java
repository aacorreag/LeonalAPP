package com.leonal.ui.controller;

import com.leonal.application.dto.examen.CreateExamenRequest;
import com.leonal.application.dto.examen.ExamenDto;
import com.leonal.application.usecase.examen.GuardarExamenUseCase;
import com.leonal.application.usecase.examen.ListarExamenesUseCase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class ExamenesController {

  private final ListarExamenesUseCase listarExamenesUseCase;
  private final GuardarExamenUseCase guardarExamenUseCase;
  private final com.leonal.ui.context.UserSession userSession; // Injected

  @FXML
  private TableView<ExamenDto> tablaExamenes;
  @FXML
  private TableColumn<ExamenDto, String> colCodigo;
  @FXML
  private TableColumn<ExamenDto, String> colNombre;
  @FXML
  private TableColumn<ExamenDto, BigDecimal> colPrecio;
  @FXML
  private TableColumn<ExamenDto, String> colMetodo;
  @FXML
  private TableColumn<ExamenDto, Boolean> colActivo;

  @FXML
  private VBox formContainer;
  @FXML
  private TextField txtCodigo;
  @FXML
  private TextField txtNombre;
  @FXML
  private TextField txtMetodo;
  @FXML
  private TextField txtPrecio;
  @FXML
  private ComboBox<String> cmbTipoResultado;
  @FXML
  private TextField txtUnidad;
  @FXML
  private CheckBox chkActivo;
  @FXML
  private Button btnNuevo;
  @FXML
  private Label lblFormTitle;
  @FXML
  private Label lblEmptySelection;
  @FXML
  private Button btnGuardar; // Need to inject this to hide it
  @FXML
  private Button btnCancelar; // Need to inject this

  private UUID currentExamenId;
  private final ObservableList<ExamenDto> examenesList = FXCollections.observableArrayList();
  private boolean isAdmin;

  public void initialize() {
    checkPermissions();
    setupTable();
    setupForm();
    loadExamenes();
    updateUiState(false);
  }

  private void checkPermissions() {
    if (userSession.getCurrentUser() != null) {
      this.isAdmin = userSession.getCurrentUser().getRoles().contains("ADMIN");
    }
    if (!isAdmin) {
      btnNuevo.setVisible(false);
      btnNuevo.setManaged(false);
    }
  }

  private void setupTable() {
    colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoInterno"));
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodo"));
    colActivo.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().isActivo()));

    // Custom cell for Activo
    colActivo.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item ? "Sí" : "No");
          setStyle(item ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        }
      }
    });

    tablaExamenes.setItems(examenesList);
    tablaExamenes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        mostrarDetalle(newVal);
      }
    });
  }

  private void setupForm() {
    cmbTipoResultado.setItems(FXCollections.observableArrayList("NUMERICO", "TEXTO", "SELECCION"));
    cmbTipoResultado.setValue("NUMERICO");
    resetForm();
  }

  private void loadExamenes() {
    examenesList.setAll(listarExamenesUseCase.ejecutar());
  }

  @FXML
  public void nuevoExamen() {
    if (!isAdmin)
      return; // Guard clause
    tablaExamenes.getSelectionModel().clearSelection();
    resetForm();
    updateUiState(true);
    lblFormTitle.setText("Nuevo Examen");
    txtCodigo.requestFocus();
  }

  private void mostrarDetalle(ExamenDto dto) {
    currentExamenId = dto.getId();
    txtCodigo.setText(dto.getCodigoInterno());
    txtNombre.setText(dto.getNombre());
    txtMetodo.setText(dto.getMetodo());
    txtPrecio.setText(dto.getPrecio() != null ? dto.getPrecio().toString() : "0");
    cmbTipoResultado.setValue(dto.getTipoResultado());
    txtUnidad.setText(dto.getUnidadMedida());
    chkActivo.setSelected(dto.isActivo());

    updateUiState(true);
    lblFormTitle.setText("Editar Examen");

    if (!isAdmin) {
      formContainer.setDisable(true); // Disable entire form
      // Alternatively disable individual fields if better visual is needed
      // but setDisable(true) is clear: Read Only.
    } else {
      formContainer.setDisable(false);
    }
  }

  @FXML
  public void guardarExamen() {
    try {
      if (txtCodigo.getText().isBlank() || txtNombre.getText().isBlank()) {
        mostrarAlerta("Error", "Código y Nombre son obligatorios");
        return;
      }

      BigDecimal precio = new BigDecimal(txtPrecio.getText().replaceAll(",", "."));

      CreateExamenRequest request = CreateExamenRequest.builder()
          .id(currentExamenId)
          .codigoInterno(txtCodigo.getText())
          .nombre(txtNombre.getText())
          .metodo(txtMetodo.getText())
          .tipoResultado(cmbTipoResultado.getValue())
          .unidadMedida(txtUnidad.getText())
          .precio(precio)
          .activo(chkActivo.isSelected())
          .build();

      guardarExamenUseCase.ejecutar(request);

      mostrarAlerta("Éxito", "Examen guardado correctamente");
      loadExamenes();
      nuevoExamen(); // Reset to new mode or keep selection? Let's clear
      updateUiState(false);
      tablaExamenes.getSelectionModel().clearSelection();

    } catch (NumberFormatException e) {
      mostrarAlerta("Error", "Precio inválido");
    } catch (Exception e) {
      mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
    }
  }

  @FXML
  public void cancelarEdicion() {
    resetForm();
    updateUiState(false);
    tablaExamenes.getSelectionModel().clearSelection();
  }

  private void updateUiState(boolean editing) {
    formContainer.setVisible(editing);
    if (lblEmptySelection != null) {
      lblEmptySelection.setVisible(!editing);
    }
  }

  private void resetForm() {
    currentExamenId = null;
    txtCodigo.clear();
    txtNombre.clear();
    txtMetodo.clear();
    txtPrecio.setText("0");
    cmbTipoResultado.setValue("NUMERICO");
    txtUnidad.clear();
    chkActivo.setSelected(true);
    lblFormTitle.setText("Nuevo Examen");
  }

  private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
  }
}
