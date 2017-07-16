package account;

import helper.Loghandler;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 *  on 19/05/2017.
 */
public class Mailer {

    private String mail;
    private String username;
    private final String sender = "tinworkfiestar@gmail.com";
    private final String host = "mail";
    private Session session;
    private Validity token;
    private Context initCtx;
    private Context envCtx;

    /**
     * Constructor
     * @param mail
     * @param username
     */
    public Mailer(String mail, String username){
        this.mail = mail;
        this.username = username;
    }

    /**
     * Init
     */
    public void init(){
        try {
            this.initCtx = new InitialContext();
            this.envCtx = (Context) this.initCtx.lookup("java:comp/env");
            this.session = (Session) this.envCtx.lookup("mail/Session");
        } catch (NamingException e) {
            Loghandler.log("naming exception "+e, "warn");
        }
    }

    /**
     * Send Mail
     */
    public void sendMail(){
        this.token = new Validity(this.username);
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", this.host);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.mail));

            // Set subject header
            message.setSubject("Activate your tinwork account");

            // Set message
            message.setContent("<p>In order to active this account click on the following Url</p><a href='https://83a55f57.ngrok.io/tinwork/register?token="+this.token.generateToken()+"'>Validate your account</a>", "text/html");

            // Send message
            Transport.send(message);
            Loghandler.log("send mail", "info");
        }catch (MessagingException mex) {
            Loghandler.log("mail exception "+mex.toString(), "warn");
        }
    }
}
