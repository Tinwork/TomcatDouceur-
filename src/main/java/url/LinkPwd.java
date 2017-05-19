package url;

import helper.Loghandler;
import sun.rmi.log.LogHandler;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Created by lookitsmarc on 16/05/2017.
 */
public class LinkPwd {

    final static private String salt = "TINWORK-CO.LTD,AMD2018";
    final static private int ITERATION = 2;
    final static private int LENGTH = 64;


    public void Hashpwd(){ }

    /**
     * Hash pwd
     * @return
     */
    public static String hash(String password) {

        if (password.isEmpty() || password == null){
            return null;
        }

        byte[] pwd = null;
        byte[] bsalt = DatatypeConverter.parseBase64Binary(salt);

        try {
            SecretKeyFactory secret = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), bsalt, ITERATION, LENGTH);
            SecretKey skey = secret.generateSecret(spec);

            // Save the key into a []byte
            pwd = skey.getEncoded();

        } catch (NoSuchAlgorithmException e) {
            Loghandler.log("NoSuchAlgorithmException "+e.toString(), "fatal");
        } catch (InvalidKeySpecException e) {
            Loghandler.log("InvalidKeySpecException "+e.toString(), "fatal");
        }

        return DatatypeConverter.printBase64Binary(pwd);
    }

    /**
     *
     * @param dbpwd
     * @param actualpwd
     * @return
     */
    public static Boolean checkValidity(String dbpwd, String actualpwd){
        if (dbpwd.isEmpty() || dbpwd == null)
            return null;

        if (actualpwd.isEmpty() || actualpwd == null)
            return null;

        // Get a byte array of the password
        byte[] dbPwd = DatatypeConverter.parseBase64Binary(dbpwd);
        // In order to test we're going to call the hash method where we're going to generate a new hash with the actual input password
        String pwd = LinkPwd.hash(actualpwd);
        byte[] actualHash = DatatypeConverter.parseBase64Binary(pwd);

        return Arrays.equals(dbPwd, actualHash);
    }
}
