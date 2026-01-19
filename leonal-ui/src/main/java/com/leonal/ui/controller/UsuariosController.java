package com.leonal.ui.controller;

import com.leonal.application.dto.user.CreateUserRequest;
import com.leonal.application.dto.user.UserDto;
import com.leonal.application.usecase.user.GuardarUsuarioUseCase;
import com.leonal.application.usecase.user.ListarRolesUseCase;
import com.leonal.application.usecase.user.ListarUsuariosUseCase;
import com.leonal.domain.model.Rol;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UsuariosController {

  private final ListarUsuariosUseCase listarUsuariosUseCase;
  private final GuardarUsuarioUseCase guardarUsuarioUseCase;
  private final ListarRolesUseCase listarRolesUseCase;

  @FXML
  private TableView<UserDto> tablaUsuarios;
  @FXML
  private TableColumn<UserDto, String> colUsername;
  @FXML
  private TableColumn<UserDto, String> colNombre;
  @FXML
  private TableColumn<UserDto, String> colRoles;
  @FXML
  private TableColumn<UserDto, String> colActivo;

  @FXML
  private VBox formContainer;
  @FXML
  private Label lblEmptySelection;
  @FXML
  private Label lblFormTitle;

  @FXML
  private TextField txtUsername;
  @FXML
  private TextField txtNombre;
  @FXML
  private TextField txtEmail;
  @FXML
  private PasswordField txtPassword;
  @FXML
  private CheckBox chkActivo;
  @FXML
  private ListView<RolSelection> listRoles;

  private UserDto selectedUser;

  @FXML
  public void initialize() {
    setupTable();
    // Setup Roles ListView with Checkboxes using Wrapper
    listRoles.setCellFactory(CheckBoxListCell.forListView(RolSelection::selectedProperty));

    // Listen selection
    tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
      if (newV != null) {
        cargarUsuario(newV);
      }
    });

    refreshTable();
  }

  private void setupTable() {
    colUsername.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsername()));
    colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreCompleto()));
    colRoles.setCellValueFactory(d -> new SimpleStringProperty(String.join(", ", d.getValue().getRoles())));
    colActivo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isActivo() ? "Si" : "No"));
  }

  private void refreshTable() {
    tablaUsuarios.setItems(FXCollections.observableArrayList(listarUsuariosUseCase.execute()));
  }

  private void loadRoles() {
    List<Rol> allRoles = listarRolesUseCase.execute();
    List<RolSelection> selections = allRoles.stream()
        .map(r -> new RolSelection(r, false))
        .collect(Collectors.toList());
    listRoles.setItems(FXCollections.observableArrayList(selections));
  }

  @FXML
  public void nuevoUsuario() {
    selectedUser = null;
    tablaUsuarios.getSelectionModel().clearSelection();
    limpiarForm();
    loadRoles(); // Reset roles
    lblFormTitle.setText("Nuevo Usuario");
    toggleForm(true);
  }

  private void cargarUsuario(UserDto user) {
    selectedUser = user;
    lblFormTitle.setText("Editar Usuario");
    toggleForm(true);

    txtUsername.setText(user.getUsername());
    txtNombre.setText(user.getNombreCompleto());
    txtEmail.setText(user.getEmail());
    chkActivo.setSelected(user.isActivo());
    txtPassword.setText(""); // Always blank on edit

    // Map Roles
    List<Rol> allRoles = listarRolesUseCase.execute();
    List<RolSelection> selections = allRoles.stream()
        .map(r -> new RolSelection(r, user.getRoles().contains(r.getNombre())))
        .collect(Collectors.toList());
    listRoles.setItems(FXCollections.observableArrayList(selections));
  }

  @FXML
  public void guardarUsuario() {
    try {
      List<String> selectedRoles = listRoles.getItems().stream()
          .filter(RolSelection::isSelected)
          .map(rs -> rs.getRol().getNombre())
          .collect(Collectors.toList());

      if (selectedRoles.isEmpty()) {
        mostrarAlerta("Error", "Debe seleccionar al menos un rol");
        return;
      }

      CreateUserRequest request = CreateUserRequest.builder()
          .id(selectedUser != null ? selectedUser.getId() : null)
          .username(txtUsername.getText())
          .nombreCompleto(txtNombre.getText())
          .email(txtEmail.getText())
          .password(txtPassword.getText()) // Empty means no change for edit
          .activo(chkActivo.isSelected())
          .roles(selectedRoles)
          .build();

      guardarUsuarioUseCase.execute(request);

      mostrarAlerta("Éxito", "Usuario guardado correctamente");

      cancelarEdicion();
      refreshTable();

    } catch (Exception e) {
      e.printStackTrace();
      mostrarAlerta("Error", "No se pudo guardar el usuario: " + e.getMessage());
    }
  }

  @FXML
  public void cancelarEdicion() {
    toggleForm(false);
    tablaUsuarios.getSelectionModel().clearSelection();
    selectedUser = null;
  }

  private void toggleForm(boolean visible) {
    formContainer.setVisible(visible);
    lblEmptySelection.setVisible(!visible);
  }

  private void limpiarForm() {
    txtUsername.clear();
    txtNombre.clear();
    txtEmail.clear();
    txtPassword.clear();
    chkActivo.setSelected(true);
  }

  private void mostrarAlerta(String titulo, String content) {
    Alert a = new Alert(Alert.AlertType.INFORMATION);
    a.setTitle(titulo);
    a.setContentText(content);
    a.showAndWait();
  }

  // Helper Class for CheckListView
  @Data
  public static class RolSelection {
    private final Rol rol;
    private final BooleanProperty selected;

    public RolSelection(Rol rol, boolean initial) {
      this.rol = rol;
      this.selected = new SimpleBooleanProperty(initial);
    }

    public boolean isSelected() {
      return selected.get();
    }

    public BooleanProperty selectedProperty() {
      return selected;
    }

    @Override
    public String toString() {
      return rol.getNombre();
    }
  }
}
