/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import beans.CategoriaJpaController;
import control.vista.IniciarSesionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Categoria;

/**
 *
 * @author usuario
 */
public class GestorDeDocumentos extends Application {

    //EntityManagerFactory del proyecto
    static public EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("Gestor_de_DocumentosPU");
            //Llenar las categorias
            CategoriaJpaController ejbCategoria = new CategoriaJpaController(emf);
            String[] categorias = {"Documentos Enviados", "Documentos Recibidos", "Resoluciones", "Informes", "Silabos", "Convalidaciones",
                "Plan de Estudios"
            };
            //Verficar que las categorias no esten creadas
            if (ejbCategoria.findCategoriaEntities().isEmpty()) {
                for (String categoria1 : categorias) {
                    Categoria categoria = new Categoria();
                    categoria.setDescripcion(categoria1);
                    ejbCategoria.create(categoria);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        //Mostrar la pantalla principal
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/vista/IniciarSesion.fxml"));
        
        Parent root = loader.load();

        //Obtener el controlador
        IniciarSesionController isc=loader.getController();
        
        Scene scene = new Scene(root);
        
        //Añadir el boton Enter como funcion
        scene.setOnKeyPressed((event) -> {
            if(event.getCode()==KeyCode.ENTER)
            {
                isc.iniciarSesion();
            }
        });

        scene.getStylesheets().add(getClass().getResource("/vista/Estilos.css").toExternalForm());

        //Titulo de la aplicacion
        stage.setTitle("Sistema de Gestion Documentaria de la E.P. de Finanzas");

        //Icono de la aplicacion
        stage.getIcons().add(new Image("/assets/finanzasMini.png"));

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
