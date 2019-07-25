package config;

import mailsender.Main;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

public class EmailConfigImpl implements EmailConfig {

    private static final String PROPERTIES_FILE_NAME = "email.properties";
    private final Properties properties = new Properties();

    @Inject
    public EmailConfigImpl() throws IOException {
        this.properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
        // FIXME: default values
    }

    @Override
    public String getSmtpAuth() {
        return properties.getProperty("mail.smtp.auth");
    }

    @Override
    public String getSmtpStartTlsEnable() {
        return properties.getProperty("mail.smtp.starttls.enable");
    }

    @Override
    public String getSmtpHost() {
        return properties.getProperty("mail.smtp.host");
    }

    @Override
    public String getSmtpPort() {
        return properties.getProperty("mail.smtp.port");
    }

    @Override
    public String getSmtpSslTrust() {
        return properties.getProperty("mail.smtp.ssl.trust");
    }

    @Override
    public String getFrom() {
        return properties.getProperty("mail.from");
    }

    @Override
    public String getUsername() {
        return properties.getProperty("mail.username");
    }

    @Override
    public String getPassword() {
        return properties.getProperty("mail.password");
    }

    @Override
    public String getSubject() {
        return properties.getProperty("mail.subject");
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
