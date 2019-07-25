package error;

import com.google.common.util.concurrent.Service;
import config.AppConfig;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class ErrorBackupServiceImpl implements ErrorBackupService {

    private final AppConfig appConfig;

    private boolean started = false;
    private BufferedWriter bufferedWriter;

    @Inject
    public ErrorBackupServiceImpl(final AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void start() throws IOException {
        if (started) return;

        bufferedWriter = Files.newBufferedWriter(Paths.get(appConfig.getErrorDir(), System.currentTimeMillis() + ".txt"));
        started = true;
    }

    @Override
    public void stop()  {
        if (!started) return;

        if (bufferedWriter != null) {
            try {
                bufferedWriter.close(); // flush included
            } catch (IOException e) {
                // ignore
            }
        }

        started = false;
    }

    @Override
    public void trace(final String failureEmailAddress, final Throwable cause) {
        System.err.println("Error during sending message to " + failureEmailAddress + " :(((");
        System.err.println("Error: " + cause.getMessage());

        try {
            bufferedWriter.write(failureEmailAddress);
            bufferedWriter.newLine();
            //bufferedWriter.flush();
        } catch (final IOException e) {
            // TODO: log error
            // ignore, we are already backing up...
        }
    }
}
