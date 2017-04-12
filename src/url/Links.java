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
    private String short_link;
    private Date create_date;
    private int id;
    private int count;
    private static String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int BASE = ALPHABET.length();
    private SecureRandom random = new SecureRandom();


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
     * Generate ID
     *          Generate a long id
     * @return
     */
    public String generateID(){
        return new BigInteger(12, random).toString(7);
    }


    /**
     * Encodes a number to Base62 string
     *
     * @return Encoded number
     */
    public String encode() {
        if (this.id == 0) {
            return ALPHABET.substring(0, 1);
        }

        StringBuilder code = new StringBuilder(16);

        while (this.id > 0) {
            int remainder = this.id % BASE;
            this.id /= BASE;

            code.append(ALPHABET.charAt(remainder));
        }

        return code.reverse().toString();
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
        String base62 = this.encode();
        String hashes = this.generateID();

        String id = hashes.concat(base62);

        return id;
    }
}
