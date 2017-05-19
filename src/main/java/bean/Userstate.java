package bean;

/**
 * Created by lookitsmarc on 15/05/2017.
 */
public class Userstate {
    private String username;
    private String token;
    private int userID;

    /**
     * Constructor
     * @param username
     * @param token
     */
    public Userstate(String username, String token, int userID){
        this.username = username;
        this.token = token;
        this.userID = userID;
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

    /**
     *
     * @return
     */
    public int getUserID() {
        return userID;
    }

}
