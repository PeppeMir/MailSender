package email;

import config.AppConfig;
import config.EmailConfig;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    private EmailConfig emailConfig;
    private AppConfig appConfig;

    @Inject
    public EmailServiceImpl(
            final EmailConfig emailConfig,
            final AppConfig appConfig) {
        this.emailConfig = emailConfig;
        this.appConfig = appConfig;
    }

    @Override
    public Session authenticate() {
        final Properties emailProps = emailConfig.getProperties();
        return Session.getInstance(emailProps, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getUsername(), emailConfig.getPassword());
            }
        });
    }

    @Override
    public Message createMessage(
            final Session session,
            final String localStudent,
            final String exchangeStudent,
            final String emailLocalStudent,
            final String emailExchangeStudent,
            final boolean isMentor) throws MessagingException, IOException {

        final Message message = new MimeMessage(session);

        message.setSubject(emailConfig.getSubject());
        message.setFrom(new InternetAddress(emailConfig.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(isMentor ? emailLocalStudent : emailExchangeStudent));

        // Create a multipart message
        final Multipart multipart = new MimeMultipart();

        // HTML part
        String htmlBodyFromFile = isMentor
                ? readFile(appConfig.getContentDir() + "\\htmlMentor.html", Charset.defaultCharset())
                : readFile(appConfig.getContentDir() + "\\htmlBuddy.html", Charset.defaultCharset());

        htmlBodyFromFile = htmlBodyFromFile.replace("LOCAL_STUDENT_NAME", localStudent + " (" + emailLocalStudent + ")");
        htmlBodyFromFile = htmlBodyFromFile.replace("EXCHANGE_STUDENT_NAME", exchangeStudent + " (" + emailExchangeStudent + ")");

        BodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(htmlBodyFromFile, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlBodyPart);

        // second part (the main image)
        BodyPart htmlBodyImg1 = new MimeBodyPart();
        DataSource fds = new FileDataSource(appConfig.getContentDir() + "\\main.png");
        htmlBodyImg1.setDataHandler(new DataHandler(fds));
        htmlBodyImg1.setFileName("mnimg.png");
        htmlBodyImg1.setHeader("Content-ID", "<mnimg>");
        multipart.addBodyPart(htmlBodyImg1);

        // third part (the body image)
        BodyPart htmlBodyImg2 = new MimeBodyPart();
        DataSource fds2 = new FileDataSource(appConfig.getContentDir() + "\\after.png");
        htmlBodyImg2.setDataHandler(new DataHandler(fds2));
        htmlBodyImg2.setFileName("bdimg.png");
        htmlBodyImg2.setHeader("Content-ID", "<bdimg>");
        multipart.addBodyPart(htmlBodyImg2);

        // Send the complete message parts
        message.setContent(multipart);

        return message;
    }

    @Override
    public void sendMessage(final Message message) throws MessagingException {
        Transport.send(message);
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
