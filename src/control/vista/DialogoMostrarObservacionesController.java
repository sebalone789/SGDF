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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Documento;
import modelo.Observacion;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class DialogoMostrarObservacionesController implements Initializable {

    @FXML
    private Button btnAnadir;
    @FXML
    private TableView<Observacion> tvObservaciones;
    @FXML
    private TableColumn<Observacion, Integer> tcItem;
    @FXML
    private TableColumn<Observacion, String> tcInformacion;
    @FXML
    private TableColumn<Observacion, String> tcSalida;
    @FXML
    private TableColumn<Observacion, String> tcEditar;
    @FXML
    private TableColumn<Observacion, String> tcEliminar;
    @FXML
    private Label lblObservacionesDoc;

    //Valores Para La Gestion de Datos
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

    //Documento para obtener observaciones
    private Documento doc;

    public Documento getDoc() {
        return doc;
    }

    public void setDoc(Documento doc) {
        this.doc = doc;
        lblObservacionesDoc.setText(lblObservacionesDoc.getText() + doc.getNroDocumento());
        cargarDatos();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Añadir la imagen al boton
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/plus-icon.png").toExternalForm(), 40, 40, true, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        btnAnadir.setBackground(background);

        //Asignar las columas
        tcItem.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(tvObservaciones.getItems().indexOf(param.getValue())+1);
        });

        tcInformacion.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getInformacion());
        });

        tcSalida.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getSalida());
        });

        //CellFactory para multilineas
        tcInformacion.setCellFactory((TableColumn<Observacion, String> param) -> {
            TableCell<Observacion, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(tcInformacion.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        
        tcSalida.setCellFactory((TableColumn<Observacion, String> param) -> {
            TableCell<Observacion, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(tcSalida.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        
        //Cell Factory para los botones
        Callback<TableColumn<Observacion, String>, TableCell<Observacion, String>> cellFactory
                = //
                (final TableColumn<Observacion, String> param) -> {
                    final TableCell<Observacion, String> cell = new TableCell<Observacion, String>() {

                Button btn = new Button("   ");
                BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/edit-icon.png").toExternalForm(), 25, 25, true, true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                Background background = new Background(backgroundImage);

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Observacion obs = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            try {
                                //Crear el dialogo
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoAnadirObservacion.fxml"));
                                GridPane page = (GridPane) loader.load();
                                // Create the dialog Stage.
                                Stage dialogStage = new Stage();
                                dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
                                dialogStage.setTitle("Añadir Observacion");
                                dialogStage.initModality(Modality.WINDOW_MODAL);
                                dialogStage.initOwner((Stage) btnAnadir.getScene().getWindow());
                                Scene scene = new Scene(page);
                                dialogStage.setScene(scene);
                                //Enviar el documento al dialogo
                                DialogoAnadirObservacionController controller = loader.getController();
                                controller.setDoc(doc);
                                controller.setObs(obs);
                                dialogStage.showAndWait();
                            } catch (IOException ex) {
                                Logger.getLogger(DialogoMostrarObservacionesController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    cell.setStyle("-fx-alignment: CENTER;");
                    return cell;
                };

        tcEditar.setCellFactory(cellFactory);

        //Asignar a la columna Eliminar
        Callback<TableColumn<Observacion, String>, TableCell<Observacion, String>> cellFactoryDelete
                = //
                (final TableColumn<Observacion, String> param) -> {
                    final TableCell<Observacion, String> cell = new TableCell<Observacion, String>() {

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
                        Observacion obs = getTableView().getItems().get(getIndex());

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
                                try {
                                    //Destruir la observacion
                                    ejbObservacion.destroy(obs.getIdObservacion());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(DialogoMostrarObservacionesController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                // Llenar los valores de la tabla
                                List<Observacion> observaciones = new ArrayList<>();
                                ObservableList<Observacion> elementos = FXCollections.observableArrayList(observaciones);
                                tvObservaciones.getItems().clear();
                                tvObservaciones.setItems(elementos);
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
    }

    @FXML
    private void accionAnadirObservacion(ActionEvent event) {
        try {
            //Crear el dialogo
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoAnadirObservacion.fxml"));
            GridPane page = (GridPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
            dialogStage.setTitle("Añadir Observacion");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner((Stage) btnAnadir.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            //Enviar el documento al dialogo
            DialogoAnadirObservacionController controller = loader.getController();
            controller.setDoc(doc);
            dialogStage.showAndWait();

            //Llenar la tabla
            tvObservaciones.getItems().clear();
            cargarDatos();
        } catch (IOException ex) {
            Logger.getLogger(DialogoMostrarObservacionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo para cargar datos
    private void cargarDatos() {
        if (doc != null) {
            em = ejbObservacion.getEntityManager();
            consulta = em.createNamedQuery("Observacion.findByDocumento", Observacion.class).setParameter("documento", this.doc);
            //Verificar que tenga observaciones
            if (!consulta.getResultList().isEmpty()) {
                ObservableList<Observacion> obs = FXCollections.observableArrayList(consulta.getResultList());
                tvObservaciones.setItems(obs);
            }
        }
    }

}
