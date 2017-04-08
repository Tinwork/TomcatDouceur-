package url;

import com.sun.org.apache.xpath.internal.operations.Bool;
import helper.Loghandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class Shortern {

    protected String password;
    protected String url;
    public static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    public static final int BASE = ALPHABET.length();
    private Pattern REGEX = Pattern.compile("_^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?$_iuS\n");
    final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();


    public Shortern(String pwd, String url){
        this.url = url;
        this.password = pwd;
    }

    public Boolean init(){
        Boolean isValid = this.validateURL();

        if (!isValid)
            return false;

        int base62 = this.base62FromStr();
        String base62str = this.base62FromInt(base62);

        helper.Loghandler.log(" base 62 from str "+Integer.toString(base62), "info");
        helper.Loghandler.log("base 62 from int " +Integer.toString(base62), "info");

        return true;
    }

    /**
     * Validate URL
     *          Check if the URL is valid
     * @return
     */
    private boolean validateURL(){
        Matcher matcher = REGEX.matcher(this.url);
        if (!matcher.find()){
            Loghandler.log("URL is not a valid url", "warn");
            return false;
        }

        return true;
    }


    /**
     * Encode Base 62
     *      Based on this gist : https://gist.github.com/jdcrensh/4670128#file-base62-java-L13
     */
    private int base62FromStr(){
        int result = 0;
        int power = 1;
        for (int i = this.url.length() - 1; i >= 0; i--) {
            int digit = this.url.charAt(i) - 48;
            if (digit > 42) {
                digit -= 13;
            } else if (digit > 9) {
                digit -= 7;
            }
            result += digit * power;
            power *= 62;
        }
        return result;
    }

    /**
     * Decode Base62
     * @param numb
     * @return
     */
    public String base62FromInt(int numb){
        final StringBuilder sb = new StringBuilder(1);
        do {
            sb.insert(0, BASE62[numb % 62]);
            numb /= 62;
        } while (numb > 0);
        return sb.toString();
    }

}
