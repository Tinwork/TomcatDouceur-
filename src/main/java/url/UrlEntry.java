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
    protected String mail;
    protected Boolean captcha;
    protected JSONObject mulPwd;
    protected java.sql.Date start;
    protected java.sql.Date end;
    protected int userID;
    private InsertURL insert;

    HashMap<String, String> data;

    protected static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    protected static final int BASE = ALPHABET.length();



    /**
     *
     * @param data
     */
    public UrlEntry(HashMap<String, String> data, HashMap<String, String> passwords, int userID){
        this.start = StringToSQLDate(data.get("start_date"));
        this.end = StringToSQLDate(data.get("end_date"));
        this.mulPwd = buildMulPwd(passwords);
        this.captcha = data.get("captcha") == null ? false : true;
        this.data = data;
        this.userID = userID;
        this.mail = data.get("mail");
        this.url = data.get("url");
    }

    /**
     * Init
     *      Init check the validity of the URL
     *      If the URL is valid then it insert the data into the database
     *      Getting the shorten_url required us to have the ID of the link in the database
     * @return Boolean
     */
    public boolean init() throws Exception{
        Boolean isValid = Helper.validateURL(this.url);

        if (!isValid)
            throw new Exception("the url is not valid");

        // Now we need to check whenever the url is in the database
        insert = new InsertURL(this.url, this.userID);

        if(!insert.checkPresenceOfURL())
            return false;
        else
            return true;
    }

    /**
     *
     * @throws Exception
     */
    public void insertAction() throws Exception{
        InsertURL insert = new InsertURL(this.url, this.userID);
        Loghandler.log("broken again 1", "info");

        String hashpwd = LinkPwd.hash(this.data.get("password"));
        Loghandler.log("broken again", "info");


        // Steps are bit special for the mail. If the mail is not correct then we set the mail at null
        if (!Helper.validateMail(this.mail)) {
            this.mail = null;
        }

        // We're encoding the short URL based on a random number and the row id of the database
        int row = insert.insertOriginalURL(hashpwd, this.mail, this.start, this.end, this.captcha, this.mulPwd);

        Loghandler.log("broken after", "warn");

        // Create a new short link
        CreateLink short_link = new CreateLink(row);
        String shortURL = short_link.encodeLongURL();
        long hash = short_link.getShortURLHash();

        try {
            insert.insertShortLink(hash, shortURL, row);
        } catch(Exception e){
            Loghandler.log("trying to insert the url", "fatal");
            throw new Exception(e);
        }
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
                String hashes = LinkPwd.hash(pwds.get(pwd));
                json.put(pwd, hashes);
            }

            idx++;
        }

        return json;
    }

}
