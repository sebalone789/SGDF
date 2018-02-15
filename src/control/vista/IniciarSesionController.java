/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.vista;

import beans.DestinatariosJpaController;
import beans.DocumentoJpaController;
import control.ConnectionUtils;
import control.GestorDeDocumentos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import modelo.Destinatarios;
import modelo.Documento;
import org.apache.commons.io.FilenameUtils;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class IniciarSesionController implements Initializable {

    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblContrasena;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;

    //Valores Para La Insercion de Datos
    final EntityManagerFactory emf = GestorDeDocumentos.emf;
    Boolean existe = false;
    Query consulta;
    EntityManager em;

    //Ejb para enviar los correos
    private final DestinatariosJpaController ejbDestinatario = new DestinatariosJpaController(emf);
    private final DocumentoJpaController ejbDocumento = new DocumentoJpaController(emf);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Cargar el icono
        cargarIconos("/img/login-icon.png", btnIniciarSesion);
    }

    @FXML
    private void accionIniciarSesion(ActionEvent event) {
        iniciarSesion();
    }

    //Metodo para iniciar sesion
    public void iniciarSesion() {
        // Verificar si el login es valido
        if (txtUsuario.getText() != null && txtContrasena.getText() != null) {
            if ("SGDFinanzas".equals(txtUsuario.getText()) && "Finanz62".equals(txtContrasena.getText())) {
                cargarIncio();
            } else {
                //Verificar si existe un registro con esos datos en la base de datos
                em = ejbDestinatario.getEntityManager();
                consulta = em.createNamedQuery("Destinatarios.findForLogin", Destinatarios.class)
                        .setParameter("contrasena", txtContrasena.getText())
                        .setParameter("email", txtUsuario.getText());
                if (!consulta.getResultList().isEmpty()) {
                    cargarIncio();
                } else {
                    //Mostrar dialogo de confirmacion 
                    Alert alertConf = new Alert(Alert.AlertType.ERROR);
                    alertConf.setTitle("Inicio Fallido");
                    alertConf.setContentText("Datos Incorrectos");
                    alertConf.initModality(Modality.APPLICATION_MODAL);
                    alertConf.showAndWait();
                    txtContrasena.clear();
                    txtUsuario.clear();
                }
            }
        } else {
            //Mostrar dialogo de confirmacion 
            Alert alertConf = new Alert(Alert.AlertType.ERROR);
            alertConf.setTitle("Inicio Fallido");
            alertConf.setContentText("Ingresar Usuario y Contraseña");
            alertConf.initModality(Modality.APPLICATION_MODAL);
            alertConf.showAndWait();
            txtContrasena.clear();
            txtUsuario.clear();
        }
    }

    //Metodo para cargar el inicio
    private void cargarIncio() {
        try {
            //hacer el backup y mandar el correo si es posible
            backupDiario().run();

            //Mostrar la pantalla principal
            Parent root = FXMLLoader.load(getClass().getResource("/vista/PantallaPrincipal.fxml"));

            Scene scene = new Scene(root);

            //Pantalla Completa
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            scene.getStylesheets().add(getClass().getResource("/vista/Estilos.css").toExternalForm());

            //Titulo de la aplicacion
            stage.setTitle("Sistema de Gestion Documentaria de la E.P. de Finanzas");

            //Icono de la aplicacion
            stage.getIcons().add(new Image("/assets/finanzasMini.png"));

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Task backupDiario() {

        //Hacer un backup 
        Task<Void> task;
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String dir = "backups";
                File directorio = new File(dir);
                //crear el directorio si no existe
                if (!directorio.exists()) {
                    Boolean result = false;
                    try {
                        directorio.mkdir();
                        result = true;
                    } catch (SecurityException se) {
                    }
                }
                //Formatter para date
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String path = dir + "\\backup-" + df.format(new Date());
                //Crear el backup
                backupDB(path);

                //Enviar correo a todos los destinatarios deseados con el backup
                List<Destinatarios> lista = ejbDestinatario.findDestinatariosEntities();
                if (!lista.isEmpty()) {
                    lista.forEach((l) -> {
                        enviarCorreo(l.getEmail(), l.getContrasena(), l.getEmail(), path);
                    });
                } else {
                    Destinatarios dest = new Destinatarios();
                    dest.setApMaterno("Caparo");
                    dest.setApPaterno("Suyo");
                    dest.setContrasena("Finanz62");
                    dest.setEmail("asuyo@uandina.edu.pe");
                    dest.setNombre("Aquiles");
                    //Crear nuevo registro
                    ejbDestinatario.create(dest);
                    enviarCorreo(dest.getEmail(), dest.getContrasena(), dest.getEmail(), path);
                }

                return null;
            }
        };
        return task;
    }

    //Metodo para crear el respaldo
    //Metodo para crear el respaldo
    public boolean backupDB(String path) {
        try {
            //Crear el backup de la base de datos
            Connection conn = ConnectionUtils.getConnection();
            try (CallableStatement cs = conn.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)")) {
                cs.setString(1, path);
                cs.execute();
                //Copiar los documentos
                List<Documento> documentosRespaldo = ejbDocumento.findDocumentoEntities();
                documentosRespaldo.forEach((d) -> {
                    //Crear el nuevo directorio para guardar el documento dentro del proyecto
                    File directorio = new File(path + "\\" + d.getCategoria().getDescripcion());
                    //Verificar que el directorio no exista
                    if (!directorio.exists()) {
                        Boolean result = false;
                        try {
                            directorio.mkdir();
                            result = true;
                        } catch (SecurityException se) {
                        }
                    }

                    //Copiar el documento al directorio creado
                    File f = new File(d.getUrlDocumento());
                    //Obtener Extension del Archivo
                    String extension = FilenameUtils.getExtension(f.getAbsolutePath());
                    String denominacion = d.getNroDocumento() + "." + extension;
                    if (f.exists()) {
                        try {
                            copiarDocumento(path + "\\" + d.getCategoria().getDescripcion(), f.getAbsolutePath(), denominacion);
                        } catch (IOException e) {
                        }
                    }
                });
                return true;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MenuPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //Metodo para enviar el archivo del backup en el correo
    private void enviarCorreo(String dirFuente, String contrasena, String dirDestino, String path) {
        // Recipient's email ID needs to be mentioned.
        String to = dirDestino;

        // Sender's email ID needs to be mentioned
        String from = dirFuente;

        final String username = dirFuente;//change accordingly
        final String password = contrasena;//change accordingly

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.user", username);
        props.put("mail.password", password);

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            message.setSubject("Respaldo Dia " + df.format(new Date()));

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Respaldo del Sistema de Gestion Documentaria de la E.P. de Finanzas.\n"
                    + "Generado el dia " + df.format(new Date()) + " por el usuario " + txtUsuario.getText());

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = path;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
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

    //Metodo para copiar el documento para el respaldo
    private void copiarDocumento(String directorioDestino, String fuente, String denominacion) throws IOException {
        File documentoParaSubir = new File(fuente);
        File documentoSubido = new File(directorioDestino + "\\" + denominacion);
        FileChannel canalOrigen = null, canalDestino = null;
        try {
            canalOrigen = new FileInputStream(documentoParaSubir).getChannel();
            canalDestino = new FileOutputStream(documentoSubido).getChannel();
            canalDestino.transferFrom(canalOrigen, 0, canalOrigen.size());
        } finally {
            canalOrigen.close();
            canalDestino.close();
        }
    }
}
