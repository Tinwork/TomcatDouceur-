package login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import helper.Loghandler;

import java.io.IOException;

/**
 * Created by lookitsmarc on 15/05/2017.
 */
public class Token {

    private final String ISSUER = "tinwork";


    /**
     * Constructor
     */
    public Token(){
    }



    private Algorithm GenerateAlg() throws Exception{
        Algorithm HMAC256;
        Loghandler.log("generating the algo", "info");
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
                    .withIssuer(ISSUER)
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
}
