/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.vista;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class ManualUsuarioController implements Initializable {
    
    @FXML
    private TitledPane tpInicio;
    @FXML
    private Button btnUltimosRegistros;
    @FXML
    private TitledPane tpRegistrarDocumentos;
    @FXML
    private Button btnCrearDocumento;
    @FXML
    private Button btnEditarDocumento;
    @FXML
    private TitledPane tpDocumentos;
    @FXML
    private Button btnCategorias;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnAbrirDocumento;
    @FXML
    private Button btnEliminarDocumento;
    @FXML
    private TitledPane tpObservaciones;
    @FXML
    private Button btnVerObservaciones;
    @FXML
    private Button btnAnadirObservacion;
    @FXML
    private TitledPane tpRegistros;
    @FXML
    private Button btnEditarRegistro;
    @FXML
    private Button btnEliminarRegistro;
    @FXML
    private TextArea txtAyuda;
    @FXML
    private Button btnBusqueda;
    @FXML
    private Button btnDestinatarios;
    @FXML
    private TitledPane tpHerramientas;
    @FXML
    private Button btnCrearRespaldo;
    @FXML
    private Button btnImportarRespaldo;
    @FXML
    private Button btnExportarDatos;
    @FXML
    private Button btnImportarDatos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void accionUltimosRegistros(ActionEvent event) {
        txtAyuda.setText("-Se visualizan los ultimos 20 documentos que se han registrado en el sistema\n"
                + "-Boton VER:\n"
                + "	*Permite Abrir el archivo relacionado al documento registrado\n"
                + "	*Si no aparece un boton en la columna VER, es que el documento no tiene"
                + " ningun archivo asociado. \n"
                + "	*Para asociar un archivo, dirigirse a la pestaña:"
                + " \"Registrar Documento\", y editar el documento");
    }
    
    @FXML
    private void accionCrearDocumento(ActionEvent event) {
        txtAyuda.setText("Para Crear un Nuevo Documento\n"
                + "-Llenar todos los campos\n"
                + "-Si el Documento tiene una copia en formato digital y desea subirla al sistema presione el campo"
                + " \"Formato Digital\" y a continuacion use el boton \"Buscar\" para ubicar la direccion del documento\n"
                + "-Seleccione la Categoria del documento\n"
                + "	*Si la Categoria es \"Documentos Enviados\" los campos de \"Area\" se llenaran automaticamente"
                + " pero pueden ser modificados de ser necesario\n"
                + "	*Si la Categoria es \"Silabo\", el campo \"Cargo\" de \"Individuo\" sera puesto con el valor de"
                + " \"Docente\", pero puede ser modificado de ser necesario\n"
                + "	*Si la Categoria es \"Convalidacion\", el campo \"Cargo\" de \"Individuo\" sera puesto con el valor"
                + " de \"Alumno\", pero puede ser modificado de ser necesario\n"
                + "	*Si la Categoria es \"Plan de Estudios\", los campos de \"Area\" se llenaran automaticamente,"
                + " pero pueden ser modificados de ser necesario.\n"
                + "-Seleccione la Fecha de Emision.\n"
                + "-El campo de \"Fecha de Recepcion\" se llenara automaticamente con el valor de la Fecha de Emision, pero"
                + " puede ser modificado de ser necesario\n"
                + "-Si no se ha seleccionado alguno de los campos \"Area\" o \"Individuo\", seleccionar uno y llenar sus campos\n"
                + "-Presionar el boton \"Guardar\"\n"
                + "	*Si despues de presionar el boton \"Guardar\" alguno de los campos aparece en rojo, es que no se ha"
                + " llenado adecuadamente la informacion");
    }
    
    @FXML
    private void accionEditarDocumento(ActionEvent event) {
        txtAyuda.setText("Para Editar un Nuevo Documento\n"
                + "-Llenar el campo \"Numero de Documento\" con el numero del documento que se desea editar\n"
                + "-Si el sistema tiene registrado un documento con dicho numero, todos los campos correspondientes"
                + " se llenaran de acuerdo con los registros del documento.\n"
                + "-Modificar los campos de acuerdo a la necesidad\n"
                + "-Si el Documento tiene una copia en formato digital y desea subirla al sistema presione el campo"
                + " \"Formato Digital\" y a continuacion use el boton \"Buscar\" para ubicar la direccion del documento\n"
                + "-Al seleccionar la Categoria del documento\n"
                + "	*Si la Categoria es \"Documentos Enviados\" los campos de \"Area\" se llenaran automaticamente"
                + " pero pueden ser modificados de ser necesario\n"
                + "	*Si la Categoria es \"Silabo\", el campo \"Cargo\" de \"Individuo\" sera puesto con el valor de"
                + " \"Docente\", pero puede ser modificado de ser necesario\n"
                + "	*Si la Categoria es \"Convalidacion\", el campo \"Cargo\" de \"Individuo\" sera puesto con el valor"
                + " de \"Alumno\", pero puede ser modificado de ser necesario\n"
                + "	*Si la Categoria es \"Plan de Estudios\", los campos de \"Area\" se llenaran automaticamente,"
                + " pero pueden ser modificados de ser necesario.\n"
                + "-Presionar el boton \"Editar\"\n"
                + "	*Si despues de presionar el boton \"Editar\" alguno de los campos aparece en rojo, es que no se ha"
                + " llenado adecuadamente la informacion");
    }
    
    @FXML
    private void accionCategorias(ActionEvent event) {
        txtAyuda.setText("-Todos: Se muestran todos los documentos pertenecientes a dicha categoria\n"
                + "-Por Area: Se muestran los documentos pertenecientes a dicha categoria que hayan sido enviados por un area\n"
                + "-Por Individuo: Se muestran los documentos pertenecientes a dicha categoria que hayan sido enviados por un individuo");
    }
    
    @FXML
    private void accionReportes(ActionEvent event) {
        txtAyuda.setText("-Cada una de las pestañas de documentos cuenta con la opcion para generar un reporte.\n"
                + "-Un \"reporte\" es un archivo en formato .pdf que el usuario podra imprimir o guardar para su posterior uso\n"
                + "\n"
                + "-Para generar un reporte:\n"
                + "*Seleccionar la Fecha de Inicio (fecha de recepcion) desde la que se quiere obtener los registros\n"
                + "*El campo \"Fecha Final\" se llenara de forma automatica igualando al valor de \"Fecha de Incio\", pero se podra"
                + " modificar segun se necesita\n"
                + "*Presionar el boton \"Reporte\"\n"
                + "	-Si uno de los campos aparece en rojo, es que no se ha llenado correctamente\n"
                + "*Seleccionar la ruta donde se guardara el reporte\n"
                + "*El reporte se abrira automaticamente\n"
                + "	-Si el reporte aparece vacio, es que no existe registro alguno en el sistema de algun documento que haya"
                + " llegado entre las fecha solicitadas");
    }
    
    @FXML
    private void accionBusqueda(ActionEvent event) {
        txtAyuda.setText("-Cada listado de documentos cuenta con un motor de busqueda para agilizar la localizacion de los documentos.\n"
                + "-Se cuenta con 5 tipos de motor de busqueda\n"
                + "     *Por Numero del Documento\n"
                + "     *Por Asunto del Documento\n"
                + "     *Por Fecha de Emision\n"
                + "     *Por Fecha de Recepcion\n"
                + "     *Por Remitente\n"
                + "-Solo se puede llenar un campo a la vez. Los resultados se mostraran en la tabla en funcion de la consulta realizada.");
    }
    
    @FXML
    private void accionAbrirDocumento(ActionEvent event) {
        txtAyuda.setText("-Cada documento cuenta con un boton en la columna \"ver\" para abrir el archivo asociado al"
                + " documento en el sistema.\n"
                + "-Para ver el formato digital del documento, basta con hacer click sobre el boton en la columna \"Ver\"\n"
                + "     *Si no aparece boton alguno en la columna \"Ver\", significa que el documento no tiene archivo alguno relacionado. "
                + "Esto puede solucionarse asignando un archivo al documento en la pestaña "
                + "\"Registrar Documento\" usando la funcion de edicion");
    }
    
    @FXML
    private void accionEliminarDocumento(ActionEvent event) {
        txtAyuda.setText("-Cada documento cuenta con un boton en la columna \"Eliminar\" para eliminar el registro del documento en el"
                + " sistema\n"
                + "-Para eliminar el documento, basta con hacer click sobre el boton en la columna \"Eliminar\"\n"
                + "	*Al eliminar el documento, se eliminaran todos los registros de él, incluido el archivo al que hace"
                + " referencia (en caso lo tuviese)");
    }
    
    @FXML
    private void accionVerObservaciones(ActionEvent event) {
        txtAyuda.setText("-Cada documento cuenta con un listado de observaciones, las cuales puedan ser accedidas a traves del boton ubicado en la columna \"Observaciones\" en cada registro del documento.\n"
                + "-Se mostrara una ventana con el listado de todas las obseraciones que el documento tiene registradas.\n"
                + "-Las observaciones se dividen en 2 grupos:\n"
                + "     *Informacion\n"
                + "     *Salida\n"
                + "-Se pueden editar los registros a traves del boton en la columna \"Editar\".\n"
                + "-Se pueden eliminar los registros a traves del boton en la columna \"Eliminar\".");
    }
    
    @FXML
    private void accionAnadirObservacion(ActionEvent event) {
        txtAyuda.setText("-Para añadir una nueva observacion se requiere estar en la ventana de \"Ver Observaciones\" y hacer clic en el icono de \"+\" ubicado en la parte superior.\n"
                + "-Esto nos enviara a una nueva ventana donde podremos llenar 2 campos para crear la observacion.\n"
                + "     *Informacion\n"
                + "     *Salida\n"
                + "-Basta con llenar uno solo para que se pueda registrar la observacion.");
    }
    
    @FXML
    private void accionEditarRegistro(ActionEvent event) {
        txtAyuda.setText("-En las pestañas de \"Registros\" podemos gestionar los registros de los Indiviudos y Areas que han mandado documentos para su registro en el sistema.\n"
                + "-Para Editar un registro basta con hacer clic en el boton en la columna \"Editar\", lo cual nos mandara a la pestaña de \"Editar Registro\" donde podremos editar los campos de acuerdo a nuestra necesidad.\n"
                + "-Una vez se hayan modificado los cambios se hace clic en el boton \"Guardar\"");
    }
    
    @FXML
    private void accionEliminarRegistro(ActionEvent event) {
        txtAyuda.setText("-En las pestañas de \"Registros\" podemos gestionar los registros de los Indiviudos y Areas que han mandado documentos para su registro en el sistema.\n"
                + "-Para Elimnar un registro basta con hacer clic en el boton en la columna \"Eliminar\". Se mostrara una ventana para confirmar si queremos eliminar el registro\n"
                + "	*OJO: SI SE ELIMINA UN REGISTRO, SE ELIMINARAN TODOS LOS DOCUMENTOS RELACIONADOS A EL.");
    }
    
    @FXML
    private void accionDestinatarios(ActionEvent event) {
        txtAyuda.setText("-Los destinatarios son las personas que cuentan con acceso al sistema. La gestion de los mismos (edicion y eliminacion) se realiza como con los demas registros (Individuos y Areas).\n"
                + "-Para crear un nuevo destinatario debe hacer clic en el boton de \"Añadir Nuevo Destinatario\", el cual nos llevara a una nueva ventana:\n"
                + "	*Llenar todos los campos\n"
                + "	*Presionar el boton \"Aceptar\"\n"
                + "	*OJO: si un campo aparece en rojo es que falta informacion");
    }
    
    @FXML
    private void accionCrearRespaldo(ActionEvent event) {
        txtAyuda.setText("-Los Respaldos son carpetas donde se guarda la informacion del sistema en caso se sufra algun desperfecto y se requiera recuperar la informacion.\n"
                + "-Para crear un respaldo basta con ir a la pestaña \"Herramientas\" y hacer clic en la opcion \"Crear Respaldo\"\n"
                + "	*Se abrira una ventana para elegir el destino donde guardaremos nuestro respaldo\n"
                + "	*Una vez elegido el sitio donde se guardara, presionar \"Aceptar\"\n"
                + "-El respaldo se guardara en la carpeta elegida bajo el nombre \"backup-'fecha cuando se realizo el respaldo'\"");
    }
    
    @FXML
    private void accionImportarRespaldo(ActionEvent event) {
        txtAyuda.setText("-Los Respaldos son carpetas donde se guarda la informacion del sistema en caso se sufra algun desperfecto y se requiera recuperar la informacion.\n"
                + "-Para importar un respaldo basta con ir a la pestaña \"Herramientas\" y hacer clic en la opcion \"Importar Respaldo\"\n"
                + "	*Se abrira una ventana para elegir el destino donde esta almacenado nuestro resplado \n"
                + "	*Seleccionar la carpeta donde esta nuestro respaldo (bajo el nombre \"backup-'fecha cuando se realizo el respaldo'\")\n"
                + "	*Presionar \"Aceptar\"\n"
                + "-Despues de que aparezca una ventana confirmando la restauracion del respaldo, ingresar nuevamente a cualquiera de las pestañas. La informacion debera estar cargada y disponible para su uso");
    }
    
    @FXML
    private void accionExportarDatos(ActionEvent event) {
        txtAyuda.setText("-La exportacion de datos sirve para compartir la informacion entre 2 computadoras que cuenten con el Sistema de Gestion Documentaria de la E.P. de Finanzas\n"
                + "-Para exportar los datos (registros y documentos almacenados en el sistema)\n"
                + "	*Hacer clic en la pestaña \"Herramientas\"\n"
                + "	*Hacer clic en la opcion \"Exportar Datos\"\n"
                + "	*Seleccionar la carpeta donde se guardaran los datos\n"
                + "-La carpeta se creara en direccion escogida bajo el nombre \"Datos SGDF 'Fecha de Hoy'\"");
    }
    
    @FXML
    private void accionImportarDatos(ActionEvent event) {
        txtAyuda.setText("-La importacion de datos sirve para compartir la informacion entre 2 computadoras que cuenten con el Sistema de Gestion Documentaria de la E.P. de Finanzas\n"
                + "-Para importar los datos (registros y documentos almacenados en el sistema)\n"
                + "	*Hacer clic en la pestaña \"Herramientas\"\n"
                + "	*Hacer clic en la opcion \"Exportar Datos\"\n"
                + "	*Seleccionar la carpeta donde se encuentran los datos previamente exportados. La carpeta estara creada bajo el nombre \"Datos SGDF 'Fecha de la Exportacion'\"");
    }
    
}
