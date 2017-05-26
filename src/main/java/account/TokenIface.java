package account;

import java.util.Date;

/**
 * Created by lookitsmarc on 20/05/2017.
 */
public interface TokenIface {
    /**
     *
     * @return
     */
    public String generateToken();

    /**
     *
     * @param token
     * @return
     */
    public Boolean parseToken(String token);

    /**
     *
     * @return
     */
    public Date getNextHours();

}