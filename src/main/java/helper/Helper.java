package helper;

import account.Token;
import bean.Userstate;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lookitsmarc on 17/05/2017.
 */
final public class Helper {

    // Private fields
    private final static String secret = "6LfmAiIUAAAAAI1Q3PZPlFFm_Xp1fE9xDz-VmB7m";

    // Regex
    private final static Pattern regexURL = Pattern.compile("(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");

    // Other private fields
    private final static Date now = new Date();
    private static int userid;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Validate Mail
     * @param mail
     * @return
     */
    public static Boolean validateMail(String mail){
        if (mail == null)
            return false;

        if (mail.isEmpty()) {
            return false;
        }

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

    /**
     *
     * @param req
     * @param res
     * @return
     */
    public static Boolean processRequest(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res){
        Object bean = req.getSession().getAttribute("userstate");

        if (bean == null)
            return false;

        Userstate userstate  = (Userstate) bean;
        userid = userstate.getUserID();

        if (!tokenValidity(userstate.getToken()))
            return false;

        return true;
    }


    /**
     *
     * @param token
     * @return
     */
    public static Boolean tokenValidity(String token) {
        Token tokenize = new Token();
        Boolean isValid = tokenize.parseToken(token);
        userid = tokenize.getUserID();

        return isValid;
    }


    /**
     *
     * @return
     */
    public static int retrieveUserID(){
       return userid;
    }

    /**
     *
     * @param url
     * @return
     */
    public static Boolean validateURL(String url) {
        Matcher matcher = regexURL.matcher(url);

        if (url == null)
            return false;


        if (matcher.find() == false){
            Loghandler.log("URL is not a valid url", "warn");
            return false;
        }

        Loghandler.log("after regex", "warn");

        Loghandler.log("URL is valid", "info");
        return true;
    }

    /**
     *
     * @param date
     * @return
     */
    public static java.sql.Date StrToSQLDate(String date) {
        java.sql.Date datec = null;

        if (date == null)
            return datec;

        if (date.isEmpty())
            return datec;

        try {
            Date parsed = format.parse(date);
            datec = new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            Loghandler.log(e.toString(), "warn");
        }

        return datec;
    }
}
