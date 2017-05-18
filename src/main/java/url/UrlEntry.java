package url;

import helper.Helper;
import helper.Loghandler;
import org.json.JSONObject;
import sql.InsertURL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class UrlEntry {

    // Protected fields
    protected String url;
    protected String pwd;
    protected String mail;
    protected Boolean captcha;
    protected JSONObject mulPwd;
    protected java.sql.Date start;
    protected java.sql.Date end;


    protected static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    protected static final int BASE = ALPHABET.length();
    private InsertURL insert;

    // Private fields
    private Pattern REGEX = Pattern.compile("(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");

    /**
     *
     * @param data
     */
    public UrlEntry(HashMap<String, String> data, HashMap<String, String> passwords){
        this.url = data.get("url");
        this.pwd = returnValidStrParam(data.get("password"));
        this.mail = returnValidStrParam(data.get("mail"));
        this.start = StringToSQLDate(data.get("start_date"));
        this.end = StringToSQLDate(data.get("end_date"));
        this.captcha = data.get("captcha") == null ? false : true;
        this.mulPwd = buildMulPwd(passwords);

    }

    /**
     * Init
     *      Init check the validity of the URL
     *      If the URL is valid then it insert the data into the database
     *      Getting the shorten_url required us to have the ID of the link in the database
     * @return Boolean
     */
    public boolean init() throws Exception{
        Boolean isValid = this.validateURL();

        if (!isValid)
            throw new Exception("the url is not valid");

        // Now we need to check whenever the url is in the database
        insert = new InsertURL(this.url, 1);

        if(!insert.checkPresenceOfURL())
            return false;
        else
            return true;
    }

    /**
     * Validate URL
     *          Check if the URL is valid
     * @return
     */
    private boolean validateURL(){
        Matcher matcher = REGEX.matcher(this.url);

        if (matcher.find() == false){
            Loghandler.log("URL is not a valid url", "warn");
            return false;
        }

        Loghandler.log("URL is valid", "info");

        return true;
    }

    /**
     *
     * @throws Exception
     */
    public void insertAction() throws Exception{
        int userID = 1;
        InsertURL insert = new InsertURL(this.url, userID);
        String hashpwd = LinkPwd.hash(this.pwd);

        // Steps are bit special for the mail. If the mail is not correct then we set the mail at null
        if (!Helper.validateMail(mail)) {
            mail = null;
        }


        // We're encoding the short URL based on a random number and the row id of the database
        int row = insert.insertOriginalURL(hashpwd, this.mail, this.start, this.end, this.captcha, this.mulPwd);

        // @TODO if i have time i shall pass the HashMap instead of the entire parse datas though...
        Links short_link = new Links(this.url, "", 0, 0, null, 0, null, null, null, null, null, null);
        String shortURL = short_link.encodeLongURL(row);
        long hash = short_link.getID();

        try {
            insert.insertShortLink(hash, shortURL, row);
        } catch(Exception e){
            Loghandler.log(e.toString(), "fatal");
            throw new Exception(e.toString());
        }
    }

    /**
     *
     * @param param
     * @return
     */
    public String returnValidStrParam(String param){
        if (param == null || param.isEmpty()) {
            return "";
        }

        return param;
    }

    /**
     *
     * @param date
     * @return
     */
    private java.sql.Date StringToSQLDate(String date){

        java.sql.Date sql = null;
        if (date == null)
            return sql;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


        try {
            Date parsed = format.parse(date);
            sql = new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            Loghandler.log(e.toString(), "warn");
        }

        return sql;
    }

    /**
     *
     * @param pwds
     * @return
     */
    final private JSONObject buildMulPwd(HashMap<String, String> pwds){
        JSONObject json = new JSONObject();

        int idx = 0;
        for (String pwd : pwds.keySet()){
            Loghandler.log("password "+pwds.get(pwd), "info");
            if (pwds.get(pwd) != null){
                json.put(pwd, pwds.get(pwd));
            }

            idx++;
        }

        return json;
    }

}
