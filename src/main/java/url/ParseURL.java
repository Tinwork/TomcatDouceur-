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
    protected Links link;

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

}
