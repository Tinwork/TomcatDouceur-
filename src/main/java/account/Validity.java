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

    private final Date date = new Date();
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
                    .withIssuedAt(this.date)
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
        if (!expiresAt.after(this.date)) {
            return null;
        }

        return claim.asString();
    }

    /**
     * Return the next hours Date.now + 1 hour
     * @return
     */
    private Date getNextHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        return calendar.getTime();
    }
}
