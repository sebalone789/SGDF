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
import control.GestorDeDocumentos;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
public class DialogoAnadirObservacionController implements Initializable {

    @FXML
    private Label lblInformacion;
    @FXML
    private Label lblSalida;
    @FXML
    private TextArea txtInformacion;
    @FXML
    private TextArea txtSalida;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;

    //Documento al que se añadira la observacion
    private Documento doc;
    
    //Booleano para la edicion
    Boolean editando=false;
    
    //Observacion para edicion o creacion
    private Observacion obs;
    

    public Observacion getObs() {
        return obs;
    }

    public void setObs(Observacion obs) {
        this.obs = obs;
        txtInformacion.setText(obs.getInformacion());
        txtSalida.setText(obs.getSalida());
        editando=true;
    }

    public Documento getDoc() {
        return doc;
    }

    public void setDoc(Documento doc) {
        this.doc = doc;
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
    final ObservacionJpaController ejbObservacion = new ObservacionJpaController(emf);

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
    private void accionCancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void accionAceptar(ActionEvent event) {
        if(!editando){
        //Crear nueva observacion
        obs = new Observacion();
        obs.setDocumento(doc);
        obs.setInformacion(txtInformacion.getText());
        obs.setSalida(txtSalida.getText());
        //crear la observacion
        ejbObservacion.create(obs);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro Creado");
        alert.setContentText("Se ha creado el registro");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        }
        else{
        //Editar observacion
        obs.setDocumento(doc);
        obs.setInformacion(txtInformacion.getText());
        obs.setSalida(txtSalida.getText());
            try {
                //editar
                ejbObservacion.edit(obs);
            } catch (Exception ex) {
                Logger.getLogger(DialogoAnadirObservacionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro Editado");
        alert.setContentText("Se ha editado el registro");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        }
        accionCancelar(event);
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
