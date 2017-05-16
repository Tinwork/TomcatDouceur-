package helper;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Created by lookitsmarc on 17/05/2017.
 */
final public class Helper {

    /**
     * Validate Mail
     * @param mail
     * @return
     */
    public static Boolean validateMail(String mail){
        if (mail.isEmpty() || mail == null)
            return false;

        EmailValidator validator = EmailValidator.getInstance();

        return validator.isValid(mail);
    }
}
