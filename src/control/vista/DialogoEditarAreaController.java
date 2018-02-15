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
import modelo.Areas;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class DialogoEditarAreaController implements Initializable {

    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApPaterno;
    @FXML
    private TextField txtApMaterno;
    @FXML
    private Label lblNombreArea;
    @FXML
    private Label lblNombreResponsable;
    @FXML
    private Label lblApPaternoResponsable;
    @FXML
    private Label lblApMaternoResponsable;
    @FXML
    private Button btnGuardar;
    
    //Valor para recibir la area a ser editada
    private Areas area;


    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
        if (area != null) {
            txtApMaterno.setText(area.getApMaternoResponsable());
            txtApPaterno.setText(area.getApPaternoResponsable());
            txtDescripcion.setText(area.getDescripcion());
            txtNombre.setText(area.getNombreResponsable());
        }
    }

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Cargar los botones
        cargarIconos("/img/save-icon.png", btnGuardar);
        cargarIconos("/img/cancel-icon.png", btnCancelar);
    }

    @FXML
    private void accionAceptar(ActionEvent event) {
        area.setDescripcion(txtDescripcion.getText());
        area.setApMaternoResponsable(txtApMaterno.getText());
        area.setApPaternoResponsable(txtApPaterno.getText());
        area.setNombreResponsable(txtNombre.getText());
        try {
            ejbArea.edit(area);
        } catch (Exception ex) {
            Logger.getLogger(DialogoEditarAreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        accionCancelar(event);
    }

    @FXML
    private void accionCancelar(ActionEvent event) {
        Stage stage=(Stage)btnCancelar.getScene().getWindow();
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
