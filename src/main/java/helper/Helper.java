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
        // Though there might be a better way to handle the multiple password
        for (int i = 0; i < constraintList.length - 1; i++) {
            Loghandler.log("constraint "+constraintList[i], "info");
            if (constraintList[i] == "mulPwd") {
                if (!Helper.containMulPwd(postDatas))
                    return false;
            } else {
                if (postDatas.get(constraintList[i]) == null) {
                    return false;
                }

                if (postDatas.get(constraintList[i]).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     *
     * @param postDatas
     * @return
     */
    private static Boolean containMulPwd(HashMap<String, String> postDatas){
        // Not the best way though... we should pass by some javascript instead of this
        Boolean ispresent = false;
        String[] optskey = {"passwords-1", "passwords-2", "passwords-3"};

        // We ensure that at least one of the passwords is present within the postDatas
        int i = 0;
        while(i < optskey.length && !ispresent) {
            if (postDatas.containsKey(optskey[i])) {
                ispresent = true;
            }

            i++;
        }

        return ispresent;
    }

}
