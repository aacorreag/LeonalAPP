package com.leonal.ui.controller;

import com.leonal.application.dto.orden.OrdenDetalleDto;
import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.application.dto.resultado.ActualizarResultadosRequest;
import com.leonal.application.dto.resultado.ItemResultadoRequest;
import com.leonal.application.usecase.resultado.IngresarResultadosUseCase;
import com.leonal.ui.context.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class IngresoResultadosDialogController {

  private final IngresarResultadosUseCase ingresarResultadosUseCase;
  private final UserSession userSession;

  @FXML
  private Label lblTitulo;
  @FXML
  private VBox vboxExamenes;

  private OrdenDto orden;
  private final Map<UUID, CamposResultado> camposMap = new HashMap<>();

  public void setOrden(OrdenDto orden) {
    this.orden = orden;
    lblTitulo.setText("Resultados de Orden: " + orden.getCodigoOrden());
    generarCampos();
  }

  private void generarCampos() {
    vboxExamenes.getChildren().clear();
    for (OrdenDetalleDto detalle : orden.getDetalles()) {
      VBox card = new VBox(5);
      card.getStyleClass().add("card-panel-small");

      Label lblExamen = new Label(detalle.getExamenNombre());
      lblExamen.setStyle("-fx-font-weight: bold;");

      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(5);

      TextField txtValor = new TextField();
      txtValor.setPromptText("Valor / Resultado");
      txtValor.setPrefWidth(200);
      txtValor.setText(detalle.getValor() != null ? detalle.getValor() : "");

      TextField txtObs = new TextField();
      txtObs.setPromptText("Observaciones para el reporte");
      txtObs.setPrefWidth(300);
      txtObs.setText(detalle.getObservacionReporte() != null ? detalle.getObservacionReporte() : "");

      CheckBox chkPatologico = new CheckBox("Patológico");
      chkPatologico.setSelected(detalle.isEsPatologico());

      grid.add(new Label("Valor:"), 0, 0);
      grid.add(txtValor, 1, 0);
      grid.add(chkPatologico, 2, 0);
      grid.add(new Label("Obs:"), 0, 1);
      grid.add(txtObs, 1, 1, 2, 1);

      card.getChildren().addAll(lblExamen, grid);
      vboxExamenes.getChildren().add(card);

      camposMap.put(detalle.getId(), new CamposResultado(txtValor, txtObs, chkPatologico));
    }
  }

  @FXML
  public void guardar() {
    List<ItemResultadoRequest> resultados = new ArrayList<>();

    for (Map.Entry<UUID, CamposResultado> entry : camposMap.entrySet()) {
      CamposResultado campos = entry.getValue();
      resultados.add(ItemResultadoRequest.builder()
          .ordenDetalleId(entry.getKey())
          .valor(campos.txtValor.getText())
          .observacionReporte(campos.txtObs.getText())
          .esPatologico(campos.chkPatologico.isSelected())
          .build());
    }

    ActualizarResultadosRequest request = ActualizarResultadosRequest.builder()
        .ordenId(orden.getId())
        .resultados(resultados)
        .build();

    try {
      ingresarResultadosUseCase.execute(request, userSession.getCurrentUser().getId());
      mostrarAlerta("Éxito", "Resultados guardados correctamente");
      cerrar();
    } catch (Exception e) {
      mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
    }
  }

  @FXML
  public void cancelar() {
    cerrar();
  }

  private void cerrar() {
    Stage stage = (Stage) vboxExamenes.getScene().getWindow();
    stage.close();
  }

  private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
  }

  private record CamposResultado(TextField txtValor, TextField txtObs, CheckBox chkPatologico) {
  }
}
