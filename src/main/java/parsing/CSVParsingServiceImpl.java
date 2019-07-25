package parsing;

import config.AppConfig;
import email.EmailService;
import error.ErrorBackupService;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Session;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CSVParsingServiceImpl implements CSVParsingService {

    private ErrorBackupService errorBackupService;
    private AppConfig appConfig;
    private EmailService emailService;

    @Inject
    public CSVParsingServiceImpl(
            final ErrorBackupService errorBackupService,
            final AppConfig appConfig,
            final EmailService emailService) {
        this.errorBackupService = errorBackupService;
        this.appConfig = appConfig;
        this.emailService = emailService;
    }

    @Override
    public void parse() {

        try {
            errorBackupService.start();
        } catch (IOException e) {
            System.err.println("Error during creating backup file. Exiting ...");
            System.exit(-1);
        }

        final Session session = emailService.authenticate();

        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(appConfig.getInputDir() + "\\people.csv"), StandardCharsets.UTF_8))) {

            if (appConfig.isSkipFirstLine()) {
                br.readLine();
            }

            String line;
            while ((line = br.readLine()) != null) {
                final String[] readLine = line.split(appConfig.getSplitChar());
                final String localStudent = readLine[0];
                final String emailLocalStudent = readLine[1];
                final String exchangeStudent = readLine[2];
                final String emailExchangeStudent = readLine[3];

                System.out.println("Processing (" + localStudent + "," + emailLocalStudent + "," + exchangeStudent + "," + emailExchangeStudent + ")");

                final Message message1 = emailService.createMessage(session, localStudent, exchangeStudent, emailLocalStudent, emailExchangeStudent, true);
                final Message message2 = emailService.createMessage(session, localStudent, exchangeStudent, emailLocalStudent, emailExchangeStudent, false);

                try {
                    emailService.sendMessage(message1);
                    System.out.println("Message to " + emailLocalStudent + " sent successfully!!! :D :D");
                } catch (Exception e) {
                    errorBackupService.trace(emailLocalStudent, e);
                }

                try {
                    emailService.sendMessage(message2);
                    System.out.println("Message to " + emailExchangeStudent + " sent successfully!!! :D :D");
                } catch (Exception e) {
                    errorBackupService.trace(emailExchangeStudent, e);
                }

                System.out.println();
            }

            System.out.println("Process done! :D");
        } catch (final Exception e) {
            e.printStackTrace();
            // FIXME
        } finally {
            errorBackupService.stop();
        }
    }
}
