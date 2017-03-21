package voting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by andrius on 3/12/17.
 */

@Component
public class SMTPAuthenticator extends Authenticator {

    @Value("${spring.mail.properties.mail.smtp.user}")
    private String user;

    @Value("${spring.mail.properties.mail.smtp.password}")
    private String password;

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }
}