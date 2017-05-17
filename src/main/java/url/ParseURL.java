package url;

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

    /**
     * Parse URL
     * @param url
     */
    public ParseURL(String url){

        this.shortURL = url.split("/")[1];
        this.linkSelector = new SelectLinks();
        Loghandler.log(this.shortURL+" short url value", "info");
    }

    /**
     * Retrieve Links
     * @return
     * @TODO Return a Link instance and process the link inside the Controller as well as handling the constrain
     */
    public String retrieveLinks(){
        String originalURL = "";

        try {

            Links link = linkSelector.SelectLinksByShortLink(this.shortURL);

            // Now we need to check whenever there're some addional options in order to convert the retrieve the Links such as the password
            HashMap<String, Boolean> constrain = link.getConstrain();

            if (constrain.containsValue(true)) {
                // Build a JSP with a constrain bean and the Link instance so we can then compare the value...

            }

            long id = link.toBase10();
            if (id != link.getHashID())
                return null;

            if (link == null)
                return null;

            originalURL = link.getOriginalURL();
        } catch (Exception e){
            Loghandler.log(e.toString(), "warn");
        }

        return originalURL;
    }

}
