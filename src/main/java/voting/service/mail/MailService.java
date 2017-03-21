package voting.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import voting.model.CountyRep;
import voting.utils.Formatter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by andrius on 3/11/17.
 */

@Service
public class MailService {

    @Value("${spring.mail.properties.mail.smtp.user}")
    private String user;

    @Value("${spring.mail.properties.mail.smtp.password}")
    private String smtpPassword;

    private final Authenticator authenticator;

    @Autowired
    public MailService(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void sendMail(CountyRep countyRep, String password) throws MessagingException {
        Properties props = System.getProperties();
        Session session = Session.getInstance(props, authenticator);
        session.setDebug(false);

        MimeMessage messageSender = new MimeMessage(session);
        String message = String.format("Jums sukurtas prisijungimas prie sistemos.\n\n" +
                "Prisijungimo vardas: %s\n" +
                "Prisijungimo slaptažodis: %s\n" +
                "Prisijungimui spauskite: %s",
                Formatter.formUsername(countyRep.getFirstName(), countyRep.getLastName()),
                password,
                "http://localhost:8080/#/prisijungti");
        messageSender.setText(message);
        messageSender.setSubject("Prisijungimas");
        messageSender.setFrom(new InternetAddress(user));
        messageSender.addRecipient(Message.RecipientType.TO, new InternetAddress(countyRep.getEmail()));

        Transport transport = session.getTransport("smtps");
        transport.connect("smtp.gmail.com", 465, user, smtpPassword);
        transport.sendMessage(messageSender, messageSender.getAllRecipients());
        transport.close();
    }
}
