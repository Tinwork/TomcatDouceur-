package url;

import helper.Loghandler;
import sql.InsertURL;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;

/**
 * Created by lookitsmarc on 11/04/2017.
 */
public class Links {

    // This class represent what we have as a data in the database for a single link.
    // Use this class to operate a link
    private String orig_link;
    private String short_url;
    private int sql_id;
    private int count;
    private Date date;
    private long id;
    private long hashid;
    private static String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int BASE = ALPHABET.length();


    /**
     * Links constructor
     * @param original_url
     * @param short_url
     */
    public Links(String original_url, String short_url, int sql_id, int count, Date date, long hashid){
        this.short_url = short_url;
        this.orig_link = original_url;
        this.sql_id = sql_id;
        this.count = count;
        this.date = date;
        this.hashid = hashid;
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
     * @param shortID
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
     *          Finalize the creation of the Hash and the url and push it into the Datbase
     * @return
     */
    public String encodeLongURL(){
        long rand = 1 + (long) ((Math.random() * (Math.pow(62, 6) - 1)));
        this.id = rand;
        Loghandler.log("random value "+String.valueOf(rand), "info");

        String base62 = this.base10ToBase62(rand);

        Loghandler.log(base62, "info");
        return base62;
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
}
