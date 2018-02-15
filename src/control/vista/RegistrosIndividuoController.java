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
import java.util.Objects;
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
import modelo.Individuo;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class RegistrosIndividuoController implements Initializable {

    @FXML
    private TableView<Individuo> tvIndividuos;
    @FXML
    private TableColumn<Individuo, Integer> tcNumero;
    @FXML
    private TableColumn<Individuo, String> tcNombre;
    @FXML
    private TableColumn<Individuo, String> tcApPaterno;
    @FXML
    private TableColumn<Individuo, String> tcApMaterno;
    @FXML
    private TableColumn<Individuo, String> tcCargo;
    @FXML
    private TableColumn<Individuo, String> tcEditar;
    @FXML
    private TableColumn<Individuo, String> tcEliminar;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Llenar la tabla
        ObservableList<Individuo> valores = FXCollections.observableArrayList(ejbIndividuo.findIndividuoEntities());
        tvIndividuos.setItems(valores);

        //Asignar las columnas
        tcApMaterno.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getApMaterno());
        });

        tcApPaterno.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getApPaterno());
        });

        tcCargo.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getCargo());
        });

        tcNombre.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getNombreIndividuo());
        });

        tcNumero.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(tvIndividuos.getItems().indexOf(param.getValue()) + 1);
        });

        //Cell Factories para los botones
        //Asignar a la columna Editar
        Callback<TableColumn<Individuo, String>, TableCell<Individuo, String>> cellFactoryEdit
                = (final TableColumn<Individuo, String> param) -> {
                    final TableCell<Individuo, String> cell = new TableCell<Individuo, String>() {

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
                        Individuo individuo = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            //Crear el dialogo
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoEditarIndividuo.fxml"));
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
                                DialogoEditarIndividuoController controller = loader.getController();
                                controller.setIndividuo(individuo);
                                dialogStage.showAndWait();
                                // Llenar los valores de la tabla
                                em = ejbIndividuo.getEntityManager();
                                consulta = em.createNamedQuery("Individuo.findAll", Individuo.class);
                                ObservableList<Individuo> elementos = FXCollections.observableArrayList(consulta.getResultList());
                                tvIndividuos.getItems().clear();
                                tvIndividuos.setItems(elementos);
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
        Callback<TableColumn<Individuo, String>, TableCell<Individuo, String>> cellFactoryDelete
                = (final TableColumn<Individuo, String> param) -> {
                    final TableCell<Individuo, String> cell = new TableCell<Individuo, String>() {

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
                        Individuo ind = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Eliminar Registro");
                            alert.setHeaderText("OJO: SI SE ELIMINA UN REGISTRO, SE ELIMINARAN TODOS LOS DOCUMENTOS RELACIONADOS A EL.\n"
                                    + "Se Eliminara el Registro");
                            alert.setContentText("¿Esta Seguro?");
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
                                        if(d.getIndividuo()!=null)
                                        {
                                            if(Objects.equals(ind.getIdIndividuo(), d.getIndividuo().getIdIndividuo()))
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
                                    ejbIndividuo.destroy(ind.getIdIndividuo());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(RegistrosIndividuoController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                // Llenar los valores de la tabla
                                ObservableList<Individuo> elementos = FXCollections.observableArrayList(ejbIndividuo.findIndividuoEntities());
                                tvIndividuos.setItems(elementos);

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
