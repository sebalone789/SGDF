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
import control.GestorDeDocumentos;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Individuo;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class DialogoEditarIndividuoController implements Initializable {

    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApPaterno;
    @FXML
    private TextField txtApMaterno;
    @FXML
    private TextField txtCargo;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblApPaternoIndividuo;
    @FXML
    private Label lblApMaternoIndividuo;
    @FXML
    private Label lblCargo;
    @FXML
    private Button btnGuardar;
    
    //Individuo a ser editado
    private Individuo individuo;
    

    public Individuo getIndividuo() {
        return individuo;
    }

    public void setIndividuo(Individuo individuo) {
        this.individuo = individuo;

        txtApMaterno.setText(individuo.getApMaterno());
        txtApPaterno.setText(individuo.getApPaterno());
        txtCargo.setText(individuo.getCargo());
        txtNombre.setText(individuo.getNombreIndividuo());
    }

    /**
     * Initializes the controller class.
     */
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
        //Cargar los botones
        cargarIconos("/img/save-icon.png", btnGuardar);
        cargarIconos("/img/cancel-icon.png", btnCancelar);
    }

    @FXML
    private void accionAceptar(ActionEvent event) {
        individuo.setApMaterno(txtApMaterno.getText());
        individuo.setApPaterno(txtApPaterno.getText());
        individuo.setCargo(txtCargo.getText());
        individuo.setNombreIndividuo(txtNombre.getText());
        try {
            ejbIndividuo.edit(individuo);
        } catch (Exception ex) {
            Logger.getLogger(DialogoEditarIndividuoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        accionCancelar(event);
    }

    @FXML
    private void accionCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
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

}
