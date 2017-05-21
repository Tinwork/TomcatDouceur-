package helper;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 17/05/2017.
 */
final public class Helper {

    private final static String secret = "6LfmAiIUAAAAAI1Q3PZPlFFm_Xp1fE9xDz-VmB7m";
    private final static Date now = new Date();

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
            switch (constraintList[i]) {
                case "mulPwd":
                        if (!Helper.containMulPwd(postDatas)) {
                            return false;
                        }
                    break;
                case "captcha":
                        if (postDatas.get("g-recaptcha-response") == null) {
                            return false;
                        }
                    break;
                case "start_date":
                    break;
                case "end_date":
                    break;
                default:
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

    /**
     *
     * @param captcha
     * @return
     */
    public final static Boolean checkRecaptcha(String captcha) {
        Boolean res = false;
        String params = "secret="+secret+"&response="+captcha;
        try {
            URL ws = new URL("https://www.google.com/recaptcha/api/siteverify");
            HttpURLConnection con = (HttpURLConnection) ws.openConnection();

            // Set the params to the request
            con.setRequestMethod("POST");

            // Send the request
            con.setDoOutput(true);
            DataOutputStream w = new DataOutputStream(con.getOutputStream());
            w.writeBytes(params);
            w.flush();
            w.close();

            // read the response
            int responseCode = con.getResponseCode();

            if (responseCode != 200) {
                Loghandler.log("not 202 : "+con.getResponseMessage(), "warn");
                return false;
            }

            JSONObject response = new JSONObject(readResponse(con));

            if (response.getBoolean("success")) {
                return true;
            }

        } catch(MalformedURLException e){
            Loghandler.log(e.toString(), "warn");
        } catch(IOException e) {
            Loghandler.log(e.toString(), "warn");
        }

        return false;
    }

    /**
     *
     * @param con
     * @return
     */
    private final static String readResponse(HttpURLConnection con){
        String res = "";

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            res = response.toString();
            Loghandler.log("response : "+response.toString(), "info");
        } catch (IOException e) {
            Loghandler.log(e.toString(), "warn");
        }

        return res;
    }

    /**
     *
     * @param startdate
     * @param enddate
     * @return
     */
    public static Boolean validateDate(Date startdate, Date enddate){
        if (now.equals(startdate) || now.equals(enddate))
            return true;
        else if (now.after(startdate) && now.before(enddate))
            return true;

        return false;
    }
}
