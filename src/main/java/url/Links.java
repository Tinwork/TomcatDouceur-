package url;

import helper.Loghandler;
import org.json.JSONArray;
import org.json.JSONObject;
import sql.CountURL;


import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lookitsmarc on 11/04/2017.
 */
public class Links {

    // This class represent what we have as a data in the database for a single link.
    // Use this class to operate a link
    private HashMap<String, String> strDatas;
    private int sql_id;
    private Date start_date;
    private Date end_date;
    private Boolean captcha;
    private int max_use;


    /**
     *
     * @param urlDatas
     * @param sql_id
     * @param hashid
     * @param captcha
     * @param startDate
     * @param endDate
     */
    public Links(HashMap<String, String> urlDatas, int sql_id, long hashid, boolean captcha, Date startDate, Date endDate, int max_use){
        this.strDatas = urlDatas;
        this.sql_id = sql_id;
        this.captcha = captcha;
        this.start_date = startDate;
        this.end_date = endDate;
        this.max_use = max_use;
    }

    /**
     * Get Original URL
     * @return orig_link
     */
    public String getOriginalURL(){
        return this.strDatas.get("original_link");
    }

    /**
     *
     * @return
     */
    public HashMap<String, Boolean> getConstrain(){
        HashMap<String, Boolean> constrain = new HashMap<String, Boolean>();

        constrain.put("password", this.strDatas.get("password") == null ? false : true);
        constrain.put("captcha", this.captcha == null || !this.captcha ? false : true);
        constrain.put("mail", this.strDatas.get("mail") == null ? false : true);
        constrain.put("start_date", this.start_date == null ? false : true);
        constrain.put("end_date", this.end_date == null ? false : true);
        constrain.put("mulPwd", this.strDatas.get("multiple_password") == null ? false : true);
        constrain.put("max_use", this.max_use == 0 ? false : true);

        return constrain;
    }

    /**
     *
     * @return
     */
    public JSONObject getMulPwd(){
        JSONObject json = new JSONObject(this.strDatas.get("multiple_password"));

        return json;
    }

    /**
     *
     * @return
     */
    public int getRow(){ return this.sql_id; };

    /**
     *
     * @return
     */
    public String getPassword(){
        return this.strDatas.get("password");
    }

    /**
     *
     * @return
     */
    public String getMail(){
        return this.strDatas.get("mail");
    }

    /**
     *
     * @return
     */
    public Boolean getCaptcha(){
        return this.captcha;
    }

    /**
     *
     * @return
     */
    public Date getStart_date(){
        return this.start_date;
    }

    /**
     *
     * @return
     */
    public Date getEnd_date(){
        return this.end_date;
    }

    /**
     *
     * @param constraint
     * @param inputValue
     * @return
     * @throws Exception
     */
    public Boolean checkParamIntegrety(String constraint, String inputValue) throws Exception{
        Boolean validity = false;
        switch (constraint) {
            case "password":
                validity = LinkPwd.checkValidity(this.strDatas.get("password"), inputValue);
                break;
            case "mail":
                validity = this.strDatas.get("mail").equals(inputValue);
                break;
            case "captcha":
                validity = this.captcha;
                break;
            default:
                throw new Exception(constraint+" is not supported");
        }

        return validity;
    }

    /**
     * Check Multiple Password Integrit
     * @param postDatas
     * @return
     */
    public Boolean checkMulPwdIntegrity(HashMap<String, String> postDatas){
       // We're going to check the value of the password input by the user and the password saved into the database
       Boolean validity = false;
       // Mulpwd is the password that exist in the database
       JSONObject mulPwd = new JSONObject(this.strDatas.get("multiple_password"));
       Iterator<String> keys = mulPwd.keys();

       while (keys.hasNext() && !validity) {
           String key = keys.next();
           String dbpwd = mulPwd.getString(key);

           validity = LinkPwd.checkValidity(dbpwd, postDatas.get("passwords"));
       }

       return validity;
    }

    /**
     * Check Count
     * @return
     */
    public Boolean checkCount() {
        CountURL counter = new CountURL();
        JSONArray jarr = counter.getCount(this.sql_id);

        if (jarr == null)
            return true;

        if (this.max_use > jarr.length())
            return true;

        return false;
    }

}
