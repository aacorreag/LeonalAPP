package com.leonal.infrastructure.report;

import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.ReportRepositoryPort;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JasperReportAdapter implements ReportRepositoryPort {

  private static final String ORDEN_REPORT_PATH = "/reports/orden_comprobante.jrxml";

  @Override
  public byte[] generateOrdenReport(Orden orden) {
    try {
      InputStream reportStream = getClass().getResourceAsStream(ORDEN_REPORT_PATH);
      if (reportStream == null) {
        throw new RuntimeException("No se encontró la plantilla del reporte: " + ORDEN_REPORT_PATH);
      }

      JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

      Map<String, Object> parameters = new HashMap<>();

      // Cargar Logo
      InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
      if (logoStream != null) {
        parameters.put("LOGO_PATH", logoStream);
      }

      parameters.put("CODIGO_ORDEN", orden.getCodigoOrden());
      parameters.put("PACIENTE_NOMBRE", orden.getPaciente() != null ? orden.getPaciente().getNombre() : "N/A");
      parameters.put("PACIENTE_DOC", orden.getPaciente() != null ? orden.getPaciente().getNumeroDocumento() : "N/A");
      parameters.put("FECHA", orden.getFechaRecepcion().toString());
      parameters.put("TOTAL", orden.getTotal());

      // Si los detalles tienen objetos complejos, Jasper los manejará si los campos
      // coinciden
      JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orden.getDetalles());

      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    } catch (JRException e) {
      throw new RuntimeException("Error al generar el reporte Jasper: " + e.getMessage(), e);
    }
  }

  @Override
  public byte[] generateResultadosReport(Orden orden) {
    try {
      InputStream reportStream = getClass().getResourceAsStream("/reports/resultados_laboratorio.jrxml");
      if (reportStream == null) {
        throw new RuntimeException("No se encontró la plantilla del reporte: /reports/resultados_laboratorio.jrxml");
      }

      JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

      Map<String, Object> parameters = new HashMap<>();

      // Cargar Logo
      InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
      if (logoStream != null) {
        parameters.put("LOGO_PATH", logoStream);
      }

      com.leonal.domain.model.Paciente paciente = orden.getPaciente();
      parameters.put("PACIENTE_NOMBRE", paciente != null ? paciente.getNombre() : "N/A");
      parameters.put("PACIENTE_DOC", paciente != null ? paciente.getNumeroDocumento() : "N/A");
      parameters.put("PACIENTE_SEXO", paciente != null ? paciente.getSexo() : "N/A");
      parameters.put("PACIENTE_EDAD", paciente != null ? paciente.getEdad() + " años" : "N/A");

      parameters.put("CODIGO_ORDEN", orden.getCodigoOrden());
      parameters.put("FECHA_REPORT",
          java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
      parameters.put("MEDICO", "S/M");

      JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orden.getDetalles());

      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    } catch (JRException e) {
      throw new RuntimeException("Error al generar el reporte de resultados: " + e.getMessage(), e);
    }
  }
}
