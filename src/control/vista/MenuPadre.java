/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.vista;

import beans.AreasJpaController;
import beans.CategoriaJpaController;
import beans.DocumentoJpaController;
import beans.IndividuoJpaController;
import control.ConnectionUtils;
import control.GestorDeDocumentos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Documento;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author usuario
 */
public class MenuPadre {

    //Valores Para La Insercion de Datos
    final EntityManagerFactory emf = GestorDeDocumentos.emf;
    Boolean existe = false;
    Query consulta;
    EntityManager em;

    //JpaControllers
    final CategoriaJpaController ejbCategoria = new CategoriaJpaController(emf);
    final DocumentoJpaController ejbDocumento = new DocumentoJpaController(emf);
    final AreasJpaController ejbArea = new AreasJpaController(emf);
    final IndividuoJpaController ejbIndividuo = new IndividuoJpaController(emf);

    @FXML
    protected MenuBar menuIncio;

    @FXML
    protected void accionUltimosDocumentos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/PantallaPrincipal.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/vista/Estilos.css").toExternalForm());
//            muestra la barra menu
            Stage stage = (Stage) this.menuIncio.getScene().getWindow();
            //Pantalla Completa
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    protected void accionRegistrarDocumento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/vista/RegistrarNuevoDocumento.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            scene.setOnKeyPressed((e) -> {
                if (e.getCode() == KeyCode.ENTER) {
                    ((RegistrarNuevoDocumentoController) loader.getController()).accionGuardado();
                }
            });
            //muestra la barra menu
            Stage stage = (Stage) this.menuIncio.getScene().getWindow();
            ((VBox) root).getChildren().add(0, this.menuIncio);
            //Pantalla Completa
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            scene.getStylesheets().add(getClass().getResource("/vista/Estilos.css").toExternalForm());

            //Titulo de la aplicacion
            stage.setTitle("Sistema de Gestion Documentaria de la E.P. de Finanzas");

            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    protected void accionDocEnviadosTodos(ActionEvent event) {
        cambioVentana("/vista/DocumentosEnviados.fxml");
    }

    @FXML
    protected void accionDocEnviadosIndividuo(ActionEvent event) {
        cambioVentana("/vista/DocumentosEnviadosIndividuo.fxml");
    }

    @FXML
    protected void accionDocEnviadosArea(ActionEvent event) {
        cambioVentana("/vista/DocumentosEnviadosArea.fxml");
    }

    @FXML
    protected void accionDocRecibidosTodos(ActionEvent event) {
        cambioVentana("/vista/DocumentosRecibidos.fxml");
    }

    @FXML
    protected void acciobDocRecibidosIndividuo(ActionEvent event) {
        cambioVentana("/vista/DocumentosRecibidosIndividuo.fxml");
    }

    @FXML
    protected void accionDocRecibidosArea(ActionEvent event) {
        cambioVentana("/vista/DocumentosRecibidosArea.fxml");
    }

    @FXML
    protected void accionResolucionesTodos(ActionEvent event) {
        cambioVentana("/vista/Resoluciones.fxml");
    }

    @FXML
    protected void accionInformesTodos(ActionEvent event) {
        cambioVentana("/vista/Informes.fxml");
    }

    @FXML
    protected void accionInformesIndividuo(ActionEvent event) {
        cambioVentana("/vista/InformesIndividuo.fxml");
    }

    @FXML
    protected void accionInformesArea(ActionEvent event) {
        cambioVentana("/vista/InformesArea.fxml");
    }

    @FXML
    protected void accionSilabos(ActionEvent event) {
        cambioVentana("/vista/Silabos.fxml");
    }

    @FXML
    protected void accionConvalidaciones(ActionEvent event) {
        cambioVentana("/vista/Convalidaciones.fxml");
    }

    @FXML
    protected void accionPlanEstudios(ActionEvent event) {
        cambioVentana("/vista/PlanDeEstudios.fxml");
    }

    @FXML
    protected void accionRegistrosIndividuos(ActionEvent event) {
        cambioVentana("/vista/RegistrosIndividuo.fxml");
    }

