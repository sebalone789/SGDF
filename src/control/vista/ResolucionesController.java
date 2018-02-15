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
import beans.ObservacionJpaController;
import beans.exceptions.NonexistentEntityException;
import control.GestorDeDocumentos;
import control.GestorReportes;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Categoria;
import modelo.Documento;
import modelo.Individuo;
import modelo.Observacion;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class ResolucionesController implements Initializable {

    @FXML
    private TableView<Documento> tvUltimosDocumentos;
    @FXML
    private TableColumn<Documento, String> tcItem;
    @FXML
    private TableColumn<Documento, String> tcTituloDocumento;
    @FXML
    private TableColumn<Documento, String> tcFechaEmision;
    @FXML
    private TableColumn<Documento, String> tcFechaRecepcion;
    @FXML
    private TableColumn<Documento, String> tcEnviadoPor;
    @FXML
    private TableColumn<Documento, String> tcVer;
    @FXML
    private TableColumn<Documento, Integer> tcNumero;
    @FXML
    private TableColumn<Documento, String> tcEliminar;
    @FXML
    private TableColumn<Documento, String> tcObservacion;
    @FXML
    private TextField txtBscNumeroDocumento;
    @FXML
    private TextField txtBscAsuntoDocumento;
    @FXML
    private TextField txtBscFechaEmision;
    @FXML
    private TextField txtBscFechaRecpecion;
    @FXML
    private TextField txtBscEnviadoPor;
    @FXML
    private Label indicador;
    @FXML
    private Label lblBuscar;
    @FXML
    private DatePicker dtpFechaInicio;
    @FXML
    private DatePicker dtpFechaFin;
    @FXML
    private Button btnReporte;
    @FXML
    private Label lblReporte;
    @FXML
    private ProgressIndicator progress;

    /**
     * Initializes the controller class.
     */
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
    final ObservacionJpaController ejbObservacion = new ObservacionJpaController(emf);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Llenar los valores de la tabla
        List<Documento> documentos = new ArrayList<>();

        //Encontrar la categoria
        em = ejbCategoria.getEntityManager();
        consulta = em.createNamedQuery("Categoria.findByDescripcion", Categoria.class).setParameter("descripcion", "Resoluciones");
        if (!consulta.getResultList().isEmpty()) {
            Categoria cat = (Categoria) consulta.getSingleResult();
            em = ejbDocumento.getEntityManager();
            consulta = em.createNamedQuery("Documento.findByCategoria", Documento.class).setParameter("categoria", cat);
            documentos = consulta.getResultList();
        }
        ObservableList<Documento> elementos = FXCollections.observableArrayList(documentos);
        tvUltimosDocumentos.setItems(elementos);

        //Asignar los valores a cada columna
        tcNumero.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(tvUltimosDocumentos.getItems().indexOf(param.getValue()) + 1);
        });

        tcItem.setCellValueFactory((TableColumn.CellDataFeatures<Documento, String> param) -> {
            Documento doc = param.getValue();
            return new ReadOnlyObjectWrapper<>(doc.getNroDocumento());
        });

        tcTituloDocumento.setCellValueFactory((TableColumn.CellDataFeatures<Documento, String> param) -> {
            Documento doc = param.getValue();
            return new ReadOnlyObjectWrapper<>(doc.getTituloDocumento());
        });

        tcFechaEmision.setCellValueFactory((param) -> {
            Documento doc = param.getValue();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String valor = df.format(doc.getFechaEmision());
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tcFechaRecepcion.setCellValueFactory((param) -> {
            Documento doc = param.getValue();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String valor = df.format(doc.getFechaRecepcion());
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tcEnviadoPor.setCellValueFactory((param) -> {
            Documento doc = param.getValue();
            String valor;
            if (doc.getIndividuo() != null) {
                Individuo ind = doc.getIndividuo();
                valor = ind.getApPaterno() + " " + ind.getApMaterno() + " " + ind.getNombreIndividuo();
            } else {
                valor = doc.getArea().getDescripcion();
            }
            return new ReadOnlyObjectWrapper<>(valor);
        });

        //CellFactory para multilineas
        Callback<TableColumn<Documento, String>, TableCell<Documento, String>> cellFactoryMultiLineas
                = (final TableColumn<Documento, String> param) -> {
                    final TableCell<Documento, String> cell = new TableCell<Documento, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Text text = new Text();
                        setGraphic(text);
                        setPrefHeight(Control.USE_COMPUTED_SIZE);
                        text.wrappingWidthProperty().bind(param.widthProperty());
                        text.textProperty().bind(itemProperty());
                    }
                }

            };
                    return cell;
                };

        tcTituloDocumento.setCellFactory(cellFactoryMultiLineas);
        tcItem.setCellFactory(cellFactoryMultiLineas);
        tcEnviadoPor.setCellFactory(cellFactoryMultiLineas);

        //Cell Factory para los botones
        Callback<TableColumn<Documento, String>, TableCell<Documento, String>> cellFactory
                = //
                (final TableColumn<Documento, String> param) -> {
                    final TableCell<Documento, String> cell = new TableCell<Documento, String>() {

                Button btn = new Button("   ");
                BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/search-icon.png").toExternalForm(), 50, 50, true, true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                Background background = new Background(backgroundImage);

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Documento doc = getTableView().getItems().get(getIndex());
                        if (doc.getUrlDocumento() != null) {
                            btn.setBackground(background);
                            btn.setOnAction(event -> {
                                //first check if Desktop is supported by Platform or not
                                if (!Desktop.isDesktopSupported()) {
                                    System.out.println("Desktop is not supported");
                                    return;
                                }
                                File file = new File(doc.getUrlDocumento());
                                Desktop desktop = Desktop.getDesktop();
                                if (file.exists()) {
                                    try {
                                        desktop.open(file);
                                    } catch (IOException ex) {
                                        Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    //Mostrar dialogo de advertencia
                                    Alert alertConf = new Alert(Alert.AlertType.ERROR);
                                    alertConf.setTitle("Abrir Archivo");
                                    alertConf.setContentText("No se encontro archivo " + doc.getUrlDocumento());
                                    alertConf.initModality(Modality.APPLICATION_MODAL);
                                    alertConf.showAndWait();
                                    doc.setUrlDocumento(null);
                                    try {
                                        ejbDocumento.edit(doc);
                                    } catch (Exception ex) {
                                        Logger.getLogger(DocumentosEnviadosAreaController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            btn.setVisible(true);
                        } else {
                            btn.setVisible(false);
                        }
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    cell.setStyle("-fx-alignment: CENTER;");
                    return cell;
                };

        tcVer.setCellFactory(cellFactory);

        //Asignar a la columna Eliminar
        Callback<TableColumn<Documento, String>, TableCell<Documento, String>> cellFactoryDelete
                = //
                (final TableColumn<Documento, String> param) -> {
                    final TableCell<Documento, String> cell = new TableCell<Documento, String>() {

                Button btn = new Button("   ");
                BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/delete-icon.png").toExternalForm(), 25, 25, true, true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                Background background = new Background(backgroundImage);

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Documento doc = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Eliminar Registro");
                            alert.setHeaderText("Se Eliminara el Registro");
                            alert.setContentText("¿Esta Seguro?");
                            alert.initModality(Modality.APPLICATION_MODAL);
                            ButtonType buttonTypeOne = new ButtonType("Si");
                            ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == buttonTypeOne) {
                                if (doc.getUrlDocumento() != null) {
                                    //Eliminar el archivo
                                    Path path = Paths.get(doc.getUrlDocumento());
                                    try {
                                        Files.delete(path);
                                    } catch (NoSuchFileException x) {
                                        System.err.format("%s: no such" + " file or directory%n", path);
                                    } catch (DirectoryNotEmptyException x) {
                                        System.err.format("%s not empty%n", path);
                                    } catch (IOException x) {
                                        // File permission problems are caught here.
                                        System.err.println(x);
                                    }
                                }
                                try {
                                    //Eliminar las observaciones de cada documento
                                    em = ejbObservacion.getEntityManager();
                                    consulta = em.createNamedQuery("Observacion.findByDocumento", Observacion.class).setParameter("documento", doc);
                                    if (!consulta.getResultList().isEmpty()) {
                                        List<Observacion> obs = consulta.getResultList();
                                        obs.forEach((o) -> {
                                            try {
                                                ejbObservacion.destroy(o.getIdObservacion());
                                            } catch (NonexistentEntityException ex) {
                                                Logger.getLogger(DocumentosEnviadosController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        });
                                    }
                                    //Destruir Documento
                                    ejbDocumento.destroy(doc.getIdDocumento());

                                    // Llenar los valores de la tabla
                                    List<Documento> documentos = new ArrayList<>();

                                    //Encontrar la categoria
                                    em = ejbCategoria.getEntityManager();
                                    consulta = em.createNamedQuery("Categoria.findByDescripcion", Categoria.class).setParameter("descripcion", "Resoluciones");
                                    if (!consulta.getResultList().isEmpty()) {
                                        Categoria cat = (Categoria) consulta.getSingleResult();
                                        em = ejbDocumento.getEntityManager();
                                        consulta = em.createNamedQuery("Documento.findByCategoria", Documento.class).setParameter("categoria", cat);
                                        documentos = consulta.getResultList();
                                        deshabFocus();
                                    }
                                    ObservableList<Documento> elementos = FXCollections.observableArrayList(documentos);
                                    tvUltimosDocumentos.setItems(elementos);

                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(DocumentosEnviadosController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                            }

                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    cell.setStyle(
                            "-fx-alignment: CENTER;");
                    return cell;
                };

        //Asignar la cell factory a la columna eliminar
        tcEliminar.setCellFactory(cellFactoryDelete);

        //Asignar a la columna Observaciones
        Callback<TableColumn<Documento, String>, TableCell<Documento, String>> cellFactoryObs
                = //
                (final TableColumn<Documento, String> param) -> {
                    final TableCell<Documento, String> cell = new TableCell<Documento, String>() {

                Button btn = new Button("   ");
                BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/obs-icon.png").toExternalForm(), 25, 25, true, true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                Background background = new Background(backgroundImage);

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Documento doc = getTableView().getItems().get(getIndex());

                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            try {
                                //Crear el dialogo
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoMostrarObservaciones.fxml"));
                                VBox page = (VBox) loader.load();
                                // Create the dialog Stage.
                                Stage dialogStage = new Stage();
                                dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
                                dialogStage.setTitle("Añadir Observacion");
                                dialogStage.initModality(Modality.WINDOW_MODAL);
                                dialogStage.initOwner((Stage) btn.getScene().getWindow());
                                Scene scene = new Scene(page);
                                dialogStage.setScene(scene);
                                //Enviar el documento al dialogo
                                DialogoMostrarObservacionesController controller = loader.getController();
                                controller.setDoc(doc);
                                dialogStage.showAndWait();
                            } catch (IOException ex) {
                                Logger.getLogger(DocumentosEnviadosController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });

                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    cell.setStyle(
                            "-fx-alignment: CENTER;");
                    return cell;
                };

        tcObservacion.setCellFactory(cellFactoryObs);

        //CellFactory para edicion
        Callback<TableColumn<Documento, String>, TableCell<Documento, String>> cellFactoryEditable
                = new Callback<TableColumn<Documento, String>, TableCell<Documento, String>>() {
            @Override
            public TableCell<Documento, String> call(TableColumn<Documento, String> param) {
                return new EditingCell();
            }
        };

        //Convertir las columnas a editables
        tvUltimosDocumentos.setEditable(true);
        tcTituloDocumento.setEditable(true);
        tcTituloDocumento.setCellFactory(cellFactoryEditable);
        tcTituloDocumento.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Documento, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Documento, String> t) {
                try {
                    Documento doc = ((Documento) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    doc.setTituloDocumento(String.valueOf(t.getNewValue()));
                    ejbDocumento.edit(doc);
                } catch (Exception ex) {
                    Logger.getLogger(DocumentosEnviadosController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        //Habilitar los cuadros de busqueda
        //Crear la lista de filtro
        FilteredList<Documento> fd = new FilteredList(elementos, d -> true);

        //Listener para cada uno de los campos de busqueda
        //Numero de documento
        txtBscNumeroDocumento.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                //Desactivar los otros campos
                txtBscAsuntoDocumento.setDisable(true);
                txtBscEnviadoPor.setDisable(true);
                txtBscFechaEmision.setDisable(true);
                txtBscFechaRecpecion.setDisable(true);
                //Limpiar los otros campos
                txtBscAsuntoDocumento.clear();
                txtBscEnviadoPor.clear();
                txtBscFechaEmision.clear();
                txtBscFechaRecpecion.clear();
            } else {
                //Habilitar todos los campos
                txtBscAsuntoDocumento.setDisable(false);
                txtBscEnviadoPor.setDisable(false);
                txtBscFechaEmision.setDisable(false);
                txtBscFechaRecpecion.setDisable(false);
            }
            fd.setPredicate(d -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return ((Documento) d).getNroDocumento().toLowerCase().contains(lowerCaseFilter);
            });
        });

        //Asunto de documento
        txtBscAsuntoDocumento.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                //Desactivar los otros campos
                txtBscEnviadoPor.setDisable(true);
                txtBscFechaEmision.setDisable(true);
                txtBscFechaRecpecion.setDisable(true);
                txtBscNumeroDocumento.setDisable(true);
                //Limpiar los otros campos
                txtBscEnviadoPor.clear();
                txtBscFechaEmision.clear();
                txtBscFechaRecpecion.clear();
                txtBscNumeroDocumento.clear();
            } else {
                //Habilitar todos los campos
                txtBscEnviadoPor.setDisable(false);
                txtBscFechaEmision.setDisable(false);
                txtBscFechaRecpecion.setDisable(false);
                txtBscNumeroDocumento.setDisable(false);
            }
            fd.setPredicate(d -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return ((Documento) d).getTituloDocumento().toLowerCase().contains(lowerCaseFilter);
            });
        });

        //Enviado por
        txtBscEnviadoPor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                //Desactivar los otros campos
                txtBscAsuntoDocumento.setDisable(true);
                txtBscFechaEmision.setDisable(true);
                txtBscFechaRecpecion.setDisable(true);
                txtBscNumeroDocumento.setDisable(true);
                //Limpiar los otros campos
                txtBscAsuntoDocumento.clear();
                txtBscFechaEmision.clear();
                txtBscFechaRecpecion.clear();
                txtBscNumeroDocumento.clear();
            } else {
                //Habilitar todos los campos
                txtBscAsuntoDocumento.setDisable(false);
                txtBscFechaEmision.setDisable(false);
                txtBscFechaRecpecion.setDisable(false);
                txtBscNumeroDocumento.setDisable(false);
            }
            fd.setPredicate(d -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (((Documento) d).getArea() != null) {
                    if (((Documento) d).getArea().getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                } else {
                    if (((Documento) d).getIndividuo().getNombreIndividuo().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else {
                        if (((Documento) d).getIndividuo().getApPaterno().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else {
                            if (((Documento) d).getIndividuo().getApMaterno().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                        }
                    }

                }

                return false;
            });
        });

        //FechaEmision
        txtBscFechaEmision.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                //Desactivar los otros campos
                txtBscAsuntoDocumento.setDisable(true);
                txtBscEnviadoPor.setDisable(true);
                txtBscFechaRecpecion.setDisable(true);
                txtBscNumeroDocumento.setDisable(true);
                //Limpiar los otros campos
                txtBscAsuntoDocumento.clear();
                txtBscEnviadoPor.clear();
                txtBscFechaRecpecion.clear();
                txtBscNumeroDocumento.clear();
            } else {
                //Habilitar todos los campos
                txtBscAsuntoDocumento.setDisable(false);
                txtBscEnviadoPor.setDisable(false);
                txtBscFechaRecpecion.setDisable(false);
                txtBscNumeroDocumento.setDisable(false);
            }
            fd.setPredicate(d -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String lowerCaseFilter = newValue.toLowerCase();

                return (df.format(((Documento) d).getFechaEmision()).toLowerCase().contains(lowerCaseFilter));
            });
        });

        //Fecha Recepcion
        txtBscFechaRecpecion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                //Desactivar los otros campos
                txtBscAsuntoDocumento.setDisable(true);
                txtBscEnviadoPor.setDisable(true);
                txtBscFechaEmision.setDisable(true);
                txtBscNumeroDocumento.setDisable(true);
                //Limpiar los otros campos
                txtBscAsuntoDocumento.clear();
                txtBscEnviadoPor.clear();
                txtBscFechaEmision.clear();
                txtBscNumeroDocumento.clear();
            } else {
                //Habilitar todos los campos
                txtBscAsuntoDocumento.setDisable(false);
                txtBscEnviadoPor.setDisable(false);
                txtBscFechaEmision.setDisable(false);
                txtBscNumeroDocumento.setDisable(false);
            }
            fd.setPredicate(d -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String lowerCaseFilter = newValue.toLowerCase();

                return (df.format(((Documento) d).getFechaRecepcion()).toLowerCase().contains(lowerCaseFilter));
            });
        });

        SortedList<Documento> sortedData = new SortedList<>(fd);
        sortedData.comparatorProperty().bind(tvUltimosDocumentos.comparatorProperty());
        tvUltimosDocumentos.setItems(sortedData);

        //Actualizar la fecha de recepcion en funcion a la de emision
        dtpFechaInicio.valueProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    dtpFechaFin.setValue(newValue);
                    dtpFechaFin.setDisable(false);
                }));

        //Restringir las fechas en la Recepcion
        dtpFechaFin.setDayCellFactory(
                (param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty
            ) {
                super.updateItem(date, empty);
                if (date.isBefore(dtpFechaInicio.getValue())) {
                    setDisable(true);
                }
            }
        }
        );

        //cargar icono
        cargarIconos("/img/print-icon.png", btnReporte);

        //Deshabilitar el focus
        deshabFocus();
    }

    //Metodo para deshabilitar el focus
    private void deshabFocus() {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        txtBscNumeroDocumento.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                txtBscNumeroDocumento.getParent().requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    @FXML
    private void accionReporte(ActionEvent event) {
        if (dtpFechaInicio.getValue() != null) {
            if (dtpFechaFin.getValue() != null) {
                //Crear el task
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        generarReporte();
                        return null;
                    }
                };

                //Colocar el progreso como indeterminado y que se muestre cuando el task este corriendo
                progress.setProgress(-1);
                progress.visibleProperty().bind(task.runningProperty());

                task.setOnSucceeded((event2) -> {
                    removerEstilo();
                });

                task.setOnFailed((event2) -> {
                    Thread.currentThread().interrupt();//preserve the message
                });

                //Correr el task
                task.run();
            } else {
                removerEstilo();
                ObservableList<String> estilo = dtpFechaFin.getStyleClass();
                if (!estilo.contains("error")) {
                    estilo.add("error");
                }
            }
        } else {
            removerEstilo();
            ObservableList<String> estilo = dtpFechaInicio.getStyleClass();
            if (!estilo.contains("error")) {
                estilo.add("error");
            }
        }
    }

    //Metodo para remover los estilos
    private void removerEstilo() {
        //Obtener los estilos
        ObservableList<String> estilosDtpInicio = dtpFechaInicio.getStylesheets();
        ObservableList<String> estilosDtpFin = dtpFechaFin.getStylesheets();

        List<ObservableList> estilos = new ArrayList<>();
        //Llenar la lista con los estilos
        estilos.add(estilosDtpInicio);
        estilos.add(estilosDtpFin);

        estilos.forEach((e) -> {
            if (e.contains("error")) {
                e.remove("error");
            }
        });
    }

    //Metodo para cargar icono de reportes
    private void cargarIconos(String ruta, Button btn) {
        try {
            Image icono = new Image(PantallaPrincipalController.class.getResourceAsStream(ruta));
            ImageView iconoView = new ImageView(icono);
            iconoView.setFitHeight(20);
            iconoView.setFitWidth(20);
            btn.setGraphic(iconoView);
        } catch (Exception e) {
        }
    }

    private void generarReporte() {
        try {
            //Encontrar la consulta
            em = ejbCategoria.getEntityManager();
            consulta = em.createNamedQuery("Categoria.findByDescripcion", Categoria.class).setParameter("descripcion", "Resoluciones");
            //Almacenar el id de la consulta
            Integer categoria = 3;
            if (!consulta.getResultList().isEmpty()) {
                categoria = ((Categoria) consulta.getSingleResult()).getIdCategoria();
            }
            GestorReportes gr = new GestorReportes();
            gr.setNombreReporte("DocumentosTodos.jrxml");
            LocalDate localDate = dtpFechaInicio.getValue();
            Date fecha1 = Date.valueOf(localDate);
            LocalDate localDate2 = dtpFechaFin.getValue();
            Date fecha2 = Date.valueOf(localDate2);
            gr.generarReporte(fecha1, fecha2, categoria, "RESOLUCIONES", (Stage) btnReporte.getScene().getWindow());
            removerEstilo();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosEnviadosAreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Clase para editar las celdas
    private class EditingCell extends TableCell<Documento, String> {

        private TextArea textArea;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textArea);
                textArea.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());

            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textArea != null) {
                        textArea.setText(getString());
                    }
                    setText(null);
                    setGraphic(textArea);
                } else {
                    Text text = new Text();
                    setGraphic(text);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(widthProperty());
                    text.textProperty().bind(itemProperty());
                }
            }

        }

        private void createTextField() {
            textArea = new TextArea(getString());
            textArea.setWrapText(true);
            textArea.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textArea.setOnKeyPressed((event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textArea.getText());
                }
            });
            textArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                        Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textArea.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
