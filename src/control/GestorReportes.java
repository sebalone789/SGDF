/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.vista.DocumentosEnviadosAreaController;
import control.vista.PantallaPrincipalController;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 *
 * @author usuario
 */
public class GestorReportes {

    //Nombre Reporte
    private String nombreReporte;

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public void generarReporte(Date date1, Date date2, Integer cat, String tipoReporte, Stage stage) throws FileNotFoundException {
        try {
            InputStream input = this.getClass().getResourceAsStream("/assets/reports/" + nombreReporte);
            
            // First, compile jrxml file.
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            // Parameters for report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("categoria", cat);
            parameters.put("fechaInicio", date1);
            parameters.put("fechaFin", date2);
            parameters.put("tipoReporte", tipoReporte);
            parameters.put("imagenFinanzas", "assets/finanzasMini.png");
            parameters.put("imagenAndina", "assets/andinaMini.png");

            jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

            try (Connection conn = ConnectionUtils.getConnection()) {
                JasperPrint print = JasperFillManager.fillReport(jasperReport,
                        parameters, conn);

                // Make sure the output directory exists.
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Guardar en");
                File selectedDirectory = chooser.showDialog(stage);
                String dir = selectedDirectory.getAbsolutePath();

                // PDF Exporter.
                JRPdfExporter exporter = new JRPdfExporter();

                ExporterInput exporterInput = new SimpleExporterInput(print);
                // ExporterInput
                exporter.setExporterInput(exporterInput);

                // ExporterOutput
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String reporte;
                switch (nombreReporte) {
                    case "DocumentosArea.jrxml":
                        reporte = dir + "/REPORTE " + tipoReporte + " POR AREA " + df.format(date1) + " AL " + df.format(date2) + " CREADO " + df.format(new Date()) + ".pdf";
                        break;
                    case "DocumentosIndividuo.jrxml":
                        reporte = dir + "/REPORTE " + tipoReporte + " POR INDIVIDUO " + df.format(date1) + " AL " + df.format(date2) + " CREADO " + df.format(new Date()) + ".pdf";
                        break;
                    case "DocumentosTodos.jrxml":
                        reporte = dir + "/REPORTE TODOS " + tipoReporte + " " + df.format(date1) + " AL " + df.format(date2) + " CREADO " + df.format(new Date()) + ".pdf";
                        break;
                    default:
                        reporte = dir + "/REPORTE " + tipoReporte + " " + df.format(date1) + " AL " + df.format(date2) + " CREADO " + df.format(new Date()) + ".pdf";
                        break;
                }
                OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                        reporte);
                // Output
                exporter.setExporterOutput(exporterOutput);

                //
                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                //Mostrar dialogo de confirmacion 
                Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
                alertConf.setTitle("Crear Reporte");
                alertConf.setContentText("Reporte Creado");
                alertConf.initModality(Modality.APPLICATION_MODAL);
                alertConf.showAndWait();

                if (!Desktop.isDesktopSupported()) {
                    return;
                }
                File fileAbrir = new File(reporte);
                Desktop desktop = Desktop.getDesktop();
                if (fileAbrir.exists()) {
                    try {
                        desktop.open(fileAbrir);
                    } catch (IOException ex) {
                        Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                conn.close();
            }
        } catch (JRException ex) {
            //Mostrar dialogo de error
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("Crear Reporte");
            alertConf.setContentText("Reporte Fallido. No se Encuentra el Archivo " + getClass().getResource("/assets/reports/" + nombreReporte));
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
            Logger.getLogger(DocumentosEnviadosAreaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException | ClassNotFoundException ex) {
            //Mostrar dialogo de error
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("Crear Reporte");
            alertConf.setContentText("Reporte Fallido. No se Encuentra el Archivo " + getClass().getResource("/assets/reports/" + nombreReporte));
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
            Logger.getLogger(GestorReportes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
