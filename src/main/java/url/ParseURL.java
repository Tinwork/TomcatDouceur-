package url;

import helper.Helper;
import helper.Loghandler;
import sql.InsertURL;
import sql.SelectLinks;

import java.util.HashMap;

/**
 * Created by lookitsmarc on 13/04/2017.
 */
public class ParseURL {

    protected String shortURL;
    protected SelectLinks linkSelector;
    protected Links link;

    /**
     * Parse URL
     * @param url
     */
    public ParseURL(String url){

        String[] splitURL = url.split("/");
        this.shortURL = splitURL[splitURL.length - 1];
        this.linkSelector = new SelectLinks();
    }

    /**
     * Retrieve Links
     * Instead of retrieving the link directly this method will set 2 variable which will be use to get the original_ur
     * and the constraint
     * @return
     * @TODO Return a Link instance and process the link inside the Controller as well as handling the constrain
     */
    public Links retrieveLinks(){
        try {
            link = linkSelector.SelectLinksByShortLink(this.shortURL);
        } catch (Exception e){
            Loghandler.log(e.toString(), "warn");
        }

        return link;
    }

    /**
     * Check Constraint
     *          Check the constraint of the Short URL
     * @param constraint
     * @param postDatas
     * @param linkInstance
     * @return
     */
    public static boolean checkConstraint(String[] constraint, HashMap<String, String> postDatas, Links linkInstance){

        Boolean validity = false;
        for (int i = 0; i < constraint.length - 1; i++){
            String d = postDatas.get(constraint[i]);
            String key = constraint[i];


            try {
                switch(key) {
                    case "mulPwd":
                        validity = linkInstance.checkMulPwdIntegrity(postDatas);
                        break;
                    case "captcha":
                        validity = Helper.checkRecaptcha(postDatas.get("g-recaptcha-response"));
                        break;
                    case "max_use":
                        Loghandler.log("key "+key, "info");
                        validity = linkInstance.checkCount();
                        break;
                    case "start_date":
                    case "end_date":
                        validity = Helper.validateDate(linkInstance.getStart_date(), linkInstance.getEnd_date());
                        break;
                    default:
                        validity = linkInstance.checkParamIntegrety(key, d);

                }
            } catch (Exception e) {
                Loghandler.log(e.toString(), "fatal");
                return false;
            }
        }

        return validity;
    }

}
