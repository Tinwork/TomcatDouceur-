package url;

import helper.Loghandler;

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
    private String short_link;
    private Date create_date;
    private int id;
    private int count;
    private final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private SecureRandom random = new SecureRandom();
    private String characters;


    /**
     * Links constructor
     * @param original_url
     * @param short_url
     * @param id
     * @param count
     */
    public Links(String original_url, String short_url, int id, int count, Date date){
        this.orig_link = original_url;
        this.short_link = short_url;
        this.create_date = date;
        this.id = id;
        this.count = count;
    }

    /**
     * Encode Long Url
     *
     */
    public void encodeLongURL(){
        Loghandler.log("encode long url", "info");
        String base62 = this.base62SqlRow();
        String randHash = this.generateID();

        Loghandler.log("randhash "+String.valueOf(randHash), "info");
        Loghandler.log("base62 "+base62, "info");
    }

    public String generateID(){
        return new BigInteger(130, random).toString(7);
    }

    /**
     * Base 62 Sql Row
     *          Encode a sql row to a base62 value
     *          Based on the dukky/Base62 Converter
     */
    public String base62SqlRow(){
        int id = Integer.valueOf(Integer.toString(this.id, 10), 10);
        Loghandler.log("convertion en cours id "+id, "info");

        if (this.id < 0) {
            throw new IllegalArgumentException("id must be nonnegative");
        }

        String ret = "";
        while (id > 0) {
            ret = BASE62[((int) this.id % 62)] + ret;
            id /= 62;
          //  Loghandler.log("ret: "+ret, "info");
        }

        return ret;
    }

    /**
     * Decode Short Url
     */
    public void decodeShortURl(){

    }
}
