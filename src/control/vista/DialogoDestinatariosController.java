/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.vista;

import beans.DestinatariosJpaController;
import control.GestorDeDocumentos;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Destinatarios;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class DialogoDestinatariosController implements Initializable {

    @FXML
    private Label lblNombreDestinatario;
    @FXML
    private Label lblApPaterno;
    @FXML
    private Label lblApMaterno;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblContrasena;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAceptar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApPaterno;
    @FXML
    private TextField txtApMaterno;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtContrasena;

    //Valor para la Edicion si es requerida
    private Destinatarios destinatario;
    private Boolean editar = false;

    public Destinatarios getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Destinatarios destinatario) {
        this.destinatario = destinatario;
        if (destinatario != null) {
            txtApMaterno.setText(destinatario.getApMaterno());
            txtApPaterno.setText(destinatario.getApPaterno());
            txtContrasena.setText(destinatario.getContrasena());
            txtEmail.setText(destinatario.getEmail());
            txtNombre.setText(destinatario.getNombre());
            editar = true;
        }
    }

    //Valores Para La Gestion de Datos    
    final EntityManagerFactory emf = GestorDeDocumentos.emf;
    Boolean existe = false;
    Query consulta;
    EntityManager em;

    //JpaControllers
    private final DestinatariosJpaController ejbDestinatarios = new DestinatariosJpaController(emf);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Cargar los botones
        cargarIconos("/img/save-icon.png", btnAceptar);
        cargarIconos("/img/cancel-icon.png", btnCancelar);
    }

    @FXML
    private void accionCancelar(ActionEvent event) {
        cerrar();
    }

    //Metodo para cerrar ventana
    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void accionAceptar(ActionEvent event) {
        guardar();
    }

    //Metodo para almacenar los datos
    public void guardar() {
        if (!txtApMaterno.getText().isEmpty()) {
            if (!txtApPaterno.getText().isEmpty()) {
                if (!txtContrasena.getText().isEmpty()) {
                    if (!txtEmail.getText().isEmpty()) {
                        if (!txtNombre.getText().isEmpty()) {
                            if (!editar) {
                                Destinatarios dest = new Destinatarios();
                                dest.setApMaterno(txtApMaterno.getText());
                                dest.setApPaterno(txtApPaterno.getText());
                                dest.setContrasena(txtContrasena.getText());
                                dest.setEmail(txtEmail.getText());
                                dest.setNombre(txtNombre.getText());
                                //Crear el registro
                                ejbDestinatarios.create(dest);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Registro Creado");
                                alert.setContentText("Se creo el Registro");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                alert.showAndWait();
                                cerrar();
                            } else {
                                try {
                                    destinatario.setApMaterno(txtApMaterno.getText());
                                    destinatario.setApPaterno(txtApPaterno.getText());
                                    destinatario.setContrasena(txtContrasena.getText());
                                    destinatario.setEmail(txtEmail.getText());
                                    destinatario.setNombre(txtNombre.getText());
                                    //Editar el registro
                                    ejbDestinatarios.edit(destinatario);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Registro Editado");
                                    alert.setContentText("Se edito el Registro");
                                    alert.initModality(Modality.APPLICATION_MODAL);
                                    alert.showAndWait();
                                    cerrar();
                                } catch (Exception ex) {
                                    Logger.getLogger(DialogoDestinatariosController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            mostrarDialogo("Falta el Nombre del Destinatario");
                            //Cambiar el color del textfield
                            ObservableList<String> estilo = txtNombre.getStyleClass();
                            if (!estilo.contains("error")) {
                                estilo.add("error");
                            }
                        }
                    } else {
                        mostrarDialogo("Falta el Email del Destinatario");
                        //Cambiar el color del textfield
                        ObservableList<String> estilo = txtEmail.getStyleClass();
                        if (!estilo.contains("error")) {
                            estilo.add("error");
                        }
                    }
                } else {
                    mostrarDialogo("Falta la Contraseña del Destinatario");
                    //Cambiar el color del textfield
                    ObservableList<String> estilo = txtContrasena.getStyleClass();
                    if (!estilo.contains("error")) {
                        estilo.add("error");
                    }
                }
            } else {
                mostrarDialogo("Falta el Apellido Paterno del Destinatario");
                //Cambiar el color del textfield
                ObservableList<String> estilo = txtApPaterno.getStyleClass();
                if (!estilo.contains("error")) {
                    estilo.add("error");
                }
            }
        } else {
            mostrarDialogo("Falta el Apellido Materno del Destinatario");
            //Cambiar el color del textfield
            ObservableList<String> estilo = txtApMaterno.getStyleClass();
            if (!estilo.contains("error")) {
                estilo.add("error");
            }
        }
    }

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

    //Metodo para eliminar los estilos
    private void removerEstilo() {
        List<ObservableList<String>> estilos = new ArrayList<>();
        //Obtener los estilos
        ObservableList<String> estiloTxtNombre = txtNombre.getStylesheets();
        ObservableList<String> estiloTxtApPaterno = txtApPaterno.getStylesheets();
        ObservableList<String> estiloTxtApMaterno = txtApMaterno.getStylesheets();
        ObservableList<String> estiloTxtEmail = txtEmail.getStylesheets();
        ObservableList<String> estiloTxtContrasena = txtContrasena.getStylesheets();

        //Añadir los estilos a la lista
        estilos.add(estiloTxtEmail);
        estilos.add(estiloTxtApMaterno);
        estilos.add(estiloTxtApPaterno);
        estilos.add(estiloTxtContrasena);
        estilos.add(estiloTxtNombre);

        //Eliminar el estilo
        estilos.forEach((e) -> {
            if (e.contains("error")) {
                e.remove("error");
            }
        });
    }

    //Metodo para mostrar el dialogo
    private void mostrarDialogo(String texto) {
        removerEstilo();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Faltan Datos");
        alert.setContentText(texto);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
