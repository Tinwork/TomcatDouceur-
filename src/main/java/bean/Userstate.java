package bean;

/**
 * Created by lookitsmarc on 15/05/2017.
 */
public class Userstate {
    private String username;
    private String token;

    /**
     * Constructor
     * @param username
     * @param token
     */
    public Userstate(String username, String token){
        this.username = username;
        this.token = token;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
