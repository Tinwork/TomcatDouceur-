package helper;

import bean.Userstate;
import sun.rmi.log.LogHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 08/04/2017.
 */
public class RequestParse {

    private static int userID;

    /**
     * Get Params
     * @param req
     * @param wanted
     * @return HashMap</String,String>
     */
    public static HashMap<String, String> getParams(javax.servlet.http.HttpServletRequest req, String[] wanted){
        HashMap<String, String> dataMap = new HashMap<String, String>();

        try {
            for (int i = 0; i < wanted.length; i++){
                dataMap.put(wanted[i], req.getParameter(wanted[i]));
            }
        } catch (Exception e){
            Loghandler.log("parse req : "+e.toString(), "warn");
        }

        return dataMap;
    }

    /**
     *
     * @param req
     * @return
     */
    public static int retrieveUser(javax.servlet.http.HttpServletRequest req) {
        Object userstate = req.getSession().getAttribute("userstate");

        if (userstate == null) {
            // Returning 1 mean that the link is anonymous and not track by the app
            return 1;
        }

        // Cast the Java Object into the Userstate bean instance
        Userstate user = (Userstate) userstate;

        // Retrieve the id
        return user.getUserID();
    }

}
