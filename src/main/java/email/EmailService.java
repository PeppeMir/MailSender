package email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;

public interface EmailService {
    Session authenticate();

    Message createMessage(
            Session session,
            String localStudent,
            String exchangeStudent,
            String emailLocalStudent,
            String emailExchangeStudent,
            boolean isMentor) throws MessagingException, IOException;

    void sendMessage(Message message) throws MessagingException;
}
