package login;

import helper.Loghandler;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class Password {

    private String pwd;
    private final SecureRandom random = new SecureRandom();
    private byte[] salt;
    final private int LENGTH = 256;
    final private int ITERATION = 5;

    /**
     * Constructor
     * @param pwd
     */
    public Password(String pwd){
        this.pwd = pwd;
    }

    //////////////////////////////// ENCRYPT ////////////////////////////////

    /**
     * Generate Random Salt
     * @return
     */
    public byte[] generateRandomSalt(){
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        this.salt = salt;

        return salt;
    }

    /**
     * Hash
     * @param pwd
     * @param salt
     * @return
     */
    public byte[] hash(final char[] pwd, final byte[] salt) {
        byte[] res = null;

        try {
            SecretKeyFactory secret = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(pwd, salt, ITERATION, LENGTH);
            SecretKey skey = secret.generateSecret(spec);

            // Save the key into a []byte
            res = skey.getEncoded();

        } catch (NoSuchAlgorithmException e) {
            Loghandler.log("NoSuchAlgorithmException "+e.toString(), "fatal");
        } catch (InvalidKeySpecException e) {
            Loghandler.log("InvalidKeySpecException "+e.toString(), "fatal");
        }

        return res;
    }

    /**
     * Encrypt
     *      Encapsulize the process of encrypting a password
     * @return
     * @throws Exception
     */
    public String encrypt(){
        String hashes = null;
        char[] pwd = this.pwd.toCharArray();
        byte[] salt = generateRandomSalt();

        // Generate hash
        byte[] pwdhash = hash(pwd, salt);

        hashes = DatatypeConverter.printBase64Binary(pwdhash);

        return hashes;
    }

    /**
     *
     * @return
     */
    public String getSalt() throws Exception{

        String strslat = DatatypeConverter.printBase64Binary(this.salt);
        Loghandler.log("before sending into db Salt "+Arrays.toString(this.salt), "info");

        return strslat;
    }

    //////////////////////////////// DECRYPT ////////////////////////////////

    /**
     *
     * @param pwd
     * @param dbSalt
     * @param dbHash
     * @return
     */
    public boolean compareHash(final char[] pwd, final byte[] dbSalt, final byte[] dbHash) {
        byte[] inputhash = hash(pwd, dbSalt);

        return Arrays.equals(inputhash, dbHash);
    }
}
