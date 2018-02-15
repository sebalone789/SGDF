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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import modelo.Areas;
import modelo.Categoria;
import modelo.Documento;
import modelo.Individuo;
import org.apache.commons.io.FilenameUtils;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class RegistrarNuevoDocumentoController implements Initializable {

    @FXML
    private TextArea txtTituloDocumento;
    @FXML
    private TextField txtUbicacionDocumento;
    @FXML
    private ComboBox<String> cbCategoriaDocumento;
    @FXML
    private DatePicker dtpFechaEmision;
    @FXML
    private DatePicker dtpFechaRecepcion;
    @FXML
    private Button btnBuscarDocumento;
    @FXML
    private RadioButton rbIndividuo;
    @FXML
    private ToggleGroup TipoPersona;
    @FXML
    private RadioButton rbArea;
    @FXML
    private TextField txtNombreIndividuo;
    @FXML
    private TextField txtApPaternoIndividuo;
    @FXML
    private TextField txtApMaternoIndividuo;
    @FXML
    private TextField txtCargoIndividuo;
    @FXML
    private TextField txtNombreResponsableArea;
    @FXML
    private TextField txtApPaternoResponsableArea;
    @FXML
    private TextField txtApMaternoResponsableArea;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<String> cbNombreArea;
    @FXML
    private CheckBox chkbFormatoDigital;
    @FXML
    private TextField txtNroDocumento;

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

    //Valor para la Edicion de un Documento
    Documento documentoExistente = new Documento();

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblUrlDocumento;
    @FXML
    private Label lblCategoria;
    @FXML
    private Label lblFechaEmision;
    @FXML
    private Label lblFechaRecepcion;
    @FXML
    private Label lblNumero;
    @FXML
    private Label lblNombreArea;
    @FXML
    private Label lblNombreResponsable;
    @FXML
    private Label lblApPaternoResponsable;
    @FXML
    private Label lblApMaternoResponsable;
    @FXML
    private Label lblNombreIndividuo;
    @FXML
    private Label lblApPaternoIndividuo;
    @FXML
    private Label lblApMaternoIndividuo;
    @FXML
    private Label lblCargo;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Colocar los iconos a los botones
        cargarIconos("/img/save-icon.png", btnGuardar);
        cargarIconos("/img/cancel-icon.png", btnCancelar);

        //Llenar la combo box de categorias
        List<Categoria> categorias = ejbCategoria.findCategoriaEntities();
        List<String> valoresCat = new ArrayList<>();
        categorias.forEach((c) -> {
            valoresCat.add(c.getDescripcion());
        });
        ObservableList<String> valoresC = FXCollections.observableArrayList(valoresCat);
        cbCategoriaDocumento.setItems(valoresC);

        //Llenar la ComboBox de areas
        List<Areas> areas = ejbArea.findAreasEntities();
        List<String> valoresArea = new ArrayList<>();
        areas.forEach((a) -> {
            valoresArea.add(a.getDescripcion());
        });
        ObservableList<String> valoresA = FXCollections.observableArrayList(valoresArea);
        cbNombreArea.setItems(valoresA);

        //Habilitar los campos en funcion al RadioButton seleccionado
        try {
            TipoPersona.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                RadioButton chk = null;
                try {
                    chk = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
                } catch (NullPointerException e) {
                }
                switch (chk.getText()) {
                    case "Individuo":
                        //Habilitar los campos de Individuo
                        txtApMaternoIndividuo.setDisable(false);
                        txtApPaternoIndividuo.setDisable(false);
                        txtCargoIndividuo.setDisable(false);
                        txtNombreIndividuo.setDisable(false);
                        //Deshabilitar los campos de Area
                        txtApMaternoResponsableArea.setDisable(true);
                        txtApPaternoResponsableArea.setDisable(true);
                        cbNombreArea.setDisable(true);
                        txtNombreResponsableArea.setDisable(true);
                        break;
                    case "Area":
                        //Habilitar los campos de area
                        txtApMaternoResponsableArea.setDisable(false);
                        txtApPaternoResponsableArea.setDisable(false);
                        cbNombreArea.setDisable(false);
                        txtNombreResponsableArea.setDisable(false);
                        //Deshabilitar los campos de individuo
                        txtApMaternoIndividuo.setDisable(true);
                        txtApPaternoIndividuo.setDisable(true);
                        txtCargoIndividuo.setDisable(true);
                        txtNombreIndividuo.setDisable(true);
                        break;
                    default:
                        //Deshabilitar todo
                        txtApMaternoResponsableArea.setDisable(true);
                        txtApPaternoResponsableArea.setDisable(true);
                        cbNombreArea.setDisable(true);
                        txtNombreResponsableArea.setDisable(true);
                        txtApMaternoIndividuo.setDisable(true);
                        txtApPaternoIndividuo.setDisable(true);
                        txtCargoIndividuo.setDisable(true);
                        txtNombreIndividuo.setDisable(true);
                        //Limpiar todo
                        //Limpiar todo los campos excepto el titulo
                        txtApMaternoIndividuo.clear();
                        txtApMaternoResponsableArea.clear();
                        txtApPaternoIndividuo.clear();
                        txtApPaternoResponsableArea.clear();
                        txtCargoIndividuo.clear();
                        txtNombreIndividuo.clear();
                        txtNombreResponsableArea.clear();
                        break;
                }
            });
        } catch (Exception e) {
        }

        //Verificar si ya existe un registro con el nombre del individuo
        txtApPaternoIndividuo.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            em = ejbIndividuo.getEntityManager();
            consulta = em.createNamedQuery("Individuo.findByNombreIndividuo", Individuo.class).setParameter("nombreIndividuo", newValue);
            //Verificar si el registro existe
            try {
                if (consulta.getSingleResult() != null) {
                    Individuo individuoExistente = (Individuo) consulta.getSingleResult();
                    //Si el registro existe llenar los campos
                    txtApMaternoIndividuo.setText(individuoExistente.getApMaterno());
                    txtNombreIndividuo.setText(individuoExistente.getNombreIndividuo());
                    txtCargoIndividuo.setText(individuoExistente.getCargo());
                } else {
                    //Limpiar los campos Llenados
                    txtApMaternoIndividuo.setText(null);
                    txtNombreIndividuo.setText(null);
                    txtCargoIndividuo.setText(null);
                }
            } catch (NoResultException e) {
            }
        });

        //Verificar si ya existe un registro con el Nombre del Area
        cbNombreArea.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            em = ejbArea.getEntityManager();
            //Verificar si ya existe el registro
            consulta = em.createNamedQuery("Areas.findByDescripcion", Areas.class).setParameter("descripcion", newValue);
            try {
                if (consulta.getSingleResult() != null) {
                    Areas areaExistente = (Areas) consulta.getSingleResult();
                    //Si el registro existe llenar los campos
                    txtApMaternoResponsableArea.setText(areaExistente.getApMaternoResponsable());
                    txtApPaternoResponsableArea.setText(areaExistente.getApPaternoResponsable());
                    txtNombreResponsableArea.setText(areaExistente.getNombreResponsable());
                } else {
                    //Limpiar los Campos Llenados
                    txtApMaternoResponsableArea.setText(null);
                    txtApPaternoResponsableArea.setText(null);
                    txtNombreResponsableArea.setText(null);
                }
            } catch (Exception e) {
            }
        });

        //Habilitar campos segun categoria
        cbCategoriaDocumento.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            switch (newValue) {
                case "Documentos Enviados":
                    cbNombreArea.setValue("Direccion Escuela Profesional de Finanzas");
                    txtApPaternoResponsableArea.setText("Suyo");
                    txtApMaternoResponsableArea.setText("Caparo");
                    txtNombreResponsableArea.setText("Aquiles");
                    TipoPersona.selectToggle(rbArea);
                    break;
                case "Resoluciones":
                    TipoPersona.selectToggle(rbArea);
                    break;
                case "Silabos":
                    TipoPersona.selectToggle(rbIndividuo);
                    txtCargoIndividuo.setText("Docente");
                    break;
                case "Convalidaciones":
                    TipoPersona.selectToggle(rbIndividuo);
                    txtCargoIndividuo.setText("Alumno");
                    break;
                case "Plan de Estudios":
                    cbNombreArea.setValue("Direccion Escuela Profesional de Finanzas");
                    txtApPaternoResponsableArea.setText("Suyo");
                    txtApMaternoResponsableArea.setText("Caparo");
                    txtNombreResponsableArea.setText("Aquiles");
                    TipoPersona.selectToggle(rbArea);
                    break;
                default:
                    //Limpiar los campos
                    TipoPersona.selectToggle(null);
                    txtApMaternoIndividuo.clear();
                    txtApMaternoResponsableArea.clear();
                    txtApPaternoIndividuo.clear();
                    txtApPaternoResponsableArea.clear();
                    txtCargoIndividuo.clear();
                    cbNombreArea.setValue(null);
                    txtNombreIndividuo.clear();
                    txtNombreResponsableArea.clear();
                    //Deshabilitar todo
                    txtApMaternoResponsableArea.setDisable(true);
                    txtApPaternoResponsableArea.setDisable(true);
                    cbNombreArea.setDisable(true);
                    txtNombreResponsableArea.setDisable(true);
                    txtApMaternoIndividuo.setDisable(true);
                    txtApPaternoIndividuo.setDisable(true);
                    txtCargoIndividuo.setDisable(true);
                    txtNombreIndividuo.setDisable(true);
                    break;
            }
        });

        //Verificar que no exista un documento con ese numero
        txtNroDocumento.textProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    //Verficar que no este habilitado via nro
                    em = ejbDocumento.getEntityManager();
                    consulta = em.createNamedQuery("Documento.findByNroDocumento", Documento.class).setParameter("nroDocumento", newValue);
                    if (consulta.getResultList().isEmpty()) {
                        if (existe) {
                            existe = false;
                            btnGuardar.setText("Guardar");
                            //Limpiar todo los campos excepto el nro y el titulo(si esta escrito en el)
                            if (txtTituloDocumento.getText().isEmpty()) {
                                txtTituloDocumento.clear();
                            }
                            txtApMaternoIndividuo.clear();
                            txtApMaternoResponsableArea.clear();
                            txtApPaternoIndividuo.clear();
                            txtApPaternoResponsableArea.clear();
                            txtCargoIndividuo.clear();
                            cbNombreArea.setValue(null);
                            txtNombreIndividuo.clear();
                            txtNombreResponsableArea.clear();
                            txtUbicacionDocumento.clear();
                            chkbFormatoDigital.setSelected(false);
                            try {
                                cbCategoriaDocumento.setValue(null);
                                dtpFechaEmision.setValue(null);
                                dtpFechaRecepcion.setValue(null);
                                TipoPersona.selectToggle(null);
                            } catch (NullPointerException e) {
                            }
                            //Deshabilitar todo
                            txtApMaternoResponsableArea.setDisable(true);
                            txtApPaternoResponsableArea.setDisable(true);
                            cbNombreArea.setDisable(true);
                            txtNombreResponsableArea.setDisable(true);
                            txtApMaternoIndividuo.setDisable(true);
                            txtApPaternoIndividuo.setDisable(true);
                            txtCargoIndividuo.setDisable(true);
                            txtNombreIndividuo.setDisable(true);
                        }

                    } else {
                        Documento documentoSelccionado = (Documento) consulta.getSingleResult();
                        //Llenar los campos
                        if (documentoSelccionado.getUrlDocumento() != null) {
                            txtUbicacionDocumento.setText(documentoSelccionado.getUrlDocumento());
                            chkbFormatoDigital.setSelected(true);
                        }
                        txtTituloDocumento.setText(documentoSelccionado.getTituloDocumento());
                        cbCategoriaDocumento.setValue(documentoSelccionado.getCategoria().getDescripcion());
                        dtpFechaEmision.setValue(documentoSelccionado.getFechaEmision().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        dtpFechaRecepcion.setValue(documentoSelccionado.getFechaRecepcion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        if (documentoSelccionado.getArea() != null) {
                            TipoPersona.selectToggle(rbArea);
                            cbNombreArea.setValue(documentoSelccionado.getArea().getDescripcion());
                            txtApMaternoResponsableArea.setText(documentoSelccionado.getArea().getApMaternoResponsable());
                            txtApPaternoResponsableArea.setText(documentoSelccionado.getArea().getApPaternoResponsable());
                            txtNombreResponsableArea.setText(documentoSelccionado.getArea().getNombreResponsable());
                        } else {
                            TipoPersona.selectToggle(rbIndividuo);
                            txtApMaternoIndividuo.setText(documentoSelccionado.getIndividuo().getApMaterno());
                            txtApPaternoIndividuo.setText(documentoSelccionado.getIndividuo().getApPaterno());
                            txtCargoIndividuo.setText(documentoSelccionado.getIndividuo().getCargo());
                            txtNombreIndividuo.setText(documentoSelccionado.getIndividuo().getNombreIndividuo());
                        }
                        documentoExistente = documentoSelccionado;
                        existe = true;
                        btnGuardar.setText("Editar");
                    }
                }));

        //Habilitar el campo de la ubicacion del documento
        chkbFormatoDigital.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        btnBuscarDocumento.setDisable(false);
                    } else {
                        txtTituloDocumento.setText("");
                        btnBuscarDocumento.setDisable(true);
                    }
                }
                );

        //Actualizar la fecha de recepcion en funcion a la de emision
        dtpFechaEmision.valueProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    dtpFechaRecepcion.setValue(newValue);
                    dtpFechaRecepcion.setDisable(false);
                }));

        //Restringir las fechas en la Recepcion
        dtpFechaRecepcion.setDayCellFactory(
                (param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty
            ) {
                super.updateItem(date, empty);
                if (date.isBefore(dtpFechaEmision.getValue())) {
                    setDisable(true);
                }
            }
        }
        );

    }

    @FXML
    private void accionBuscarDocumento(ActionEvent event) {
        //Crear el Selector de archivos
        FileChooser fileChooser = new FileChooser();

        //Crear Filtros
        FileChooser.ExtensionFilter extFilterTXT = new FileChooser.ExtensionFilter("Text Files (*.txt),(*.docx),(*.xlsx),(*.pdf),"
                + "", "*.txt", "*.docx", "*.xlsx", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilterTXT);

        //Mostrar el Dialogo para seleccionar
        File file = fileChooser.showOpenDialog(null);

        //Mostrar la URL en el txt para su posterior guardado
        if (file != null) {
            txtUbicacionDocumento.setText(file.getPath());
        }
    }

    @FXML
    private void accionGuardar(ActionEvent event) {
        accionGuardado();
    }

    //Metodo para los botones de guardado
    public void accionGuardado() {
        //Verficar que todos los registros esten llenos
        if (!txtNroDocumento.getText().isEmpty()) {
            if (!txtTituloDocumento.getText().isEmpty()) {
                if (!(txtUbicacionDocumento.getText().isEmpty() && chkbFormatoDigital.isSelected())) {
                    if (cbCategoriaDocumento.getValue() != null) {
                        if (dtpFechaEmision.getValue() != null) {
                            if (dtpFechaRecepcion.getValue() != null) {
                                //Revisar los campos del radio button
                                if (TipoPersona.getSelectedToggle() != null) {
                                    RadioButton chk = (RadioButton) TipoPersona.getSelectedToggle();
                                    switch (chk.getText()) {
                                        case "Individuo":
                                            if (!txtApPaternoIndividuo.getText().isEmpty()) {
                                                if (!txtApMaternoIndividuo.getText().isEmpty()) {
                                                    if (!txtNombreIndividuo.getText().isEmpty()) {
                                                        if (!txtCargoIndividuo.getText().isEmpty()) {
                                                            if (!existe) {
                                                                guardarDocumento(1);
                                                            } else {
                                                                editarDocumento();
                                                            }
                                                            //Limpiar
                                                            limpiar();
                                                        } else {
                                                            mostrarDialogo("Falta el Cargo del Individuo");
                                                            //Cambiar el color del textfield
                                                            ObservableList<String> estilo = txtCargoIndividuo.getStyleClass();
                                                            if (!estilo.contains("error")) {
                                                                estilo.add("error");
                                                            }
                                                        }
                                                    } else {
                                                        mostrarDialogo("Falta el Nombre del Individuo");
                                                        //Cambiar el color del textfield
                                                        ObservableList<String> estilo = txtNombreIndividuo.getStyleClass();
                                                        if (!estilo.contains("error")) {
                                                            estilo.add("error");
                                                        }
                                                    }
                                                } else {
                                                    mostrarDialogo("Falta el Apellido Materno del Inidividuo");
                                                    //Cambiar el color del textfield
                                                    ObservableList<String> estilo = txtApMaternoIndividuo.getStyleClass();
                                                    if (!estilo.contains("error")) {
                                                        estilo.add("error");
                                                    }
                                                }
                                            } else {
                                                mostrarDialogo("Falta el Apellido Paterno del Individuo");
                                                //Cambiar el color del textfield
                                                ObservableList<String> estilo = txtApPaternoIndividuo.getStyleClass();
                                                if (!estilo.contains("error")) {
                                                    estilo.add("error");
                                                }
                                            }
                                            break;
                                        case "Area":
                                            if (cbNombreArea.getValue() != null) {
                                                if (!txtNombreResponsableArea.getText().isEmpty()) {
                                                    if (!txtApPaternoResponsableArea.getText().isEmpty()) {
                                                        if (!txtApMaternoResponsableArea.getText().isEmpty()) {
                                                            if (!existe) {
                                                                guardarDocumento(2);
                                                            } else {
                                                                editarDocumento();
                                                            }
                                                            //Limpiar
                                                            limpiar();
                                                        } else {
                                                            mostrarDialogo("Falta el Apellido Materno del responsable del Area");
                                                            //Cambiar el color del textfield
                                                            ObservableList<String> estilo = txtApMaternoResponsableArea.getStyleClass();
                                                            if (!estilo.contains("error")) {
                                                                estilo.add("error");
                                                            }
                                                        }
                                                    } else {
                                                        mostrarDialogo("Falta el Apellido Paterno del Responsable del Area");
                                                        //Cambiar el color del textfield
                                                        ObservableList<String> estilo = txtApPaternoResponsableArea.getStyleClass();
                                                        if (!estilo.contains("error")) {
                                                            estilo.add("error");
                                                        }
                                                    }
                                                } else {
                                                    mostrarDialogo("Falta el Nombre del Responsable del Area");
                                                    //Cambiar el color del textfield
                                                    ObservableList<String> estilo = txtNombreResponsableArea.getStyleClass();
                                                    if (!estilo.contains("error")) {
                                                        estilo.add("error");
                                                    }
                                                }
                                            } else {
                                                mostrarDialogo("Falta el Nombre del Area");
                                                //Cambiar el color del textfield
                                                ObservableList<String> estilo = cbNombreArea.getStyleClass();
                                                if (!estilo.contains("error")) {
                                                    estilo.add("error");
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    mostrarDialogo("Seleccione un Tipo de Emisor/Destinatario");
                                }
                            } else {
                                mostrarDialogo("Falta la Fecha de Recepcion del Documento");
                                //Cambiar el color del textfield
                                ObservableList<String> estilo = dtpFechaRecepcion.getStyleClass();
                                if (!estilo.contains("error")) {
                                    estilo.add("error");
                                }
                            }
                        } else {
                            mostrarDialogo("Falta la Fecha de Emision del Documento");
                            //Cambiar el color del textfield
                            ObservableList<String> estilo = dtpFechaEmision.getStyleClass();
                            if (!estilo.contains("error")) {
                                estilo.add("error");
                            }
                        }
                    } else {
                        mostrarDialogo("Falta la Categoria del Documento");
                        //Cambiar el color del textfield
                        ObservableList<String> estilo = cbCategoriaDocumento.getStyleClass();
                        if (!estilo.contains("error")) {
                            estilo.add("error");
                        }
                    }
                } else {
                    mostrarDialogo("Falta la Ubicacion del Documento");
                    //Cambiar el color del textfield
                    ObservableList<String> estilo = txtUbicacionDocumento.getStyleClass();
                    if (!estilo.contains("error")) {
                        estilo.add("error");
                    }
                }
            } else {
                mostrarDialogo("Falta el Titulo del Documento");
                //Cambiar el color del textfield
                ObservableList<String> estilo = txtTituloDocumento.getStyleClass();
                if (!estilo.contains("error")) {
                    estilo.add("error");
                }
            }
        } else {
            mostrarDialogo("Falta el Numero del Documento");
            //Cambiar el color del textfield
            ObservableList<String> estilo = txtNroDocumento.getStyleClass();
            if (!estilo.contains("error")) {
                estilo.add("error");
            }

        }
    }

    //Metodo para cargar dialogos
    private void mostrarDialogo(String texto) {
        removerEstilo();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Faltan Datos");
        alert.setContentText(texto);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    //Metodo para remover el color rojo de los bordes
    private void removerEstilo() {
        //Lista de estilos por item
        List<ObservableList<String>> estilos = new ArrayList<>();
        ObservableList<String> estiloTxtNroDocumento = txtNroDocumento.getStyleClass();
        ObservableList<String> estiloTxtTituloDocuemento = txtTituloDocumento.getStyleClass();
        ObservableList<String> estiloTxtUbicacionDocumento = txtUbicacionDocumento.getStyleClass();
        ObservableList<String> estiloCbCategoria = cbCategoriaDocumento.getStyleClass();
        ObservableList<String> estiloDtpFechaEmision = dtpFechaEmision.getStyleClass();
        ObservableList<String> estiloDtpFechaRecepcion = dtpFechaRecepcion.getStyleClass();
        ObservableList<String> estiloTxtNombreIndividuo = txtNombreIndividuo.getStyleClass();
        ObservableList<String> estiloTxtApPaternoIndividuo = txtApPaternoIndividuo.getStyleClass();
        ObservableList<String> estiloTxtApMaternoIndividuo = txtApMaternoIndividuo.getStyleClass();
        ObservableList<String> estiloTxtCargoIndividuo = txtCargoIndividuo.getStyleClass();
        ObservableList<String> estiloCbNombreArea = cbNombreArea.getStyleClass();
        ObservableList<String> estiloTxtNombreResponsableArea = txtNombreResponsableArea.getStyleClass();
        ObservableList<String> estiloTxtApPaternoResponsableArea = txtApPaternoResponsableArea.getStyleClass();
        ObservableList<String> estiloTxtApMaternoResponsableArea = txtApMaternoResponsableArea.getStyleClass();

        //Llenar la lista
        estilos.add(estiloTxtNroDocumento);
        estilos.add(estiloTxtTituloDocuemento);
        estilos.add(estiloTxtUbicacionDocumento);
        estilos.add(estiloCbCategoria);
        estilos.add(estiloDtpFechaEmision);
        estilos.add(estiloDtpFechaRecepcion);
        estilos.add(estiloTxtNombreIndividuo);
        estilos.add(estiloTxtApPaternoIndividuo);
        estilos.add(estiloTxtApMaternoIndividuo);
        estilos.add(estiloTxtCargoIndividuo);
        estilos.add(estiloCbNombreArea);
        estilos.add(estiloTxtNombreResponsableArea);
        estilos.add(estiloTxtApPaternoResponsableArea);
        estilos.add(estiloTxtApMaternoResponsableArea);

        //Verificar la presencia del estilo "error" en cada una de las listas
        estilos.forEach((e) -> {
            if (e.contains("error")) {
                e.remove("error");
            }
        });
    }

    //Metodo para guardar el documento
    private void guardarDocumento(int tipoPersona) {
        //Crear documento
        Documento documento = new Documento();
        documento.setTituloDocumento(txtTituloDocumento.getText());
        documento.setNroDocumento(txtNroDocumento.getText());

        //Buscar y asignar la categoria
        em = ejbCategoria.getEntityManager();
        consulta
                = em.createNamedQuery("Categoria.findByDescripcion", Categoria.class
                ).setParameter("descripcion", cbCategoriaDocumento.getValue());
        Categoria categoriaSeleccionada = null;
        if (!consulta.getResultList().isEmpty()) {
            categoriaSeleccionada = (Categoria) consulta.getSingleResult();
            documento.setCategoria(categoriaSeleccionada);
        }

        //Si tiene un formato digital guradar el docuemento
        if (chkbFormatoDigital.isSelected()) {
            //Crear el nuevo directorio para guardar el documento dentro del proyecto
            File directorio = new File(categoriaSeleccionada.getDescripcion());
            //Verificar que el directorio no exista
            if (!directorio.exists()) {
                System.out.println("Creando directorio " + categoriaSeleccionada.getDescripcion());
                Boolean result = false;
                try {
                    directorio.mkdir();
                    result = true;
                } catch (SecurityException se) {
                }
                if (result) {
                    System.out.println("Directorio Creado");
                }
            }

            //Copiar el documento al directorio creado
            String nuevaDireccion = "";
            try {
                nuevaDireccion = copiarDocumento(categoriaSeleccionada.getDescripcion());
            } catch (IOException e) {
            }

            //Asignar la nueva direccion
            documento.setUrlDocumento(nuevaDireccion);
        }

        //Convertir las fechas del dtp en date y asignarlas al documento
        if (dtpFechaEmision.getValue() != null) {
            LocalDate localDate = dtpFechaEmision.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date fechaEmision = Date.from(instant);
            documento.setFechaEmision(fechaEmision);
        }

        if (dtpFechaRecepcion.getValue() != null) {
            LocalDate localDate = dtpFechaRecepcion.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date fechaRecepcion = Date.from(instant);
            documento.setFechaRecepcion(fechaRecepcion);
        }

        //Asignar ya sea un area o un individuo de acuerdo a la seleccion del radio button
        switch (tipoPersona) {
            case 1:
                //Crear el nuevo individuo
                Individuo individuo = new Individuo();
                individuo.setApMaterno(txtApMaternoIndividuo.getText());
                individuo.setApPaterno(txtApPaternoIndividuo.getText());
                individuo.setCargo(txtCargoIndividuo.getText());
                individuo.setNombreIndividuo(txtNombreIndividuo.getText());

                //Almacenar el individuo en la base de datos si es que no existe, sino editarlo
                em = ejbIndividuo.getEntityManager();
                consulta
                        = em.createNamedQuery("Individuo.findExiste", Individuo.class
                        ).setParameter("nombreIndividuo", individuo.getNombreIndividuo())
                                .setParameter("apPaterno", individuo.getApPaterno())
                                .setParameter("apMaterno", individuo.getApMaterno());
                List<Individuo> listaC = consulta.getResultList();
                if (listaC.isEmpty()) {
                    ejbIndividuo.create(individuo);
                } else {
                    try {
                        individuo.setIdIndividuo(((Individuo) consulta.getSingleResult()).getIdIndividuo());
                        ejbIndividuo.edit(individuo);

                    } catch (Exception ex) {
                        Logger.getLogger(RegistrarNuevoDocumentoController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Asignar al individuo al documento
                documento.setIndividuo((Individuo) consulta.getSingleResult());
                break;
            case 2:
                //Crear el nuevo area
                Areas area = new Areas();
                area.setApMaternoResponsable(txtApMaternoResponsableArea.getText());
                area.setApPaternoResponsable(txtApPaternoResponsableArea.getText());
                area.setDescripcion(cbNombreArea.getValue());
                area.setNombreResponsable(txtNombreResponsableArea.getText());

                //Almacenar el area en la base de datos si es que no existe
                em = ejbArea.getEntityManager();
                consulta
                        = em.createNamedQuery("Areas.findByDescripcion", Areas.class
                        ).setParameter("descripcion", area.getDescripcion());
                List<Areas> listaA = consulta.getResultList();
                if (listaA.isEmpty()) {
                    ejbArea.create(area);
                } else {
                    try {
                        area.setIdArea(((Areas) consulta.getSingleResult()).getIdArea());
                        ejbArea.edit(area);

                    } catch (Exception ex) {
                        Logger.getLogger(RegistrarNuevoDocumentoController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Asignar el area al documento
                documento.setArea((Areas) consulta.getSingleResult());
        }

        //Guardar el Documento en la base de datos
        ejbDocumento.create(documento);

        //Mostrar dialogo para crear observaciones
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrar Observaciones");
        alert.setHeaderText("Se pueden registrar observaciones");
        alert.setContentText("¿Desea Registrar Observaciones?");
        alert.initModality(Modality.APPLICATION_MODAL);
        ButtonType buttonTypeOne = new ButtonType("Si");
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            try {
                //Obtener el documento
                em = ejbDocumento.getEntityManager();
                consulta = em.createNamedQuery("Documento.findByNroDocumento", Documento.class).setParameter("nroDocumento", txtNroDocumento.getText());
                Documento doc = null;
                if (!consulta.getResultList().isEmpty()) {
                    doc = (Documento) consulta.getSingleResult();
                }
                //Crear el dialogo
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoMostrarObservaciones.fxml"));
                VBox page = (VBox) loader.load();
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Añadir Observacion");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner((Stage) btnGuardar.getScene().getWindow());
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                //Enviar el documento al dialogo
                DialogoMostrarObservacionesController controller = loader.getController();
                controller.setDoc(doc);
                dialogStage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(RegistrarNuevoDocumentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Mostrar dialogo de confirmacion
        Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
        alertConf.setTitle("Registrar Documento");
        alertConf.setContentText("Documento Registrado");
        alertConf.initModality(Modality.APPLICATION_MODAL);
        alertConf.showAndWait();
    }

    //Metodo para editar el documento
    private void editarDocumento() {
        Documento documento = documentoExistente;

        // Buscar y asignar la categoria
        em = ejbCategoria.getEntityManager();
        consulta
                = em.createNamedQuery("Categoria.findByDescripcion", Categoria.class
                ).setParameter("descripcion", cbCategoriaDocumento.getValue());
        Categoria categoriaSeleccionada = null;
        if (!consulta.getResultList().isEmpty()) {
            categoriaSeleccionada = (Categoria) consulta.getSingleResult();
            documento.setCategoria(categoriaSeleccionada);
        }

        //Convertir las fechas del dtp en date y asignarlas al documento
        if (dtpFechaEmision.getValue() != null) {
            LocalDate localDate = dtpFechaEmision.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date fechaEmision = Date.from(instant);
            documento.setFechaEmision(fechaEmision);
        }

        if (dtpFechaRecepcion.getValue() != null) {
            LocalDate localDate = dtpFechaRecepcion.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date fechaRecepcion = Date.from(instant);
            documento.setFechaRecepcion(fechaRecepcion);
        }

        //Si tiene un formato digital guradar el docuemento
        if (chkbFormatoDigital.isSelected()) {
            //Crear el nuevo directorio para guardar el documento dentro del proyecto
            File directorio = new File(categoriaSeleccionada.getDescripcion());
            //Verificar que el directorio no exista
            if (!directorio.exists()) {
                System.out.println("Creando directorio " + categoriaSeleccionada.getDescripcion());
                Boolean result = false;
                try {
                    directorio.mkdir();
                    result = true;
                } catch (SecurityException se) {
                }
                if (result) {
                    System.out.println("Directorio Creado");
                }
            }

            //Copiar el documento al directorio creado si es nuevo documento
            String nuevaDireccion;
            try {
                if (documentoExistente.getUrlDocumento() != null) {
                    //Eliminar el archivo
                    Path path = Paths.get(documento.getUrlDocumento());
                    try {
                        Files.delete(path);
                    } catch (NoSuchFileException x) {
                        System.err.format("%s: no such" + " file or directory%n", path);
                    } catch (DirectoryNotEmptyException x) {
                        System.err.format("%s not empty%n", path);
                    } catch (IOException x) {
                        // File permission problems are caught here.
                        System.err.println(x);
                    }
                }
                nuevaDireccion = copiarDocumento(categoriaSeleccionada.getDescripcion());
                //Asignar la nueva direccion
                documento.setUrlDocumento(nuevaDireccion);
            } catch (IOException e) {
            }

        }

        //Asignar ya sea un area o un individuo de acuerdo a la seleccion del radio button
        switch (((RadioButton) TipoPersona.getSelectedToggle()).getText()) {
            case "Individuo":
                //Crear el nuevo individuo
                Individuo individuo = new Individuo();
                individuo.setApMaterno(txtApMaternoIndividuo.getText());
                individuo.setApPaterno(txtApPaternoIndividuo.getText());
                individuo.setCargo(txtCargoIndividuo.getText());
                individuo.setNombreIndividuo(txtNombreIndividuo.getText());

                //Almacenar el individuo en la base de datos si es que no existe, sino editarlo
                em = ejbIndividuo.getEntityManager();
                consulta
                        = em.createNamedQuery("Individuo.findExiste", Individuo.class
                        ).setParameter("nombreIndividuo", individuo.getNombreIndividuo())
                                .setParameter("apPaterno", individuo.getApPaterno())
                                .setParameter("apMaterno", individuo.getApMaterno());
                List<Individuo> listaC = consulta.getResultList();
                if (listaC.isEmpty()) {
                    ejbIndividuo.create(individuo);
                } else {
                    try {
                        individuo.setIdIndividuo(((Individuo) consulta.getSingleResult()).getIdIndividuo());
                        ejbIndividuo.edit(individuo);

                    } catch (Exception ex) {
                        Logger.getLogger(RegistrarNuevoDocumentoController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Asignar al individuo al documento
                documento.setIndividuo((Individuo) consulta.getSingleResult());
                break;
            case "Area":
                //Crear el nuevo area
                Areas area = new Areas();
                area.setApMaternoResponsable(txtApMaternoResponsableArea.getText());
                area.setApPaternoResponsable(txtApPaternoResponsableArea.getText());
                area.setDescripcion(cbNombreArea.getValue());
                area.setNombreResponsable(txtNombreResponsableArea.getText());

                //Almacenar el area en la base de datos si es que no existe
                em = ejbArea.getEntityManager();
                consulta
                        = em.createNamedQuery("Areas.findByDescripcion", Areas.class
                        ).setParameter("descripcion", area.getDescripcion());
                List<Areas> listaA = consulta.getResultList();
                if (listaA.isEmpty()) {
                    ejbArea.create(area);
                } else {
                    try {
                        area.setIdArea(((Areas) consulta.getSingleResult()).getIdArea());
                        ejbArea.edit(area);

                    } catch (Exception ex) {
                        Logger.getLogger(RegistrarNuevoDocumentoController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Asignar el area al documento
                documento.setArea((Areas) consulta.getSingleResult());
        }

        try {
            //Editar el documento
            ejbDocumento.edit(documento);

        } catch (Exception ex) {
            Logger.getLogger(RegistrarNuevoDocumentoController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //Mostrar dialogo para crear observaciones
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrar Observaciones");
        alert.setHeaderText("Se pueden registrar observaciones");
        alert.setContentText("¿Desea Registrar Observaciones?");
        alert.initModality(Modality.APPLICATION_MODAL);
        ButtonType buttonTypeOne = new ButtonType("Si");
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            try {
                //Crear el dialogo
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RegistrosAreaController.class.getResource("/vista/DialogoMostrarObservaciones.fxml"));
                VBox page = (VBox) loader.load();
                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Añadir Observacion");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner((Stage) btnGuardar.getScene().getWindow());
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                //Enviar el documento al dialogo
                DialogoMostrarObservacionesController controller = loader.getController();
                controller.setDoc(documentoExistente);
                dialogStage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(RegistrarNuevoDocumentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Mostrar dialogo de confirmacion 
        Alert alertConf = new Alert(Alert.AlertType.INFORMATION);
        alertConf.setTitle("Editar Documento");
        alertConf.setContentText("Documento Editado");
        alertConf.initModality(Modality.APPLICATION_MODAL);
        alertConf.showAndWait();
    }

    //Metodo para copiar el documento
    private String copiarDocumento(String directorio) throws IOException {
        File documentoParaSubir = new File(txtUbicacionDocumento.getText());
        //Obtener Extension del Archivo
        String extension = FilenameUtils.getExtension(txtUbicacionDocumento.getText());
        //Impedir los caracteres no validos a la hora de ingresar un nombre de documento
        String nombreDocumento = txtNroDocumento.getText();
        String[] vNV = {"~", "#", "%", "&", "*", "{", "}", "'\'", ":", "<", ">", "¿", "?", "/", "+", "|", "\""};
        for (String string : vNV) {
            if (txtNroDocumento.getText().contains(string)) {
                StringBuilder sb = new StringBuilder(txtNroDocumento.getText());
                sb.setCharAt(txtNroDocumento.getText().indexOf(string), '-');
                nombreDocumento = sb.toString();
            }
        }

        File documentoSubido = new File(directorio + "/" + nombreDocumento + "." + extension);
        System.out.println(directorio + "/" + txtNroDocumento.getText() + "." + extension);
        FileChannel canalOrigen = null, canalDestino = null;
        try {
            canalOrigen = new FileInputStream(documentoParaSubir).getChannel();
            canalDestino = new FileOutputStream(documentoSubido).getChannel();
            canalDestino.transferFrom(canalOrigen, 0, canalOrigen.size());
        } finally {
            canalDestino.close();
            canalOrigen.close();
        }
        return directorio + "/" + txtNroDocumento.getText() + "." + extension;
    }

    @FXML
    private void accionCancelar(ActionEvent event) {
        limpiar();
    }

    //Metodo para limpiar
    private void limpiar() {
        //Limpiar todo los campos
        txtApMaternoIndividuo.clear();
        txtApPaternoIndividuo.clear();
        txtCargoIndividuo.clear();
        txtNombreIndividuo.clear();
        txtTituloDocumento.clear();
        txtUbicacionDocumento.clear();
        txtApMaternoResponsableArea.clear();
        txtApPaternoResponsableArea.clear();
        txtNombreResponsableArea.clear();
        txtNroDocumento.clear();
        cbNombreArea.setValue(null);
        chkbFormatoDigital.setSelected(false);
        try {
            cbCategoriaDocumento.setValue(null);
            dtpFechaEmision.setValue(null);
            dtpFechaRecepcion.setValue(null);
            TipoPersona.getSelectedToggle().setSelected(false);
        } catch (NullPointerException e) {
            System.out.println("Se limpio");
        }
        //Deshabilitar todo
        txtApMaternoResponsableArea.setDisable(true);
        txtApPaternoResponsableArea.setDisable(true);
        cbNombreArea.setDisable(true);
        txtNombreResponsableArea.setDisable(true);
        txtApMaternoIndividuo.setDisable(true);
        txtApPaternoIndividuo.setDisable(true);
        txtCargoIndividuo.setDisable(true);
        txtNombreIndividuo.setDisable(true);
        dtpFechaRecepcion.setDisable(true);
        //Devolver los valores a su valor original
        documentoExistente = new Documento();
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
