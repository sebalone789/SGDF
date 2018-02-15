/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.vista;

import beans.AreasJpaController;
import beans.CategoriaJpaController;
import beans.DestinatariosJpaController;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
import modelo.Destinatarios;
import modelo.Documento;
import modelo.Individuo;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class RegistrosDestinatarioController implements Initializable {

    @FXML
    private Label indicador;
    @FXML
    private TableView<Destinatarios> tvDestinatarios;
    @FXML
    private TableColumn<Destinatarios, Integer> tcNumero;
    @FXML
    private TableColumn<Destinatarios, String> tcNombre;
    @FXML
    private TableColumn<Destinatarios, String> tcApPaterno;
    @FXML
    private TableColumn<Destinatarios, String> tcApMaterno;
    @FXML
    private TableColumn<Destinatarios, String> tcEmail;
    @FXML
    private TableColumn<Destinatarios, String> tcEditar;
    @FXML
    private TableColumn<Destinatarios, String> tcEliminar;
    @FXML
    private Label lblAnadirDestinatario;
    @FXML
    private Button btnAnadir;
    //Valores Para La Gestion de Datos    
    final EntityManagerFactory emf = GestorDeDocumentos.emf;
    Boolean existe = false;
    Query consulta;
    EntityManager em;

    //JpaControllers
    final DestinatariosJpaController ejbDestinatarios = new DestinatariosJpaController(emf);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Añadir la imagen al boton
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/plus-icon.png").toExternalForm(), 40, 40, true, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        btnAnadir.setBackground(background);

        // Llenar la tabla
        ObservableList<Destinatarios> valores = FXCollections.observableArrayList(ejbDestinatarios.findDestinatariosEntities());
        tvDestinatarios.setItems(valores);

        //LLenar las columnas
        tcApMaterno.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getApMaterno());
        });

        tcApPaterno.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getApPaterno());
        });

        tcEmail.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getEmail());
        });

        tcNombre.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().getNombre());
        });

        tcNumero.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(tvDestinatarios.getItems().indexOf(param.getValue()) + 1);
        });

        //Cell Factories para los botones
        //Asignar a la columna Editar
        Callback<TableColumn<Destinatarios, String>, TableCell<Destinatarios, String>> cellFactoryEdit
                = (final TableColumn<Destinatarios, String> param) -> {
                    final TableCell<Destinatarios, String> cell = new TableCell<Destinatarios, String>() {

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
                        Destinatarios dest = getTableView().getItems().get(getIndex());
                        btn.setBackground(background);
                        btn.setOnAction(event -> {
                            //Crear el dialogo
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoDestinatarios.fxml"));
                            try {
                                GridPane page = (GridPane) loader.load();
                                // Create the dialog Stage.
                                Stage dialogStage = new Stage();
                                dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
                                dialogStage.setTitle("Gestion de Destinatario");
                                dialogStage.initModality(Modality.WINDOW_MODAL);
                                dialogStage.initOwner((Stage) btn.getScene().getWindow());
                                Scene scene = new Scene(page);
                                dialogStage.setScene(scene);
                                //Mandar la area al controlador
                                DialogoDestinatariosController controller = loader.getController();
                                controller.setDestinatario(dest);
                                dialogStage.showAndWait();
                                // Llenar los valores de la tabla
                                ObservableList<Destinatarios> elementos = FXCollections.observableArrayList(ejbDestinatarios.findDestinatariosEntities());
                                tvDestinatarios.getItems().clear();
                                tvDestinatarios.setItems(elementos);
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
        Callback<TableColumn<Destinatarios, String>, TableCell<Destinatarios, String>> cellFactoryDelete
                = (final TableColumn<Destinatarios, String> param) -> {
                    final TableCell<Destinatarios, String> cell = new TableCell<Destinatarios, String>() {

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
                        Destinatarios dest = getTableView().getItems().get(getIndex());
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
                                    //Eliminar el registro
                                    ejbDestinatarios.destroy(dest.getIdDestinario());
                                } catch (NonexistentEntityException ex) {
                                    Logger.getLogger(RegistrosDestinatarioController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                // Llenar los valores de la tabla
                                ObservableList<Destinatarios> elementos = FXCollections.observableArrayList(ejbDestinatarios.findDestinatariosEntities());
                                tvDestinatarios.setItems(elementos);

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
    private void accionAnadir(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoDestinatarios.fxml"));
        try {
            GridPane page = (GridPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image("/assets/finanzasMini.png"));
            dialogStage.setTitle("Gestion de Destinatario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner((Stage) btnAnadir.getScene().getWindow());
            Scene scene = new Scene(page);

            //Colocar la tecla Enter como botno aceptar
            scene.setOnKeyPressed((e) -> {
                if (e.getCode() == KeyCode.ENTER) {
                    ((DialogoDestinatariosController) loader.getController()).guardar();
                }
            });

            dialogStage.setScene(scene);
            //Mandar la area al controlador
            DialogoDestinatariosController controller = loader.getController();
            dialogStage.showAndWait();
            // Llenar los valores de la tabla
            ObservableList<Destinatarios> elementos = FXCollections.observableArrayList(ejbDestinatarios.findDestinatariosEntities());
            tvDestinatarios.getItems().clear();
            tvDestinatarios.setItems(elementos);
        } catch (IOException ex) {
            Logger.getLogger(RegistrosAreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
