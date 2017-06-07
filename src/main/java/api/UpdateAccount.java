package api;

import helper.Helper;
import helper.RequestParse;
import org.json.JSONObject;
import sql.UserDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 06/06/2017.
 */
public class UpdateAccount extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws IOException, ServletException {
        PrintWriter writer = res.getWriter();
        JSONObject json = new JSONObject();
        String[] params = {"username", "password", "mail", "oldpwd", "data-token"};
        HashMap<String, String> data = RequestParse.getJSONParams(req, params);

        // check if one of the data is null
        if (data.containsValue(null)) {
            json.put("error", "data is null");
            this.write(res, json);
            return;
        }

        // Check the validity of the token
        Boolean isProfilValid = Helper.tokenValidity(data.get("data-token"));

        if (!isProfilValid) {
            json.put("error", "profile is not valid");
            this.write(res, json);
            return;
        }

        UserDB usrdb = new UserDB();
        Boolean success = usrdb.UpdateUserFlow(Helper.retrieveUserID(), data);

        if (success) {
            json.put("success", "profile is update");
            this.write(res, json);
            return;
        }

        // otherwise return an error
        json.put("error", "update user has failed");
        this.write(res, json);
        return;


    }

    /**
     * Write
     * @param res
     */
    public void write(javax.servlet.http.HttpServletResponse res, JSONObject data) throws IOException{
        PrintWriter writer = res.getWriter();
        writer.print(data);
        writer.flush();
    }
}