    @FXML
    protected void accionRegistrosArea(ActionEvent event) {
        cambioVentana("/vista/RegistrosArea.fxml");
    }

    @FXML
    protected void accionAyudaManual(ActionEvent event) {
        cambioVentana("/vista/ManualUsuario.fxml");
    }

    @FXML
    protected void accionAyudaAcercaDe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RegistrosAreaController.class.getResource("/vista/AcercaDe.fxml"));
            VBox page = (VBox) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            //Icono de la aplicacion
            dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
            dialogStage.setTitle("Acerca De");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner((Stage) menuIncio.getScene().getWindow());
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/vista/Estilos.css").toExternalForm());
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    protected void accionDestinatarios(ActionEvent event) {
        cambioVentana("/vista/RegistrosDestinatario.fxml");
    }

    @FXML
    protected void accionCrearRespaldo(ActionEvent event) {
        String dir = obtenerDirectorio((Stage) menuIncio.getScene().getWindow());
        //Formatter para date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String path = dir + "\\backup-" + df.format(new Date());
        //Crear el backup
        backupDB(path);
    }

    @FXML
    protected void accionImportarRespaldo(ActionEvent event) {
        String path = obtenerDirectorio((Stage) menuIncio.getScene().getWindow());
        try {
            //Importar el backup
            restoreDB(path);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    protected void accionExportarDatos(ActionEvent event) {
        String path = obtenerDirectorio((Stage) menuIncio.getScene().getWindow());
        exportData(path);
    }

    @FXML
    protected void accionImportarDatos(ActionEvent event) {
        String path = obtenerDirectorio((Stage) menuIncio.getScene().getWindow());
        importData(path);
    }

    //Metodo para el cambio de ventana
    protected void cambioVentana(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Scene scene = new Scene(root);
            //muestra la barra menu
            Stage stage = (Stage) this.menuIncio.getScene().getWindow();
            ((VBox) root).getChildren().add(0, this.menuIncio);
            //Pantalla Completa
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            scene.getStylesheets().add(getClass().getResource("/vista/Estilos.css").toExternalForm());

            //Titulo de la aplicacion
            stage.setTitle("Sistema de Gestion Documentaria de la E.P. de Finanzas");

            stage.setScene(scene);
        } catch (IOException ex) {
            //Mostrar dialogo de confirmacion 
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("ERROR");
            alertConf.setContentText(ex.getMessage());
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
        }
    }

    //Metodo para crear el respaldo
    public boolean backupDB(String path) {
        try {
            //Cambiar el cursor a espera
            menuIncio.getScene().setCursor(Cursor.WAIT);
            //Crear el backup de la base de datos
            Connection conn = ConnectionUtils.getConnection();
            try (CallableStatement cs = conn.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)")) {
                cs.setString(1, path);
                cs.execute();
                //Copiar los documentos
                List<Documento> documentosRespaldo = ejbDocumento.findDocumentoEntities();
                documentosRespaldo.forEach((d) -> {
                    //Crear el nuevo directorio para guardar el documento dentro del proyecto
                    File directorio = new File(path + "\\" + d.getCategoria().getDescripcion());
                    //Verificar que el directorio no exista
                    if (!directorio.exists()) {
                        Boolean result = false;
                        try {
                            directorio.mkdir();
                            result = true;
                        } catch (SecurityException se) {
                        }
                        if (result) {
                            System.out.println("Directorio Creado");
                        }
                    }

                    //Copiar el documento al directorio creado
                    if (d.getUrlDocumento() != null) {
                        File f = new File(d.getUrlDocumento());
                        //Obtener Extension del Archivo
                        String extension = FilenameUtils.getExtension(f.getAbsolutePath());
                        String denominacion = d.getNroDocumento() + "." + extension;
                        if (f.exists()) {
                            try {
                                copiarDocumento(path + "\\" + d.getCategoria().getDescripcion(), f.getAbsolutePath(), denominacion);
                            } catch (IOException e) {
                            }
                        } else {
                            //Mostrar dialogo de confirmacion 
                            Alert alertConf = new Alert(Alert.AlertType.ERROR);
                            alertConf.setTitle("Respaldo Fallido");
                            alertConf.setContentText("No existe documento");
                            alertConf.initModality(Modality.APPLICATION_MODAL);
                            alertConf.showAndWait();
                        }
                    }
                });
                //Cambiar el cursor a default
                menuIncio.getScene().setCursor(Cursor.DEFAULT);
                //Mostrar dialogo de confirmacion 
                Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
                alertConf.setTitle("Crear Respaldo");
                alertConf.setContentText("Respaldo Creado");
                alertConf.initModality(Modality.APPLICATION_MODAL);
                alertConf.showAndWait();
                return true;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            //Cambiar el cursor a default
            menuIncio.getScene().setCursor(Cursor.DEFAULT);
            //Mostrar dialogo de error
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("Crear Respaldo");
            alertConf.setContentText("Respaldo Fallido");
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //Metodo para importar el respaldo
    public boolean restoreDB(String source) throws ClassNotFoundException, SQLException {
        //Cambiar el cursor a espera
        menuIncio.getScene().setCursor(Cursor.WAIT);
        try {
            String nsURL1 = "jdbc:derby:gestiondocumentaria;shutdown=true";
            Properties connectionProps = new Properties();
            connectionProps.setProperty("user", "sgdf");
            connectionProps.setProperty("password", "Finanz62");
            Connection conn = DriverManager.getConnection(nsURL1, connectionProps);
            return true;
        } catch (SQLException ex) {
            String nsURL2 = "jdbc:derby:gestiondocumentaria;restoreFrom=" + source + "\\gestiondocumentaria";
            Properties connectionProps = new Properties();
            connectionProps.setProperty("user", "sgdf");
            connectionProps.setProperty("password", "Finanz62");
            Connection connection = DriverManager.getConnection(nsURL2, connectionProps);
            //Volver a conectar a la base de datos
            String nsURL3 = "jdbc:derby:gestiondocumentaria;create=true";
            connection = DriverManager.getConnection(nsURL3, connectionProps);
            try {
                //Copiar los documentos
                recuperarDocumento(source);
            } catch (IOException ex1) {
                //Cambiar el cursor a default
                menuIncio.getScene().setCursor(Cursor.DEFAULT);
                //Mostrar dialogo de confirmacion 
                Alert alertConf = new Alert(Alert.AlertType.ERROR);
                alertConf.setTitle("Error en la Importacion");
                alertConf.setContentText("Fallo el Importe. Seleecione una carpeta valida");
                alertConf.initModality(Modality.APPLICATION_MODAL);
                alertConf.showAndWait();
                Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex1);
            }
            //Mostrar dialogo de confirmacion 
            Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
            alertConf.setTitle("Importar Respaldo");
            alertConf.setContentText("Respaldo Importado");
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
        }
        //Cambiar el cursor a default
        menuIncio.getScene().setCursor(Cursor.DEFAULT);
        return false;
    }

    //Metodo para obtener el directorio
    private String obtenerDirectorio(Stage stage) {
        // Make sure the output directory exists.
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Guardar en");
        File selectedDirectory = chooser.showDialog(stage);
        String dir = selectedDirectory.getAbsolutePath();
        return dir;
    }

    //Metodo para exportar los datos de la bd
    private boolean exportData(String path) {
        //Cambiar el cursor a espera
        menuIncio.getScene().setCursor(Cursor.WAIT);
        //Crear el directorio para la data exportada
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        File directorio = new File(path + "\\Datos SGDF " + df.format(new Date()));
        //Verificar que el directorio no exista
        if (!directorio.exists()) {
            Boolean result = false;
            try {
                directorio.mkdir();
                result = true;
            } catch (SecurityException se) {
            }
            if (result) {
                System.out.println("Directorio Creado");
            }
        } else {
            if (directorio.isDirectory()) {
                String[] entries = directorio.list();
                for (String s : entries) {
                    File currentFile = new File(directorio.getPath(), s);
                    currentFile.delete();
                }
                directorio.delete();
                Boolean result = false;
                try {
                    directorio.mkdir();
                    result = true;
                } catch (SecurityException se) {
                }
                if (result) {
                    System.out.println("Directorio Creado");
                }
            }
        }
        //Copiar los documentos
        List<Documento> documentosRespaldo = ejbDocumento.findDocumentoEntities();
        documentosRespaldo.forEach((d) -> {
            File dir = new File(directorio.getAbsolutePath() + "\\" + d.getCategoria().getDescripcion());
            //Crear el nuevo directorio para guardar el documento dentro del proyecto
            //Verificar que el directorio no exista
            if (!dir.exists()) {
                Boolean result = false;
                try {
                    dir.mkdir();
                    result = true;
                } catch (SecurityException se) {
                }
                if (result) {
                    System.out.println("Directorio Creado");
                }
            }

            //Copiar el documento al directorio creado
            if (d.getUrlDocumento() != null) {
                File f = new File(d.getUrlDocumento());
                //Obtener Extension del Archivo
                String extension = FilenameUtils.getExtension(f.getAbsolutePath());
                String denominacion = d.getNroDocumento() + "." + extension;
                if (f.exists()) {
                    try {
                        copiarDocumento(directorio.getAbsolutePath() + "\\" + d.getCategoria().getDescripcion(), f.getAbsolutePath(), denominacion);
                    } catch (IOException e) {
                        //Mostrar dialogo de confirmacion 
                        Alert alertConf = new Alert(Alert.AlertType.ERROR);
                        alertConf.setTitle("Exportar Datos");
                        alertConf.setContentText("Fallo al exportar los datos al directorio " + directorio.getAbsolutePath());
                        alertConf.initModality(Modality.APPLICATION_MODAL);
                        alertConf.showAndWait();
                    }
                } else {
                    //Mostrar dialogo de confirmacion 
                    Alert alertConf = new Alert(Alert.AlertType.ERROR);
                    alertConf.setTitle("Exportar Datos");
                    alertConf.setContentText("No se encuentra el documento");
                    alertConf.initModality(Modality.APPLICATION_MODAL);
                    alertConf.showAndWait();
                }
            }
        });
        try {
            Connection conn = ConnectionUtils.getConnection();
            //Hacer los exports de la data
            PreparedStatement ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "AREAS");
            ps.setString(3, directorio.getAbsolutePath() + "\\areas.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "DESTINATARIOS");
            ps.setString(3, directorio.getAbsolutePath() + "\\destinatarios.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "CATEGORIA");
            ps.setString(3, directorio.getAbsolutePath() + "\\categoria.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "DOCUMENTO");
            ps.setString(3, directorio.getAbsolutePath() + "\\documento.dat" + "'");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "INDIVIDUO");
            ps.setString(3, directorio.getAbsolutePath() + "\\individuo.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "OBSERVACION");
            ps.setString(3, directorio.getAbsolutePath() + "\\observacion.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.execute();

            //Cambiar el cursor a default
            menuIncio.getScene().setCursor(Cursor.DEFAULT);
            //Mostrar dialogo de confirmacion 
            Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
            alertConf.setTitle("Exportar Datos");
            alertConf.setContentText("Datos Exportados al directorio " + directorio.getAbsolutePath());
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();

            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            //Cambiar el cursor a default
            menuIncio.getScene().setCursor(Cursor.DEFAULT);
            //Mostrar dialogo de confirmacion 
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("Exportar Datos");
            alertConf.setContentText("Fallo al exportar los datos al directorio " + directorio.getAbsolutePath());
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Mostrar dialogo de confirmacion
        return false;
    }

    //Metodo para importar un archivo
    private boolean importData(String path) {
        //Cambiar el cursor a espera
        menuIncio.getScene().setCursor(Cursor.WAIT);
        try {
            //Copiar los documentos
            recuperarDocumento(path);
        } catch (IOException ex1) {
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex1);
        }
        try {
            Connection conn = ConnectionUtils.getConnection();
            //Hacer los exports de la data
            PreparedStatement ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "AREAS");
            ps.setString(3, path + "\\areas.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.setInt(7, 1);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "DESTINATARIOS");
            ps.setString(3, path + "\\destinatarios.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.setInt(7, 1);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "CATEGORIA");
            ps.setString(3, path + "\\categoria.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.setInt(7, 1);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "DOCUMENTO");
            ps.setString(3, path + "\\documento.dat" + "'");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.setInt(7, 1);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "INDIVIDUO");
            ps.setString(3, path + "\\individuo.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.setInt(7, 1);
            ps.execute();

            ps = conn.prepareStatement("CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
            ps.setString(1, null);
            ps.setString(2, "OBSERVACION");
            ps.setString(3, path + "\\observacion.dat");
            ps.setString(4, ";");
            ps.setString(5, "%");
            ps.setString(6, null);
            ps.setInt(7, 1);
            ps.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            //Cambiar el cursor a default
            menuIncio.getScene().setCursor(Cursor.DEFAULT);
            //Mostrar dialogo de error 
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("Importar Datos");
            alertConf.setContentText("Fallo al importar datos del directorio " + path);
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex);
            return true;

        }
        //Cambiar el cursor a default
        menuIncio.getScene().setCursor(Cursor.DEFAULT);
        //Mostrar dialogo de confirmacion 
        Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
        alertConf.setTitle("Importar Datos");
        alertConf.setContentText("Datos Importados del directorio " + path);
        alertConf.initModality(Modality.APPLICATION_MODAL);
        alertConf.showAndWait();
        return true;
    }

    //Metodo para copiar el documento para el respaldo
    private void copiarDocumento(String directorioDestino, String fuente, String denominacion) throws IOException {
        File documentoParaSubir = new File(fuente);
        File documentoSubido = new File(directorioDestino + "\\" + denominacion);
        FileChannel canalOrigen = null, canalDestino = null;
        try {
            canalOrigen = new FileInputStream(documentoParaSubir).getChannel();
            canalDestino = new FileOutputStream(documentoSubido).getChannel();
            canalDestino.transferFrom(canalOrigen, 0, canalOrigen.size());
        } finally {
            canalOrigen.close();
            canalDestino.close();
        }
    }

    //Metodo para recuperar el documento en el recovery del respaldo
    private void recuperarDocumento(String path) throws IOException {
        //Obtener los documentos del path
        File directorioOrigen = new File(path);
        File[] listaArchivos = directorioOrigen.listFiles();
        for (File file : listaArchivos) {
            if (file.isDirectory()) {
                //Crear el directorio si es q no existe
                if (!"gestiondocumentaria".equals(file.getName())) {
                    //Crear el nuevo directorio para guardar el documento dentro del proyecto
                    File directorio = new File(file.getName());
                    //Verificar que el directorio no exista
                    if (!directorio.exists()) {
                        Boolean result = false;
                        try {
                            directorio.mkdir();
                            result = true;
                        } catch (SecurityException se) {
                        }
                        if (result) {
                            System.out.println("Directorio Creado");
                        }
                    }
                    //Copiar todos los documentos dentro del directorio a la carpeta
                    File[] listaDocumentos = file.listFiles();
                    for (File doc : listaDocumentos) {
                        File documentoParaSubir = new File(doc.getAbsolutePath());
                        File documentoSubido = new File(file.getName() + "\\" + doc.getName());
                        FileChannel canalOrigen = null, canalDestino = null;
                        try {
                            canalOrigen = new FileInputStream(documentoParaSubir).getChannel();
                            canalDestino = new FileOutputStream(documentoSubido).getChannel();
                            canalDestino.transferFrom(canalOrigen, 0, canalOrigen.size());
                        } finally {
                            canalOrigen.close();
                            canalDestino.close();
                        }
                    }
                }

            }
        }
    }
}
