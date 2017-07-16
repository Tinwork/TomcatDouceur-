package account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import helper.Loghandler;
import sun.security.provider.SHA;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 15/05/2017.
 */
public class Token implements TokenIface{

    protected final String ISSUER = "tinwork";
    protected DecodedJWT token;
    protected int id;
    protected final Date NOW = new Date();

    /**
     * Constructor
     */
    public Token(){}


    /**
     *
     * @return
     * @throws Exception
     */
    protected Algorithm GenerateAlg() throws Exception{
        Algorithm HMAC256;
        try {
            HMAC256 = Algorithm.HMAC256("tinwork-java-of-death");
        } catch (IOException e) {
            Loghandler.log(e.toString(), "fatal");
            throw new Exception(e);
        }

        return HMAC256;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String generateToken(){
        String token;

        try {
            Algorithm HMAC256 = GenerateAlg();
            Loghandler.log("beginning to generate the token", "info");
            token = JWT.create()
                    .withIssuer(this.ISSUER)
                    .withClaim("id", this.id)
                    .withExpiresAt(getNextHours())
                    .sign(HMAC256);
        } catch (Exception e) {
            Loghandler.log(e.toString(), "info");
            return null;
        }

        return token;
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     */
    public Boolean parseToken(String token){
        try {
            Algorithm HMAC256 = GenerateAlg();
            // Create an instance of the verifier
            JWTVerifier verifier = JWT.require(HMAC256)
                    .withIssuer(ISSUER)
                    .build();

            DecodedJWT jwt = verifier.verify(token);

            // check if the date is correct
            Date expire = jwt.getExpiresAt();

            if (!this.NOW.before(expire))
                return false;

            // Set the token
            this.token = jwt;
        } catch (JWTVerificationException e) {
            // If pass threw this then the token is the not valid
            Loghandler.log("token is not valid", "info");
            return false;
        } catch (Exception e) {
            Loghandler.log("exception token "+e.toString(), "info");
            return false;
        }

        return true;
    }

    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Date getNextHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        return calendar.getTime();
    }

    /**
     *
     * @return
     */
    public int getUserID(){
        return this.token.getClaim("id").asInt();
    }
}
