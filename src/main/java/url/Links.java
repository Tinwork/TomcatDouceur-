package url;

import com.sun.org.apache.xpath.internal.operations.Bool;
import helper.Loghandler;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import sql.InsertURL;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lookitsmarc on 11/04/2017.
 */
public class Links {

    // This class represent what we have as a data in the database for a single link.
    // Use this class to operate a link
    private String orig_link;
    private String short_url;
    private String password;
    private String mail;
    private String mulPwd;

    private int sql_id;
    private int count;

    private Date date;
    private Date start_date;
    private Date end_date;

    private Boolean captcha;
    private long id;
    private long hashid;
    private static String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int BASE = ALPHABET.length();


    /**
     * Links constructor
     * @param original_url
     * @param short_url
     */
    public Links(String original_url, String short_url, int sql_id, Date date, long hashid, String password, String mulPwd, Boolean captcha, String mail, Date start_date, Date end_date){
        this.short_url = short_url;
        this.orig_link = original_url;
        this.sql_id = sql_id;
        this.date = date;
        this.hashid = hashid;
        this.password = password;
        this.captcha = captcha;
        this.mail = mail;
        this.start_date = start_date;
        this.end_date = end_date;
        this.mulPwd = mulPwd;
    }

    /**
     *
     * @param uniqID
     * @return
     */
    public String base10ToBase62(long uniqID){
        StringBuilder sb = new StringBuilder();

        if (uniqID == 0)
            return "a";

        while (uniqID > 0)
            uniqID = generateBase62(sb, uniqID);

        return sb.reverse().toString();
    }

    /**
     *
     * @param sb
     * @param currentNumb
     * @return
     */
    protected long generateBase62(final StringBuilder sb, long currentNumb){
        long remaining = currentNumb % BASE;
        sb.append(ALPHABET.charAt((int) remaining));

        return currentNumb / BASE;
    }


    /**
     *
     * @return
     */
    protected long toBase10(){
        char[] chars = new StringBuilder(this.short_url).reverse().toString().toCharArray();
        long n = 0;
        for (int idx = chars.length - 1; idx >= 0; idx--)
            n += CalculateBase10(ALPHABET.indexOf(chars[idx]), idx);

        return n;
    }

    /**
     *
     * @param idx
     * @param pow
     * @return
     */
    protected long CalculateBase10(long idx, long pow){
        return idx * (long) Math.pow(BASE, pow);
    }


    /**
     * Decodes a Base62 string
     *
     * @param code Code to decode
     * @return Decoded code
     */
    public int decode(String code) {
        int number = 0;

        for (int i = 0; i < code.length(); i++) {
            int power = code.length() - (i + 1);
            number += ALPHABET.indexOf(code.charAt(i)) * Math.pow(BASE, power);
        }

        return number;
    }

    /**
     * Finalize Short URL
     *          Finalize the creation of the Hash and the UrlAPI and push it into the Datbase
     * @return
     */
    public String encodeLongURL(int row){
        long rand = 1 + (long) ((Math.random() * (Math.pow(62, 6) - 1)));

        // Now we need to concat the value of the random number with the value of the row
        this.id = concatLong(row, rand);
        Loghandler.log("random value "+String.valueOf(this.id), "info");

        String base62 = this.base10ToBase62(this.id);

        Loghandler.log(base62, "info");
        return base62;
    }

    /**
     *
     * @param row
     * @param rand
     * @return
     */
    public Long concatLong(int row, long rand) {
        long concat = Long.valueOf(String.valueOf(rand) + String.valueOf(row));

        return concat;
    }

    /**
     * Get Original URL
     * @return orig_link
     */
    public String getOriginalURL(){
        Loghandler.log("trying to get the original link", "info");
        return this.orig_link;
    }

    /**
     *
     * @return
     */
    public HashMap<String, Boolean> getConstrain(){
        HashMap<String, Boolean> constrain = new HashMap<String, Boolean>();

        constrain.put("password", this.password == null ? false : true);
        constrain.put("captcha", this.captcha == null || !this.captcha ? false : true);
        constrain.put("mail", this.mail == null ? false : true);
        constrain.put("start_date", this.start_date == null ? false : true);
        constrain.put("end_date", this.end_date == null ? false : true);
        constrain.put("mulPwd", this.mulPwd == null ? false : true);

        return constrain;
    }

    /**
     * getID
     * @return
     */
    public long getID(){
        return this.id;
    }

    /**
     * getHashID
     * @return
     */
    public long getHashID(){
        return this.hashid;
    }

    /**
     *
     * @return
     */
    public JSONObject getMulPwd(){
        JSONObject json = new JSONObject(this.mulPwd);

        return json;
    }

    /**
     *
     * @return
     */
    public int getMulPwdLength(){
        JSONObject json = new JSONObject(this.mulPwd);
        return json.length();
    }

    /**
     *
     * @return
     */
    public int getSQLID(){ return this.sql_id; };

    /**
     *
     * @return
     */
    public String getPassword(){
        return this.password;
    }

    /**
     *
     * @return
     */
    public String getMail(){
        return this.mail;
    }

    /**
     *
     * @return
     */
    public Boolean getCaptcha(){
        return this.captcha;
    }

    /**
     *
     * @return
     */
    public Date getStart_date(){
        return this.start_date;
    }

    /**
     *
     * @return
     */
    public Date getEnd_date(){
        return this.end_date;
    }

    /**
     *
     * @param constraint
     * @param inputValue
     * @return
     * @throws Exception
     */
    public Boolean checkParamIntegrety(String constraint, String inputValue) throws Exception{
        Boolean validity = false;
        switch (constraint) {
            case "password":
                validity = LinkPwd.checkValidity(this.password, inputValue);
                break;
            case "mail":
                validity = this.mail.equals(inputValue);
                break;
            case "captcha":
                validity = this.captcha;
                break;
            default:
                throw new Exception(constraint+" is not supported");
        }

        return validity;
    }

    /**
     * Check Multiple Password Integrit
     * @param postDatas
     * @return
     */
    public Boolean checkMulPwdIntegrity(HashMap<String, String> postDatas){
       // We're going to check the value of the password input by the user and the password saved into the database
        Boolean validity = false;
       JSONObject mulPwd = new JSONObject(this.mulPwd);
       Iterator<String> keys = mulPwd.keys();

       while (keys.hasNext()) {
           String key = keys.next();
           String dbpwd = mulPwd.getString(key);

           validity = LinkPwd.checkValidity(dbpwd, postDatas.get(key));
       }

       return validity;
    }

}
