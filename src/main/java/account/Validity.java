package account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import helper.Loghandler;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lookitsmarc on 20/05/2017.
 */
public class Validity extends Token implements TokenIface {

    private String username;

    /**
     * Constructor
     */
    public Validity(String username){
        super();
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String generateToken(){
        String token;

        try {
            Algorithm HMAC256 = super.GenerateAlg();

            token = JWT.create()
                    .withIssuer(super.ISSUER)
                    .withClaim("username", this.username)
                    .withIssuedAt(super.NOW)
                    .withExpiresAt(this.getNextHours())
                    .sign(HMAC256);
        } catch (Exception e) {
            Loghandler.log(e.toString(), "info");
            return null;
        }

        return token;
    }

    /**
     *
     * @return
     */
    public String getPayload() {
        Claim claim = super.token.getClaim("username");
        Loghandler.log("payload "+claim.asString(), "info");

        if (claim == null) {
            return null;
        }

        // Now we need to check if the date is still valid..
        Date expiresAt = super.token.getExpiresAt();
        if (!expiresAt.after(super.NOW)) {
            return null;
        }

        return claim.asString();
    }

    /**
     * Return the next hours Date.now + 1 hour
     * @return
     */
    public Date getNextHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(super.NOW);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        return calendar.getTime();
    }
}
