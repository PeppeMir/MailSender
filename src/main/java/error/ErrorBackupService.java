package error;

import java.io.IOException;

public interface ErrorBackupService {
    void start() throws IOException;

    void stop();

    void trace(String failureEmailAddress, Throwable cause);
}
