package com.leonal.ui.controller;

import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.application.usecase.orden.ListarOrdenesUseCase;
import com.leonal.application.usecase.resultado.IngresarResultadosUseCase;
import com.leonal.application.usecase.resultado.ValidarOrdenUseCase;
import com.leonal.application.usecase.resultado.GenerarReporteResultadosUseCase;
import com.leonal.ui.context.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ResultadosController {

  private final ListarOrdenesUseCase listarOrdenesUseCase;
  private final IngresarResultadosUseCase ingresarResultadosUseCase;
  private final ValidarOrdenUseCase validarOrdenUseCase;
  private final GenerarReporteResultadosUseCase generarReporteResultadosUseCase;
  private final UserSession userSession;
  private final ApplicationContext applicationContext;

  @FXML
  private TextField txtFiltro;
  @FXML
  private TableView<OrdenDto> tblOrdenes;
  @FXML
  private TableColumn<OrdenDto, String> colNumero;
  @FXML
  private TableColumn<OrdenDto, String> colPaciente;
  @FXML
  private TableColumn<OrdenDto, String> colFecha;
  @FXML
  private TableColumn<OrdenDto, String> colEstado;
  @FXML
  private TableColumn<OrdenDto, Void> colAcciones;

  private ObservableList<OrdenDto> masterData = FXCollections.observableArrayList();

  public void initialize() {
    configurarColumnas();
    actualizarLista();
    configurarFiltro();
  }

  private void configurarColumnas() {
    colNumero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigoOrden()));
    colPaciente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPacienteNombre()));
    colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaRecepcion().toString()));
    colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));

    colAcciones.setCellFactory(param -> new TableCell<>() {
      private final Button btnIngresar = new Button("📝 Ingresar");
      private final Button btnValidar = new Button("✅ Validar");
      private final Button btnImprimir = new Button("🖨️ Reporte");
      private final HBox container = new HBox(5, btnIngresar, btnValidar, btnImprimir);

      {
        btnIngresar.getStyleClass().add("button-small");
        btnValidar.getStyleClass().addAll("button-small", "button-success");
        btnImprimir.getStyleClass().addAll("button-small", "button-secondary");

        btnIngresar.setOnAction(event -> {
          OrdenDto orden = getTableView().getItems().get(getIndex());
          abrirDialogoResultados(orden);
        });

        btnValidar.setOnAction(event -> {
          OrdenDto orden = getTableView().getItems().get(getIndex());
          confirmarValidacion(orden);
        });

        btnImprimir.setOnAction(event -> {
          OrdenDto orden = getTableView().getItems().get(getIndex());
          imprimirReporte(orden);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          OrdenDto orden = getTableView().getItems().get(getIndex());
          btnValidar.setVisible(!"VALIDADO".equals(orden.getEstado()) && !"ENTREGADO".equals(orden.getEstado()));
          btnImprimir.setVisible("VALIDADO".equals(orden.getEstado()) || "ENTREGADO".equals(orden.getEstado()));
          setGraphic(container);
        }
      }
    });
  }

  @FXML
  public void actualizarLista() {
    List<OrdenDto> ordenes = listarOrdenesUseCase.ejecutar();
    masterData.setAll(ordenes.stream()
        .filter(o -> !"ENTREGADO".equals(o.getEstado()))
        .collect(Collectors.toList()));
  }

  private void configurarFiltro() {
    FilteredList<OrdenDto> filteredData = new FilteredList<>(masterData, p -> true);
    txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(orden -> {
        if (newValue == null || newValue.isEmpty())
          return true;
        String lowerCaseFilter = newValue.toLowerCase();
        if (orden.getCodigoOrden().toLowerCase().contains(lowerCaseFilter))
          return true;
        if (orden.getPacienteNombre() != null && orden.getPacienteNombre().toLowerCase().contains(lowerCaseFilter))
          return true;
        return false;
      });
    });
    tblOrdenes.setItems(filteredData);
  }

  private void abrirDialogoResultados(OrdenDto orden) {
    try {
      javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
          getClass().getResource("/fxml/ingreso_resultados_dialog.fxml"));
      loader.setControllerFactory(applicationContext::getBean);
      javafx.scene.Parent root = loader.load();

      IngresoResultadosDialogController controller = loader.getController();
      controller.setOrden(orden);

      javafx.stage.Stage stage = new javafx.stage.Stage();
      stage.setTitle("Ingreso de Resultados - " + orden.getCodigoOrden());
      stage.setScene(new javafx.scene.Scene(root));
      stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      stage.showAndWait();

      actualizarLista();
    } catch (Exception e) {
      e.printStackTrace();
      mostrarAlerta("Error", "No se pudo abrir el diálogo: " + e.getMessage());
    }
  }

  private void confirmarValidacion(OrdenDto orden) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmar Validación");
    alert.setHeaderText("¿Desea validar técnicamente la Orden " + orden.getCodigoOrden() + "?");
    alert.setContentText("Una vez validada, los resultados podrán ser impresos.");

    if (alert.showAndWait().get() == ButtonType.OK) {
      validarOrdenUseCase.execute(orden.getId(), userSession.getCurrentUser().getId());
      actualizarLista();
    }
  }

  private void imprimirReporte(OrdenDto orden) {
    try {
      byte[] pdfData = generarReporteResultadosUseCase.execute(orden.getId());

      java.io.File tempFile = java.io.File.createTempFile("resultado_" + orden.getCodigoOrden(), ".pdf");
      try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
        fos.write(pdfData);
      }

      if (java.awt.Desktop.isDesktopSupported()) {
        java.awt.Desktop.getDesktop().open(tempFile);
      } else {
        mostrarAlerta("Información", "Reporte generado en: " + tempFile.getAbsolutePath());
      }
    } catch (Exception e) {
      e.printStackTrace();
      mostrarAlerta("Error", "No se pudo generar el reporte: " + e.getMessage());
    }
  }

  private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
  }
}
