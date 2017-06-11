package account;

import helper.Helper;
import helper.Loghandler;
import sql.UserDB;

import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 10/06/2017.
 */
public class UserFactory {

    // protected field
    protected String username;
    protected String pwd;
    protected String mail;
    protected String type;

    // private field
    private UserDB user;
    private Password pwdFactory;

    /**
     *
     * @param usrData
     */
    public UserFactory(HashMap<String, String> usrData) {
        this.username = usrData.get("username");
        this.pwd = usrData.get("password");
        this.mail = usrData.get("mail");
        this.type = usrData.get("type");

        this.user = new UserDB();
    }

    /**
     * Init Process
     * @return
     */
    public Boolean doLoginProcess() {
        if (this.isEmpty())
            return false;

        // Retrieve
        byte[][] userDBDatas = this.user.selectPwd(this.username);

        if (userDBDatas == null)
            return false;

        this.pwdFactory = new Password(null);
        // Compare the current password input with the one coming from the DB
        Boolean issame = this.pwdFactory.compareHash(this.pwd.toCharArray(), userDBDatas[0], userDBDatas[1]);

        if (issame)
            return true;
        else
            return false;
    }

    /**
     *
     * @return
     */
    public boolean registerProcess() throws Exception{
        Boolean isUserExist = this.user.userExist(this.username, this.mail);

        if (this.mail == null || this.username == null || this.pwd == null) {
            throw new Exception("one of the argument is empty");
        }

        if (isUserExist)
            throw new Exception("user already exist");

        // Validate the mail
        if (!Helper.validateMail(this.mail))
            throw new Exception("Invalid mail");

        this.pwdFactory = new Password(this.pwd);
        // Otherwise insert the user
        Boolean insertStatus = this.encryptUser();

        return insertStatus;
    }

    /**
     *
     * @return
     */
    private Boolean encryptUser(){

        Boolean isInsert = false;

        try {
            String hash = this.pwdFactory.encrypt();
            String salt = this.pwdFactory.getSalt();
            isInsert = this.user.insertUser(this.username, hash, salt, this.mail, this.type);
        } catch(Exception e) {
            Loghandler.log("encrypt user "+e.toString(), "fatal");
        }

        return isInsert;
    }

    /**
     *
     * @return
     */
    private Boolean isEmpty() {
        if (this.username == null || this.pwd == null)
            return true;

        if (this.username.isEmpty() || this.pwd.isEmpty())
            return true;

        return false;
    }

    /**
     *
     * @return
     */
    public int getUserID() {
        return this.user.selectUserID(this.username);
    }

    /**
     *
     * @return
     */
    public String getMail() {
        return this.user.selectUserExtraInfo(this.username)[0];
    }

    /**
     *
     * @return
     */
    public String getType() {
        return this.user.selectUserExtraInfo(this.username)[1];
    }
}
