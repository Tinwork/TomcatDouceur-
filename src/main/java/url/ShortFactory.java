package url;

import helper.Helper;
import helper.Loghandler;
import org.json.JSONObject;
import sql.InsertURL;

import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 09/06/2017.
 */
public class ShortFactory {

    // Private fields
    private HashMap<String, String> userDatas;
    private int UDID;
    protected int row;

    // sql date
    private java.sql.Date startDate;
    private java.sql.Date endDate;

    // Protected fields
    protected InsertURL insertFactory;


    /**
     * Constructor
     * @param data
     * @param userID
     */
    public ShortFactory(HashMap<String, String> data, int userID) {
        this.userDatas = data;
        this.UDID = userID;

        // date
        this.startDate = Helper.StrToSQLDate(data.get("start_date"));
        this.endDate = Helper.StrToSQLDate(data.get("end_date"));
    }

    /**
     * @public
     * @return
     */
    public boolean initProcess() {
        Boolean res = false;
        try {
            res = this.validURL().insertOriginalURL().AddShortURL();
        } catch (Exception e) {
            Loghandler.log("exception in initProcess "+e.toString(), "fatal");
        }

        return res;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    private ShortFactory validURL() throws Exception{
        if (!Helper.validateURL(this.userDatas.get("url")))
            throw new Exception("url is not valid");

        return this;
    }

    /**
     * @private
     * @return
     */
    private ShortFactory insertOriginalURL() throws Exception{
        // First insert the current url
        this.insertFactory = new InsertURL(this.userDatas.get("url"), this.UDID);

        // Make the short url
        String hashUserPwd = LinkPwd.hash(this.userDatas.get("password"));

        // Check the mail
        String mail = Helper.validateMail(this.userDatas.get("mail")) ? this.userDatas.get("mail") : null;

        // Captcha
        Boolean captcha = this.userDatas.get("captcha") == null ? false : true;

        // Max_use
        int max_use = this.userDatas.get("max_use") == null ? 0 : Integer.parseInt(this.userDatas.get("max_use"));

        // Build the multiple passwords
        JSONObject multiplePwd = this.buildMulPwd(this.userDatas);
        // Insert the datas
        this.row = this.insertFactory.insertOriginalURL(hashUserPwd, mail, this.startDate, this.endDate, captcha, this.buildMulPwd(this.userDatas), max_use);

        if (this.row == 0)
            throw new Exception("insert has failed");

        return this;
    }

    /**
     * @Add Short URL
     * @return
     */
    private Boolean AddShortURL() {
        CreateLink short_link = new CreateLink(this.row);
        String shortURL = short_link.encodeLongURL();
        long hashURL = short_link.getShortURLHash();

        try {
            this.insertFactory.insertShortLink(hashURL, shortURL, this.row);
        } catch(Exception e){
            Loghandler.log("trying to insert the url "+e.toString(), "fatal");
            return false;
        }

        return true;
    }

    /**
     *
     * @param data
     * @return
     */
    final private JSONObject buildMulPwd(HashMap<String, String> data){
        JSONObject json = new JSONObject();

        HashMap<String, String> pwds = new HashMap<String, String>();
        pwds.put("passwords-1", data.get("passwords-1"));
        pwds.put("passwords-2", data.get("passwords-2"));
        pwds.put("passwords-3", data.get("passwords-1"));

        int idx = 0;
        for (String pwd : pwds.keySet()){
            if (pwds.get(pwd) != null){
                String hashes = LinkPwd.hash(pwds.get(pwd));
                json.put(pwd, hashes);
            }

            idx++;
        }

        return json;
    }
}
