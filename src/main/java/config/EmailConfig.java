package config;

import java.util.Properties;

public interface EmailConfig {
    String getSmtpAuth();

    String getSmtpStartTlsEnable();

    String getSmtpHost();

    String getSmtpPort();

    String getSmtpSslTrust();

    String getFrom();

    String getUsername();

    String getPassword();

    String getSubject();

    Properties getProperties();
}
