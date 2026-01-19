package com.leonal.ui.controller;

import com.leonal.application.dto.examen.ExamenDto;
import com.leonal.application.dto.orden.CreateOrdenRequest;
import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.application.usecase.examen.ListarExamenesUseCase;
import com.leonal.application.usecase.orden.CrearOrdenUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.domain.model.Paciente;
import com.leonal.ui.context.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NuevaOrdenController {

  private final CrearOrdenUseCase crearOrdenUseCase;
  private final ListarExamenesUseCase listarExamenesUseCase;
  private final ListarPacientesUseCase listarPacientesUseCase;
  private final UserSession userSession;

  private Stage dialogStage;
  private boolean success = false;

  @FXML
  private RadioButton rbPacienteExistente;
  @FXML
  private RadioButton rbPacienteNuevo;
  @FXML
  private HBox panelBuscarPaciente;
  @FXML
  private VBox panelNuevoPaciente;
  @FXML
  private TextField txtBuscarPaciente;
  @FXML
  private Label lblPacienteSeleccionado;
  @FXML
  private ComboBox<String> cmbTipoDoc;
  @FXML
  private TextField txtNumeroDoc;
  @FXML
  private TextField txtNombrePaciente;
  @FXML
  private DatePicker dpFechaNac;
  @FXML
  private ComboBox<String> cmbSexo;
  @FXML
  private TextField txtTelefono;
  @FXML
  private TextField txtFiltroExamen;
  @FXML
  private ListView<ExamenDto> listExamenesDisponibles;
  @FXML
  private ListView<ExamenDto> listExamenesSeleccionados;
  @FXML
  private Label txtMontoTotal;
  @FXML
  private Button btnGuardar;

  private final ObservableList<ExamenDto> examenesDisponibles = FXCollections.observableArrayList();
  private final ObservableList<ExamenDto> examenesSeleccionados = FXCollections.observableArrayList();
  private Paciente pacienteSeleccionado;

  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }

  public boolean isSuccess() {
    return success;
  }

  public void initialize() {
    setupDialog();
  }

  private void setupDialog() {
    // Setup toggle group
    ToggleGroup tg = new ToggleGroup();
    rbPacienteExistente.setToggleGroup(tg);
    rbPacienteNuevo.setToggleGroup(tg);

    tg.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
      boolean esNuevo = newVal == rbPacienteNuevo;
      panelBuscarPaciente.setVisible(!esNuevo);
      panelBuscarPaciente.setManaged(!esNuevo);
      panelNuevoPaciente.setVisible(esNuevo);
      panelNuevoPaciente.setManaged(esNuevo);
      pacienteSeleccionado = null;
      lblPacienteSeleccionado.setText("");
    });

    // Setup combos
    cmbTipoDoc.setItems(FXCollections.observableArrayList("CC", "TI", "CE", "PA"));
    cmbTipoDoc.setValue("CC");
    cmbSexo.setItems(FXCollections.observableArrayList("M", "F", "O"));
    cmbSexo.setValue("M");

    // Load exámenes
    List<ExamenDto> examenes = listarExamenesUseCase.ejecutar();
    examenesDisponibles.setAll(examenes.stream().filter(ExamenDto::isActivo).collect(Collectors.toList()));
    examenesSeleccionados.clear();

    // Setup filtered list for exámenes
    FilteredList<ExamenDto> filteredExamenes = new FilteredList<>(examenesDisponibles, p -> true);
    txtFiltroExamen.textProperty().addListener((obs, old, newVal) -> {
      filteredExamenes.setPredicate(e -> {
        if (newVal == null || newVal.isEmpty())
          return true;
        String lowerFilter = newVal.toLowerCase();
        return e.getNombre().toLowerCase().contains(lowerFilter) ||
            e.getCodigoInterno().toLowerCase().contains(lowerFilter);
      });
    });

    listExamenesDisponibles.setItems(filteredExamenes);
    listExamenesSeleccionados.setItems(examenesSeleccionados);

    // Custom cell factory
    listExamenesDisponibles.setCellFactory(lv -> new ListCell<>() {
      @Override
      protected void updateItem(ExamenDto item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null)
          setText(null);
        else
          setText(String.format("%s - %s ($%,.0f)", item.getCodigoInterno(), item.getNombre(), item.getPrecio()));
      }
    });

    listExamenesSeleccionados.setCellFactory(lv -> new ListCell<>() {
      @Override
      protected void updateItem(ExamenDto item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null)
          setText(null);
        else
          setText(String.format("%s - %s ($%,.0f)", item.getCodigoInterno(), item.getNombre(), item.getPrecio()));
      }
    });

    updateTotal();
  }

  @FXML
  public void buscarPaciente() {
    String query = txtBuscarPaciente.getText().trim();
    if (query.isEmpty())
      return;

    var resultados = listarPacientesUseCase.buscar(query);
    if (resultados.isEmpty()) {
      showAlert("No encontrado", "No se encontró ningún paciente.");
      pacienteSeleccionado = null;
      lblPacienteSeleccionado.setText("");
    } else if (resultados.size() == 1) {
      pacienteSeleccionado = resultados.get(0);
      lblPacienteSeleccionado.setText("✓ " + pacienteSeleccionado.getNombre());
    } else {
      ChoiceDialog<Paciente> dialog = new ChoiceDialog<>(resultados.get(0), resultados);
      dialog.showAndWait().ifPresent(p -> {
        pacienteSeleccionado = p;
        lblPacienteSeleccionado.setText("✓ " + p.getNombre());
      });
    }
  }

  @FXML
  public void agregarExamen() {
    ExamenDto selected = listExamenesDisponibles.getSelectionModel().getSelectedItem();
    if (selected != null && !examenesSeleccionados.contains(selected)) {
      examenesSeleccionados.add(selected);
      updateTotal();
    }
  }

  @FXML
  public void quitarExamen() {
    ExamenDto selected = listExamenesSeleccionados.getSelectionModel().getSelectedItem();
    if (selected != null) {
      examenesSeleccionados.remove(selected);
      updateTotal();
    }
  }

  private void updateTotal() {
    BigDecimal total = examenesSeleccionados.stream()
        .map(ExamenDto::getPrecio)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    txtMontoTotal.setText(String.format("$%,.0f", total));
  }

  @FXML
  public void guardarOrden() {
    try {
      if (examenesSeleccionados.isEmpty()) {
        showAlert("Error", "Debe agregar al menos un examen");
        return;
      }

      CreateOrdenRequest.CreateOrdenRequestBuilder requestBuilder = CreateOrdenRequest.builder();

      if (rbPacienteExistente.isSelected()) {
        if (pacienteSeleccionado == null) {
          showAlert("Error", "Debe seleccionar un paciente");
          return;
        }
        requestBuilder.pacienteId(pacienteSeleccionado.getId());
      } else {
        if (txtNumeroDoc.getText().isBlank() || txtNombrePaciente.getText().isBlank()
            || dpFechaNac.getValue() == null) {
          showAlert("Error", "Complete los datos del paciente");
          return;
        }
        requestBuilder.nuevoPaciente(CreateOrdenRequest.NuevoPacienteData.builder()
            .tipoDocumento(cmbTipoDoc.getValue())
            .numeroDocumento(txtNumeroDoc.getText().trim())
            .nombre(txtNombrePaciente.getText().trim())
            .fechaNacimiento(dpFechaNac.getValue())
            .sexo(cmbSexo.getValue())
            .telefono(txtTelefono.getText().trim())
            .build());
      }

      requestBuilder.examenIds(examenesSeleccionados.stream().map(ExamenDto::getId).collect(Collectors.toList()));
      if (userSession.getCurrentUser() != null)
        requestBuilder.usuarioCreacionId(userSession.getCurrentUser().getId());

      crearOrdenUseCase.ejecutar(requestBuilder.build());
      success = true;
      dialogStage.close();
    } catch (Exception e) {
      showAlert("Error", "Error al guardar: " + e.getMessage());
    }
  }

  @FXML
  public void cancelar() {
    dialogStage.close();
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
