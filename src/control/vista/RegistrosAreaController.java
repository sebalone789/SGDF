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
import beans.exceptions.NonexistentEntityException;
import control.GestorDeDocumentos;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Areas;
import modelo.Documento;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class RegistrosAreaController implements Initializable {

    @FXML
    private TableView<Areas> tvAreas;
    @FXML
    private TableColumn<Areas, Integer> tcNumero;
    @FXML
    private TableColumn<Areas, String> tcDescripcion;
    @FXML
    private TableColumn<Areas, String> tcApPaterno;
    @FXML
    private TableColumn<Areas, String> tcApMaterno;
    @FXML
    private TableColumn<Areas, String> tcNombre;
    @FXML
    private TableColumn<Areas, String> tcEditar;
    @FXML
    private TableColumn<Areas, String> tcEliminar;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Llenar las tablas
        ObservableList<Areas> areas = FXCollections.observableArrayList(ejbArea.findAreasEntities());
        tvAreas.setItems(areas);

        //Asignar las columnas
        tcApMaterno.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getApMaternoResponsable());
        });

        tcApPaterno.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getApPaternoResponsable());
        });

        tcDescripcion.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getDescripcion());
        });

        tcNombre.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getNombreResponsable());
        });

        tcNumero.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(tvAreas.getItems().indexOf(param.getValue()) + 1);
        });

        //Cell Factories para los botones
        //Asignar a la columna Editar
        Callback<TableColumn<Areas, String>, TableCell<Areas, String>> cellFactoryEdit
                = (final TableColumn<Areas, String> param) -> {
                    final TableCell<Areas, String> cell = new TableCell<Areas, String>() {

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
                        Areas area = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            //Crear el dialogo
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoEditarArea.fxml"));
                            try {
                                GridPane page = (GridPane) loader.load();
                                // Create the dialog Stage.
                                Stage dialogStage = new Stage();
                                dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
                                dialogStage.setTitle("Editar Registro");
                                dialogStage.initModality(Modality.WINDOW_MODAL);
                                dialogStage.initOwner((Stage) btn.getScene().getWindow());
                                Scene scene = new Scene(page);
                                dialogStage.setScene(scene);
                                //Mandar la area al controlador
                                DialogoEditarAreaController controller = loader.getController();
                                controller.setArea(area);
                                dialogStage.showAndWait();
                                // Llenar los valores de la tabla
                                em = ejbArea.getEntityManager();
                                consulta = em.createNamedQuery("Areas.findAll", Areas.class);
                                ObservableList<Areas> elementos = FXCollections.observableArrayList(consulta.getResultList());
                                tvAreas.getItems().clear();
                                tvAreas.setItems(elementos);
                            } catch (IOException ex) {
                                Logger.getLogger(RegistrosAreaController.class.getName()).log(Level.SEVERE, null, ex);
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

        //Asignar la cell factory a la columna Editar
        tcEditar.setCellFactory(cellFactoryEdit);

        //Asignar a la columna Eliminar
        Callback<TableColumn<Areas, String>, TableCell<Areas, String>> cellFactoryDelete
                = (final TableColumn<Areas, String> param) -> {
                    final TableCell<Areas, String> cell = new TableCell<Areas, String>() {

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
                        Areas area = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Eliminar Registro");
                            alert.setHeaderText("Se Eliminara el Registro");
                            alert.setContentText("OJO: SI SE ELIMINA UN REGISTRO, SE ELIMINARAN TODOS LOS DOCUMENTOS RELACIONADOS A EL.\n"
                                    + "¿Esta Seguro?");
                            alert.initModality(Modality.APPLICATION_MODAL);
                            ButtonType buttonTypeOne = new ButtonType("Si");
                            ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == buttonTypeOne) {
                                try {
                                    //Eliminacion en cascada
                                    List<Documento> documentos=ejbDocumento.findDocumentoEntities();
                                    documentos.forEach((d)->{
                                        if(d.getArea()!=null)
                                        {
                                            if(d.getArea().getIdArea()==area.getIdArea())
                                            {
                                                try {
                                                    ejbDocumento.destroy(d.getIdDocumento());
                                                } catch (NonexistentEntityException ex) {
                                                    Logger.getLogger(RegistrosAreaController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    });
                                    //Eliminar el registro
                                    ejbArea.destroy(area.getIdArea());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(RegistrosAreaController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                // Llenar los valores de la tabla
                                ObservableList<Areas> elementos = FXCollections.observableArrayList(ejbArea.findAreasEntities());
                                tvAreas.setItems(elementos);

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

}
