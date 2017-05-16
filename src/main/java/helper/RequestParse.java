package helper;

import sun.rmi.log.LogHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 08/04/2017.
 */
public class RequestParse {

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

}
