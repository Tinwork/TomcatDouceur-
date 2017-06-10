package controller;

import account.Validity;
import helper.Dispatch;
import helper.Loghandler;
import helper.RequestParse;
import sql.UserDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 20/05/2017.
 */
public class RegisterController extends HttpServlet {

    private Validity validity;
    private final UserDB db = new UserDB();

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        String[] wanted = {"token"};
        // We does not call this registrationToken as it's not the same thing
        HashMap<String, String> validityToken = RequestParse.getParams(req, wanted);
        // Check the validity of the token

        if (validityToken.get("token") == null)
            Dispatch.dispatchError(req, res, "/sign", "token is null");

        this.validity = new Validity("");
        Boolean isTokenValid = validity.parseToken(validityToken.get("token"));

        if (!isTokenValid) {
            Dispatch.dispatchError(req, res, "/sign", "invalid token");
            return;
        }

        // Retrieve the payload. It should contain the username
        String payload = this.validity.getPayload();

        if (payload == null) {
            Dispatch.dispatchError(req, res, "/sign", "invalid payload");
            return;
        }

        // Activate the account
        Boolean activation = db.activateUser(payload);

        if (!activation){
            Dispatch.dispatchError(req, res, "/sign", "activation has failed");
            return;
        }

        Dispatch.dispatchSuccess(req, res, "", "", "/login");
        return;
    }
}
