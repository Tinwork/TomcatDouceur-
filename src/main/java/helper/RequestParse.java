package helper;

import bean.Userstate;
import org.json.JSONObject;
import org.json.JSONString;
import sun.rmi.log.LogHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 08/04/2017.
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
     * @param
     * @return
     */
    public static HashMap<String, String> getJSONParams(javax.servlet.http.HttpServletRequest req, String[] paramwanted){
        HashMap<String, String> jsondata = new HashMap<String, String>();
        StringBuilder buffer = new StringBuilder();

        try {
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String data = buffer.toString();

            // convert the data into a json object
            JSONObject json = new JSONObject(data);

            for (int i = 0; i < paramwanted.length; i++) {
                jsondata.put(paramwanted[i], json.getString(paramwanted[i]));
            }
        } catch (IOException e) {
            Loghandler.log("json parse "+e.toString(), "info");
        }

        return jsondata;
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
