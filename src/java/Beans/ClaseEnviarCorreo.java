
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Utils.Constants;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

/**
 *
 * @author Rocio
 */
public class ClaseEnviarCorreo {

    private MimeMultipart multipart = new MimeMultipart("related");
    private String origen = Constants.userM;
    private String contrasenia = Constants.passM;
    private String correo;
    private String asunto = "";
    private String cuerpo;
    private String error = "correcto";

    public void sendFromMail(ServletContext d) throws Exception {
        addContent(formato());
        addImagen(d);
        Session session = Session.getDefaultInstance(propiedades());
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(origen));
            InternetAddress[] toAddress = new InternetAddress[1];

            toAddress[0] = new InternetAddress(correo);

            message.addRecipient(Message.RecipientType.TO, toAddress[0]);

            message.setSubject(asunto);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(Constants.MAIL_HOST, origen, contrasenia);
            try {

                transport.sendMessage(message, message.getAllRecipients());

                transport.close();
            } catch (MessagingException ex) {
                error = ex.getMessage();
                System.out.println(ex.getCause());
            }

        } catch (MessagingException me) {
            me.printStackTrace();
            error = me.getMessage();
            System.out.println(me.getMessage());
        }

    }

    public void sendMail(ServletContext d) throws Exception {
        addContent(formato());
        addImagen(d);
        addPDF(d);
        Session session = Session.getDefaultInstance(propiedades());
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(origen));
            InternetAddress[] toAddress = new InternetAddress[1];

            toAddress[0] = new InternetAddress(correo);

            message.addRecipient(Message.RecipientType.TO, toAddress[0]);

            message.setSubject(asunto);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(Constants.MAIL_HOST, origen, contrasenia);
            try {

                transport.sendMessage(message, message.getAllRecipients());

                transport.close();
            } catch (MessagingException ex) {
                error = ex.getMessage();
                System.out.println(ex.getCause());
            }

        } catch (MessagingException me) {
            me.printStackTrace();
            error = me.getMessage();
            System.out.println(me.getMessage());
        }
    }

    public Properties propiedades() {
        Properties props = System.getProperties();
        props.setProperty("mail.mime.charset", "ISO-8859-1");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Constants.MAIL_HOST);
        props.put("mail.smtp.user", origen);
        props.put("mail.smtp.password", contrasenia);
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.localhost", "sia.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", Constants.MAIL_HOST);
        props.put("mail.smtp.ehlo", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.auth", "true");
        return props;
    }

    public String formato() throws Exception {

        String content;

        String cabecera = "<html>\n"
                + "    <head>\n"
                + "      \n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <header style=\"position: relative;left:80px;\">\n"
                + "        <img src=\"cid:cidcabecera\">\n"
                + "        <pre style=\"font-family:'calibri'; font-size: 16px;\">\n"
                + "            Instituto Tecnológico de Toluca\n"
                + "\n"
                + "        </pre>\n"
                + "    </header>\n"
                + "    <body >";

        String pie = " </body>\n"
                + "    <footer  style=\"position: relative;\" >\n"
                + "         <img src=\"cid:cidpie\">\n"
                + "    </footer>\n"
                + "</html>";
        content = String.format("%s%s%s%s%s", cabecera, "<br/>", cuerpo, "<br/>", pie);

        return content;
    }

    public void addImagen(ServletContext d) throws Exception {
        String url_cab = "/Imagenes/header_correo.png";
        String cab_img = d.getRealPath(url_cab);
        String url_pie = "/Imagenes/footer_correo.png";
        String pie_img = d.getRealPath(url_pie);

        addCID("cidcabecera", cab_img);
        addCID("cidpie", pie_img);
    }

    public void addPDF(ServletContext context) throws Exception {
        String url_cab = "/PDF/Manual-CENEVAL.pdf";
        String cab_img = context.getRealPath(url_cab);
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource(cab_img);
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setFileName("Manual-CENEVAL.pdf");
        this.multipart.addBodyPart(messageBodyPart);

    }

    public void addContent(String htmlText) throws Exception {
        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlText, "text/html");
        // add it
        this.multipart.addBodyPart(messageBodyPart);
    }

    public void addCID(String cidname, String pathname) throws Exception {
        DataSource fds = new FileDataSource(pathname);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<" + cidname + ">");
        this.multipart.addBodyPart(messageBodyPart);
    }

    /**
     * @return the origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * @return the contrasenia
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @param contrasenia the contrasenia to set
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * @param asunto the asunto to set
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * @return the cuerpo
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * @param cuerpo the cuerpo to set
     */
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
