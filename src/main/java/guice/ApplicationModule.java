package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import config.AppConfig;
import config.AppConfigImpl;
import config.EmailConfig;
import config.EmailConfigImpl;
import email.EmailService;
import email.EmailServiceImpl;
import error.ErrorBackupService;
import error.ErrorBackupServiceImpl;
import parsing.CSVParsingService;
import parsing.CSVParsingServiceImpl;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        // configs
        bind(AppConfig.class).to(AppConfigImpl.class).in(Scopes.SINGLETON);
        bind(EmailConfig.class).to(EmailConfigImpl.class).in(Scopes.SINGLETON);

        // services
        bind(ErrorBackupService.class).to(ErrorBackupServiceImpl.class).in(Scopes.SINGLETON);
        bind(EmailService.class).to(EmailServiceImpl.class).in(Scopes.SINGLETON);
        bind(CSVParsingService.class).to(CSVParsingServiceImpl.class).in(Scopes.SINGLETON);
    }
}
