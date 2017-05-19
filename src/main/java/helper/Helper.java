package helper;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;

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

    /**
     * Check Constraint Emptyness
     *      Make sure that the value enter are not empty
     * @param constraintList
     * @param postDatas
     * @return
     */
    public static Boolean checkConstraintEmptyness(String[] constraintList, HashMap<String, String> postDatas){
        for (int i = 0; i < constraintList.length - 1; i++) {
            Loghandler.log("constraint "+constraintList[i], "info");
            if (postDatas.get(constraintList[i]) == null) {
                return false;
            }

            if (postDatas.get(constraintList[i]).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
