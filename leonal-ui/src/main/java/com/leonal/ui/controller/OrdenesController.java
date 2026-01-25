package com.leonal.ui.controller;

import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.application.usecase.orden.ListarOrdenesUseCase;
import com.leonal.application.usecase.report.GenerarComprobanteOrdenUseCase;
import com.leonal.ui.context.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class OrdenesController {

  private final ListarOrdenesUseCase listarOrdenesUseCase;
  private final GenerarComprobanteOrdenUseCase generarComprobanteOrdenUseCase;
  private final org.springframework.context.ApplicationContext applicationContext;

  @FXML
  private TableView<OrdenDto> tablaOrdenes;
  @FXML
  private TableColumn<OrdenDto, String> colCodigo;
  @FXML
  private TableColumn<OrdenDto, String> colPaciente;
  @FXML
  private TableColumn<OrdenDto, String> colDocumento;
  @FXML
  private TableColumn<OrdenDto, String> colFecha;
  @FXML
  private TableColumn<OrdenDto, String> colEstado;
  @FXML
  private TableColumn<OrdenDto, Integer> colItems;
  @FXML
  private TableColumn<OrdenDto, BigDecimal> colTotal;
  @FXML
  private TableColumn<OrdenDto, Void> colAcciones;
  @FXML
  private Button btnNuevaOrden;

  private final ObservableList<OrdenDto> ordenesList = FXCollections.observableArrayList();

  public void initialize() {
    setupTable();
    loadOrdenes();
  }

  private void setupTable() {
    colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoOrden"));
    colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
    colDocumento.setCellValueFactory(new PropertyValueFactory<>("pacienteDocumento"));

    colFecha.setCellValueFactory(cell -> {
      var fecha = cell.getValue().getFechaRecepcion();
      String formatted = fecha != null
              ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
              : "";
      return new SimpleStringProperty(formatted);
    });

    colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

    // 👉 COLOR POR ESTADO
    colEstado.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(String estado, boolean empty) {
        super.updateItem(estado, empty);

        getStyleClass().removeAll(
                "estado-validado",
                "estado-proceso",
                "estado-cancelado",
                "estado-default"
        );

        if (empty || estado == null) {
          setText(null);
          return;
        }

        setText(estado);

        switch (estado.toUpperCase()) {
          case "VALIDADO" -> getStyleClass().add("estado-validado");
          case "PROCESO" -> getStyleClass().add("estado-proceso");
          case "CANCELADO" -> getStyleClass().add("estado-cancelado");
          default -> getStyleClass().add("estado-default");
        }
      }
    });


    colItems.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
    colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

    // Formato moneda
    colTotal.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(BigDecimal item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(String.format("$%,.0f", item));
        }
      }
    });

    tablaOrdenes.setItems(ordenesList);
    setupAccionesColumn();
  }

  private void setupAccionesColumn() {
    colAcciones.setCellFactory(param -> new TableCell<>() {
      private final Button btnPrint = new Button("🖨️");

      {
        btnPrint.getStyleClass().add("button-icon");
        btnPrint.setOnAction(event -> {
          OrdenDto orden = getTableView().getItems().get(getIndex());
          imprimirComprobante(orden);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btnPrint);
      }
    });
  }

  private void imprimirComprobante(OrdenDto ordenDto) {
    try {
      byte[] pdfBytes = generarComprobanteOrdenUseCase.ejecutar(ordenDto.getId());

      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Guardar Comprobante de Orden");
      fileChooser.setInitialFileName("Orden_" + ordenDto.getCodigoOrden() + ".pdf");
      fileChooser.getExtensionFilters().add(
              new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
      );

      java.io.File file = fileChooser.showSaveDialog(tablaOrdenes.getScene().getWindow());
      if (file != null) {
        java.nio.file.Files.write(file.toPath(), pdfBytes);
        showAlert("Éxito", "Comprobante guardado correctamente en:\n" + file.getAbsolutePath());
      }
    } catch (Exception e) {
      e.printStackTrace();
      showAlert("Error", "No se pudo generar el comprobante:\n" + e.getMessage());
    }
  }

  private void loadOrdenes() {
    ordenesList.setAll(listarOrdenesUseCase.ejecutar());
  }

  @FXML
  public void abrirNuevaOrden() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/nueva_orden_form.fxml"));
      loader.setControllerFactory(applicationContext::getBean);
      VBox dialogContent = loader.load();

      NuevaOrdenController controller = loader.getController();
      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setTitle("Nueva Orden");
      dialogStage.setScene(new Scene(dialogContent));

      controller.setDialogStage(dialogStage);
      dialogStage.showAndWait();

      if (controller.isSuccess()) {
        loadOrdenes();
      }
    } catch (IOException e) {
      e.printStackTrace();
      showAlert("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
    }
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
