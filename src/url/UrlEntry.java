package url;

import helper.Loghandler;
import sql.InsertURL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class UrlEntry {

    // Protected fields
    protected String password;
    protected String url;
    protected static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    protected static final int BASE = ALPHABET.length();

    // Private fields
    private Pattern REGEX = Pattern.compile("(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");

    /**
     * Url Entry
     * @param pwd
     * @param url
     */
    public UrlEntry(String pwd, String url){
        this.url = url;
        this.password = pwd;
    }

    /**
     * Init
     *      Init check the validity of the URL
     *      If the URL is valid then it insert the data into the database
     *      Getting the shorten_url required us to have the ID of the link in the database
     * @return Boolean
     */
    public int init(){
        Boolean isValid = this.validateURL();

        if (!isValid)
           return -1;

        // Now we need to check whenever the url is in the database
        InsertURL insert = new InsertURL(this.url, 1);

        if(!insert.checkPresenceOfURL())
            return insert.insertData();
        else
            Loghandler.log("not insert", "info");

        return 0;
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
}
