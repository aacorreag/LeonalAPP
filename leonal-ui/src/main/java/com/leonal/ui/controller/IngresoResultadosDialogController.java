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

import java.util.*;

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
    camposMap.clear();

    for (OrdenDetalleDto detalle : orden.getDetalles()) {

      VBox card = new VBox(6);
      card.getStyleClass().add("card-panel-small");

      // ===== NOMBRE DEL EXAMEN =====
      Label lblExamen = new Label(detalle.getExamenNombre());
      lblExamen.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

      // ===== RANGO DE REFERENCIA (AÚN NO DISPONIBLE) =====
      String referencia = detalle.getValoresReferencia();

      Label lblRango;
      if (referencia != null && !referencia.isBlank()) {
        lblRango = new Label("Referencia: " + referencia);
      } else {
        lblRango = new Label("Referencia: *No definida");
      }

      lblRango.getStyleClass().add("range-label");

      // ===== GRID DE CAMPOS =====
      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(6);

      TextField txtValor = new TextField();
      txtValor.setPromptText("Resultado");
      txtValor.setPrefWidth(160);
      txtValor.setText(detalle.getValor() != null ? detalle.getValor() : "");

      CheckBox chkFueraRango = new CheckBox("Resultado fuera de rango");
      chkFueraRango.setSelected(detalle.isEsPatologico());

      TextField txtObs = new TextField();
      txtObs.setPromptText("Observaciones (si aplica)");
      txtObs.setPrefWidth(320);
      txtObs.setText(
              detalle.getObservacionReporte() != null
                      ? detalle.getObservacionReporte()
                      : ""
      );

      grid.add(new Label("Resultado:"), 0, 0);
      grid.add(txtValor, 1, 0);
      grid.add(chkFueraRango, 2, 0);
      grid.add(txtObs, 1, 1, 2, 1);

      card.getChildren().addAll(lblExamen, lblRango, grid);
      vboxExamenes.getChildren().add(card);

      camposMap.put(
              detalle.getId(),
              new CamposResultado(txtValor, txtObs, chkFueraRango)
      );
    }
  }

  @FXML
  public void guardar() {
    List<ItemResultadoRequest> resultados = new ArrayList<>();

    for (Map.Entry<UUID, CamposResultado> entry : camposMap.entrySet()) {
      CamposResultado campos = entry.getValue();

      resultados.add(
              ItemResultadoRequest.builder()
                      .ordenDetalleId(entry.getKey())
                      .valor(campos.txtValor.getText())
                      .observacionReporte(campos.txtObs.getText())
                      .esPatologico(campos.chkFueraRango.isSelected())
                      .build()
      );
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

  private record CamposResultado(
          TextField txtValor,
          TextField txtObs,
          CheckBox chkFueraRango
  ) {}
}
