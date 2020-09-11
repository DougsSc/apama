package utilidades;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class SendMail {

    static final Logger logger = LogManager.getLogger(SendMail.class.getName());

    public boolean sendMail(final ConfiguracaoEmail conf, String assunto, String msg, String emailDestinatario, String emailComCopiaOculta, String emailReposta, HashMap anexos) throws Exception {
        if (conf.getTpAmb() != 1) {
            emailDestinatario = Utilidades.getVariavel("mail.destinatarios");
            emailComCopiaOculta = Utilidades.getVariavel("mail.destinatarios.cco");
            assunto = "Homologação " + assunto;
            System.out.println("Enviando e-mail homologação para " + emailDestinatario + " cco: " + emailComCopiaOculta);
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", conf.getSmtp());
        prop.put("mail.smtp.auth", conf.isRequerAutenticacao());
        prop.put("mail.debug", "true");
        prop.put("mail.smtp.debug", "true");
        prop.put("mail.mime.charset", "ISO-8859-1");
        prop.put("mail.smtp.port", conf.getPorta());

        prop.put("mail.smtp.socketFactory.port", conf.getPorta());
        prop.put("mail.smtp.socketFactory.fallback", "false");
        prop.put("mail.smtp.ssl.enable", conf.isSsl());
        if (conf.isSsl()) {
            prop.put("mail.smtp.ssl.trust", "*");
            prop.put("mail.smtp.ssl.checkserveridentity", "false");
            prop.put("mail.smtp.starttls.enable", "true");
        }
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException ex) {
            logger.error("Erro SendMail.sendMail()");
            logger.catching(Level.ERROR, ex);
        }

        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(conf.getLogin(), conf.getSenha());
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress(conf.getLogin()));
        message.setSubject(assunto);

        Address a = new InternetAddress(conf.getLogin(), conf.getLogin());
        message.setFrom(a);
        if (emailReposta != null && !emailReposta.isEmpty()) {
            message.setReplyTo(new InternetAddress[]{new InternetAddress(emailReposta)});
        }
        if (emailDestinatario.indexOf(',') > 0) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
        } else {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailDestinatario));
        }
        //envia copia oculta
        if (emailComCopiaOculta != null && !emailComCopiaOculta.isEmpty()) {
            message.setRecipient(Message.RecipientType.BCC, new InternetAddress(emailComCopiaOculta));
        }
        MimeMultipart mpRoot = new MimeMultipart("mixed");
        MimeMultipart mpContent = new MimeMultipart("alternative");
        MimeBodyPart contentPartRoot = new MimeBodyPart();
        contentPartRoot.setContent(mpContent);
        mpRoot.addBodyPart(contentPartRoot);

        //enviando html
        MimeBodyPart mbp2 = new MimeBodyPart();
        mbp2.setContent(msg, "text/html");
        mbp2.setHeader("Content-Type", "text/HTML; charset=ISO-8859-1; format=flowed");
        mbp2.setHeader("X-Accept-Language", "pt-br, pt");
        mpContent.addBodyPart(mbp2);

        if ((anexos != null) && (anexos.size() > 0)) {
            Set<String> chaves = anexos.keySet();
            for (String chave : chaves) {
                //enviando anexo
                MimeBodyPart mbp3 = new MimeBodyPart();
                FileDataSource fds = new FileDataSource("" + anexos.get(chave));
                if (fds.getFile().exists()) {
                    mbp3.setDisposition(Part.ATTACHMENT);
                    mbp3.setDataHandler(new DataHandler(fds));
                    mbp3.setFileName(fds.getName());

                    mpRoot.addBodyPart(mbp3);
                } else {
                    System.out.println("Erro ao anexar: " + anexos.get(chave));
                }
            }
        }
        message.setContent(mpRoot);
        message.saveChanges();
        Transport transport = session.getTransport("smtp");
        transport.connect(conf.getSmtp(), conf.getLogin(), conf.getSenha());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return true;
    }
}
