package com.leonal.ui.controller;

import com.leonal.application.dto.paciente.CreatePacienteRequest;
import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.domain.model.Paciente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PacienteController {

  private final CrearPacienteUseCase crearPacienteUseCase;
  private final ListarPacientesUseCase listarPacientesUseCase;

  @FXML
  private TextField txtDocumento;
  @FXML
  private TextField txtNombre;
  @FXML
  private DatePicker dpNacimiento;
  @FXML
  private TableView<Paciente> tblPacientes;
  @FXML
  private TableColumn<Paciente, String> colDocumento;
  @FXML
  private TableColumn<Paciente, String> colNombre;
  @FXML
  private TableColumn<Paciente, String> colEdad;

  public void initialize() {
    colDocumento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroDocumento()));
    colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
    colEdad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEdad())));

    cargarPacientes();
  }

  private void cargarPacientes() {
    List<Paciente> pacientes = listarPacientesUseCase.execute();
    tblPacientes.setItems(FXCollections.observableArrayList(pacientes));
  }

  @FXML
  public void guardarPaciente() {
    try {
      CreatePacienteRequest request = CreatePacienteRequest.builder()
          .tipoDocumento("CEDULA") // Hardcoded example
          .numeroDocumento(txtDocumento.getText())
          .nombre(txtNombre.getText())
          .fechaNacimiento(dpNacimiento.getValue())
          .build();

      crearPacienteUseCase.execute(request);

      mostrarAlerta("Éxito", "Paciente guardado correctamente");
      limpiarFormulario();
      cargarPacientes();
    } catch (Exception e) {
      mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
    }
  }

  private void limpiarFormulario() {
    txtDocumento.clear();
    txtNombre.clear();
    dpNacimiento.setValue(null);
  }

  private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
  }
}
