package url;

import helper.Loghandler;
import sql.InsertURL;
import sql.SelectLinks;

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
     */
    public String retrieveLinks(){
        Loghandler.log("exception", "short url");
        String originalURL = "";

        try {

            Links link = linkSelector.SelectLinksByShortLink(this.shortURL);
            long id = link.toBase10();


            Loghandler.log("id calc "+id+"", "info");
            if (id != link.getHashID())
                return null;
            Loghandler.log("back ID "+String.valueOf(id), "info");

            if (link == null)
                return null;

            originalURL = link.getOriginalURL();
        } catch (Exception e){
            Loghandler.log(e.toString(), "warn");
        }

        return originalURL;
    }
}
