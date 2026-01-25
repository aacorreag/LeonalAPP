package com.leonal.ui.controller;

import javafx.application.Platform;
import com.leonal.application.dto.examen.ExamenDto;
import com.leonal.application.dto.orden.CreateOrdenRequest;
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
import javafx.util.StringConverter; // Import necesario
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
    
    // --- NUEVO: ComboBox para seleccionar paciente ---
    @FXML
    private ComboBox<Paciente> cmbPacientes;
    
    @FXML
    private Label lblPacienteSeleccionado;
    
    // Campos para paciente nuevo
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
    
    // Exámenes
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
        setupPacienteSelection(); // Configuración separada para el combo
    }

    // --- NUEVO MÉTODO: Configurar el ComboBox de Pacientes ---
    private void setupPacienteSelection() {
        // 1. Cargar la lista base y envolverla en una lista filtrable
        List<Paciente> listaBase = listarPacientesUseCase.execute();
        ObservableList<Paciente> items = FXCollections.observableArrayList(listaBase);
        FilteredList<Paciente> filteredItems = new FilteredList<>(items, p -> true);

        cmbPacientes.setItems(filteredItems);
        cmbPacientes.setEditable(true); // <--- ESTO ES CLAVE: Permite escribir dentro

        // 2. Definir cómo se muestra el texto (Documento - Nombre)
        cmbPacientes.setConverter(new StringConverter<Paciente>() {
            @Override
            public String toString(Paciente p) {
                if (p == null) return null;
                return p.getNumeroDocumento() + " - " + p.getNombre();
            }

            @Override
            public Paciente fromString(String string) {
                // Retorna null si el texto no coincide exactamente con un objeto (no crítico para búsqueda)
                return cmbPacientes.getItems().stream()
                        .filter(p -> (p.getNumeroDocumento() + " - " + p.getNombre()).equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // 3. Lógica de Filtrado (El "Buscador")
        cmbPacientes.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                // Si el cambio de texto fue porque seleccionamos un usuario, NO filtramos
                Paciente selected = cmbPacientes.getSelectionModel().getSelectedItem();
                if (selected != null && 
                   (selected.getNumeroDocumento() + " - " + selected.getNombre()).equals(newVal)) {
                    return;
                }

                // Si está vacío, mostrar todos. Si no, filtrar por nombre o cédula.
                if (newVal == null || newVal.isEmpty()) {
                    filteredItems.setPredicate(p -> true);
                } else {
                    String lowerVal = newVal.toLowerCase();
                    filteredItems.setPredicate(p -> 
                        p.getNombre().toLowerCase().contains(lowerVal) || 
                        p.getNumeroDocumento().toLowerCase().contains(lowerVal)
                    );
                }
                
                // Si hay resultados y el usuario está escribiendo, desplegar la lista automáticamente
                if (!filteredItems.isEmpty() && !cmbPacientes.isShowing()) {
                    cmbPacientes.show();
                }
            });
        });

        // 4. Listener de Selección (Llenar datos al hacer clic)
        cmbPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                pacienteSeleccionado = newVal;
                lblPacienteSeleccionado.setText("✓ Seleccionado: " + newVal.getNombre());
                //txtBuscarPaciente.setText(""); // Limpiar el otro buscador
            }
        });
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
            
            // Reseteamos selección al cambiar de modo
            if (esNuevo) {
                pacienteSeleccionado = null;
                lblPacienteSeleccionado.setText("");
                cmbPacientes.getSelectionModel().clearSelection();
            }
        });

        // Setup combos nuevo paciente
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
                    showAlert("Error", "Debe seleccionar un paciente de la lista o buscarlo");
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