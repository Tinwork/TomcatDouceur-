package account;

import helper.Loghandler;
import sql.UserDB;

import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 10/06/2017.
 */
public class UserFactory {

    protected String username;
    protected String pwd;
    private UserDB user;
    private Password pwdFactory;

    /**
     *
     * @param usrData
     */
    public UserFactory(HashMap<String, String> usrData) {
        this.username = usrData.get("username");
        this.pwd = usrData.get("password");
        this.user = new UserDB();
        this.pwdFactory = new Password(null);
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

        // Compare the current password input with the one coming from the DB
        Boolean issame = this.pwdFactory.compareHash(this.pwd.toCharArray(), userDBDatas[0], userDBDatas[1]);
        Loghandler.log("is same"+issame, "warn");

        if (issame)
            return true;
        else
            return false;
    }

    /**
     *
     * @return
     */
    public Boolean isEmpty() {
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
}
