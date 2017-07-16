package api;

import helper.Helper;
import helper.Loghandler;
import helper.RequestParse;
import org.json.JSONArray;
import org.json.JSONObject;
import sql.SelectLinks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 27/05/2017.
 */
public class GetURL extends HttpServlet{

    /**
     *
     * @param req
     * @param res
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("application/json");
        PrintWriter writer = res.getWriter();

        String[] paramWanted = {"data-token"};
        JSONObject resp = new JSONObject();
        HashMap<String, String> token = RequestParse.getJSONParams(req, paramWanted);

        if (token == null) {
            resp.put("success", false)
                .put("error", "token is empty");
        }

        // We implicitely say that the user id is pass within the request
        // As the request will be send from a JavaScript file
        Boolean isProfilValid = Helper.tokenValidity(token.get("data-token"));

        if (!isProfilValid) {
             resp.put("success", false)
                 .put("error", "profile is not valid");
            writer.print(resp);
            writer.flush();
            return;
        }

        // Now that the user has been auth and verified we can get the link for him
        SelectLinks link = new SelectLinks();

        try  {
            JSONArray data = link.selectLinksByUserID(Helper.retrieveUserID());
          //  Loghandler.log("arr json "+data.toString(), "info");

            writer.print(data);
            writer.flush();
        } catch (Exception e) {
            Loghandler.log("error sql "+e.toString(), "fatal");
            resp.put("success", false)
                    .put("error", "unexpected error please contact the admin");
            writer.print(resp);
            writer.flush();
            return;
        }
    }
}
