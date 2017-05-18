package bean;

import org.json.JSONObject;
import url.Links;

import java.util.HashMap;

/**
 * Created by lookitsmarc on 17/05/2017.
 */
public class Constraint {
    // Use by constructor
    private Links link;
    private HashMap<String, Boolean> constrain;

    // Class's properties
    private String pwd;
    private String mail;
    private String original_url;
    private JSONObject mulPwd;


    /**
     * Constraint
     * @param link
     * @param constrain
     */
    public Constraint(Links link, HashMap<String, Boolean> constrain) {
        this.link = link;
        this.constrain = constrain;
    }

    /**
     * Get constrain enable us to build the JSP based on the request made by it...
     * @param type
     * @return
     */
    public Boolean getConstrain(String type) {
        return this.constrain.get(type);
    }

    /**
     *
     * @return
     */
    public String getPwd() {
        return pwd;
    }

    /**
     *
     * @param pwd
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     *
     * @return
     */
    public String getMail() {
        return "ddooooo";
    }

    /**
     *
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     *
     * @return
     */
    public String getOriginalURL() {
        return link.getOriginalURL();
    }

    /**
     *
     * @return
     */
    public int getMulPwdLength(){
        return link.getMulPwdLength();
    }
}
