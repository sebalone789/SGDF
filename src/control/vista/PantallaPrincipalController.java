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
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Categoria;
import modelo.Documento;
import modelo.Individuo;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class PantallaPrincipalController extends MenuPadre implements Initializable {

    @FXML
    private TableView<Documento> tvUltimosDocumentos;
    @FXML
    private TableColumn<Documento, String> tcItem;
    @FXML
    private TableColumn<Documento, String> tcCategoria;
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
    private Menu menuInicio;
    @FXML
    private MenuItem miUltimosDocumentos;
    @FXML
    private MenuItem miRegistrarDocumento;
    @FXML
    private Menu menuDocumentos;
    @FXML
    private Menu menuDocEnviados;
    @FXML
    private MenuItem miDocEnviadosTodos;
    @FXML
    private MenuItem miDocEnviadosArea;
    @FXML
    private MenuItem miDocEnviadosIndividuo;
    @FXML
    private Menu menuDocRecibidos;
    @FXML
    private MenuItem miDocRecibidosTodos;
    @FXML
    private MenuItem miDocRecibidosArea;
    @FXML
    private MenuItem miDocRecibidosIndividuo;
    @FXML
    private Menu menuInformes;
    @FXML
    private MenuItem miInformesTodos;
    @FXML
    private MenuItem miInformesArea;
    @FXML
    private MenuItem miInformesIndividuo;
    @FXML
    private MenuItem miResoluciones;
    @FXML
    private MenuItem miSilabos;
    @FXML
    private MenuItem miConvalidaciones;
    @FXML
    private MenuItem miPlanEstudios;
    @FXML
    private Menu menuRegistros;
    @FXML
    private MenuItem miRegistrosIndividuos;
    @FXML
    private MenuItem miRegistrosArea;
    @FXML
    private Menu menuAyuda;
    @FXML
    private MenuItem miAyudaManual;
    @FXML
    private MenuItem miAyudaAcercaDe;
    @FXML
    private Label indicador;
    @FXML
    private Menu menuHerramientas;
    @FXML
    private MenuItem miCrearRespaldo;
    @FXML
    private MenuItem miImportarRespaldo;
    @FXML
    private MenuItem miDestinatarios;
    @FXML
    private MenuItem miExportarDatos;
    @FXML
    private MenuItem miImportarDatos;

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
    @FXML
    private MenuBar menuIncio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Colocar los iconos en los menus
        cargarIconos("/img/help-icon.png", menuAyuda);
        cargarIconos("/img/document-icon.png", menuDocumentos);
        cargarIconos("/img/home-icon.png", menuInicio);
        cargarIconos("/img/register-icon.png", menuRegistros);
        cargarIconos("/img/tools-icon.png", menuHerramientas);

        // Cargar datos en la tabla
        List<Documento> documentos = new ArrayList<>();
        //Encontrar la categoria
        em = ejbDocumento.getEntityManager();
        consulta = em.createNamedQuery("Documento.findAllDesc", Documento.class);
        documentos = consulta.setMaxResults(20).getResultList();

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

        tcCategoria.setCellValueFactory((param) -> {
            Documento doc = param.getValue();
            return new ReadOnlyObjectWrapper<>(doc.getCategoria().getDescripcion());
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
                BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/search-icon.png")
                        .toExternalForm(), 50, 50, true, true),
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
    }

    private void cargarIconos(String ruta, Menu menu) {
        try {
            Image icono = new Image(PantallaPrincipalController.class.getResourceAsStream(ruta));
            ImageView iconoView = new ImageView(icono);
            iconoView.setFitHeight(20);
            iconoView.setFitWidth(20);
            menu.setGraphic(iconoView);
        } catch (Exception e) {
        }
    }

}
