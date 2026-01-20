package com.leonal.ui.controller;

import com.leonal.application.dto.paciente.CreatePacienteRequest;
import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.domain.model.Paciente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
  private ComboBox<String> cbTipoDocumento;
  @FXML
  private TextField txtDocumento;
  @FXML
  private TextField txtNombre;
  @FXML
  private DatePicker dpNacimiento;
  @FXML
  private ComboBox<String> cbSexo;
  @FXML
  private TextField txtTelefono;
  @FXML
  private TextField txtEmail;
  @FXML
  private TextField txtDireccion;

  @FXML
  private TableView<Paciente> tblPacientes;
  @FXML
  private TableColumn<Paciente, String> colDocumento;
  @FXML
  private TableColumn<Paciente, String> colNombre;
  @FXML
  private TableColumn<Paciente, String> colEdad;
  @FXML
  private TableColumn<Paciente, String> colTelefono;
  @FXML
  private TableColumn<Paciente, String> colEmail;

  public void initialize() {
    // Inicializar Combos
    cbTipoDocumento.setItems(FXCollections.observableArrayList("C.C", "T.I", "Registro Civil", "PASAPORTE", "C.E"));
    cbSexo.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));

    // Configurar Columnas
    colDocumento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroDocumento()));
    colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
    colEdad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEdad())));
    colTelefono.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getTelefono() != null ? data.getValue().getTelefono() : ""));
    colEmail.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getEmail() != null ? data.getValue().getEmail() : ""));

    cargarPacientes();
    configurarDatePicker();
  }

  private void configurarDatePicker() {
    // Permitir edición manual
    dpNacimiento.setEditable(true);
    dpNacimiento.setPromptText("DD/MM/AAAA");

    String pattern = "dd/MM/yyyy";
    java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern(pattern);

    dpNacimiento.setConverter(new javafx.util.StringConverter<java.time.LocalDate>() {
      @Override
      public String toString(java.time.LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public java.time.LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          try {
            return java.time.LocalDate.parse(string, dateFormatter);
          } catch (Exception e) {
            return null;
          }
        } else {
          return null;
        }
      }
    });
  }

  private void cargarPacientes() {
    List<Paciente> pacientes = listarPacientesUseCase.execute();
    tblPacientes.setItems(FXCollections.observableArrayList(pacientes));
  }

  @FXML
  public void guardarPaciente() {
    try {
      if (validarCampos()) {
        CreatePacienteRequest request = CreatePacienteRequest.builder()
            .tipoDocumento(cbTipoDocumento.getValue())
            .numeroDocumento(txtDocumento.getText())
            .nombre(txtNombre.getText())
            .fechaNacimiento(dpNacimiento.getValue())
            .sexo(cbSexo.getValue())
            .telefono(txtTelefono.getText())
            .email(txtEmail.getText())
            .direccion(txtDireccion.getText())
            .build();

        crearPacienteUseCase.execute(request);

        mostrarAlerta("Éxito", "Paciente guardado correctamente");
        limpiarFormulario();
        cargarPacientes();
      }
    } catch (Exception e) {
      mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
    }
  }

  private boolean validarCampos() {
    StringBuilder errores = new StringBuilder();

    // Sincronizar editor manual si el valor es null
    if (dpNacimiento.getValue() == null && dpNacimiento.getEditor().getText() != null
        && !dpNacimiento.getEditor().getText().isEmpty()) {
      try {
        java.time.LocalDate parsedDate = dpNacimiento.getConverter().fromString(dpNacimiento.getEditor().getText());
        if (parsedDate != null) {
          dpNacimiento.setValue(parsedDate);
        }
      } catch (Exception e) {
        // Si falla el parseo, el error será capturado abajo
      }
    }

    if (cbTipoDocumento.getValue() == null) {
      errores.append("- Tipo de Documento\n");
    }
    if (txtDocumento.getText() == null || txtDocumento.getText().trim().isEmpty()) {
      errores.append("- Número de Documento\n");
    }
    if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
      errores.append("- Nombre Completo\n");
    }
    if (dpNacimiento.getValue() == null) {
      errores.append("- Fecha de Nacimiento (Asegúrese de usar el formato DD/MM/AAAA)\n");
    }

    if (errores.length() > 0) {
      mostrarAlerta("Atención", "Por favor complete los siguientes campos obligatorios:\n" + errores.toString());
      return false;
    }
    return true;
  }

  @FXML
  public void limpiarFormulario() {
    cbTipoDocumento.setValue(null);
    txtDocumento.clear();
    txtNombre.clear();
    dpNacimiento.setValue(null);
    cbSexo.setValue(null);
    txtTelefono.clear();
    txtEmail.clear();
    txtDireccion.clear();
  }

  private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
  }
}
