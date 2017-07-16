package bean;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 15/05/2017.
 */
public class Userstate {

    // private fields
    private String username;
    private String token;
    private String mail;
    private String type;
    private int userID;

    /**
     * Constructor
     * @param username
     * @param token
     */
    public Userstate(String username, String token, int userID, String mail, String type){
        this.username = username;
        this.token = token;
        this.userID = userID;
        this.mail = mail;
        this.type = type;
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

    /**
     *
     * @return
     */
    public String getMail() {
        return this.mail;
    }

    /**
     *
     * @return
     */
    public String getType() { return this.type; }
}
