package api;

import helper.Helper;
import helper.RequestParse;
import org.json.JSONArray;
import org.json.JSONObject;
import sql.CountURL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 27/05/2017.
 */
public class GetCount extends HttpServlet{

    /**
     *
     * @param req
     * @param res
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws IOException, ServletException{
        res.setContentType("application/json");
        // Check the user authenticity
        PrintWriter writer = res.getWriter();

        String[] paramWanted = {"data-token", "id-url"};
        JSONObject resp = new JSONObject();
        HashMap<String, String> token = RequestParse.getJSONParams(req, paramWanted);

        if (token == null) {
            resp.put("error", "token is empty");
        }

        // We implicitely say that the user id is pass within the request
        // As the request will be send from a JavaScript file
        Boolean isProfilValid = Helper.tokenValidity(token.get("data-token"));

        if (!isProfilValid) {
            resp.put("error", "profile is not valid");
            writer.print(resp);
            writer.flush();
        }

        // return an array list of count
        CountURL counterURI = new CountURL();
        JSONArray arr = counterURI.getCount(Integer.parseInt(token.get("id-url")));

        writer.print(arr);
        writer.flush();
    }
}
