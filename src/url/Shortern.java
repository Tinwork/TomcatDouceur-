package url;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class Shortern {
    protected String password;
    public static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    public static final int BASE = ALPHABET.length();

    public Shortern(String pwd){
        this.password = pwd;
    }

    /**
     * Encode
     * @param num Int
     * @return
     */
    public String encode(int num) {
        StringBuilder str = new StringBuilder();
        while (num > 0) {
            str.insert(0, ALPHABET.charAt(num % BASE));
            num = num / BASE;
        }
        return str.toString();
    }

    /**
     * Decode
     * @param str String
     * @return
     */
    public int decode(String str) {
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            num = num * BASE + ALPHABET.indexOf(str.charAt(i));
        }
        return num;
    }
}
