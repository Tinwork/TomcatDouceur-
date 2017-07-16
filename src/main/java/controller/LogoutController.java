package controller;

import helper.Dispatch;
import helper.Helper;
import helper.Loghandler;
import helper.RequestParse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 16/07/2017.
 */
public class LogoutController extends HttpServlet {

    protected final String PATH = "/WEB-INF/template/login.jsp";

    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws IOException, ServletException {
        String[] params = {"type"};

        HashMap<String, String> data = RequestParse.getParams(req, params);

        Helper.removeAttrFromReq(req);
        if (data.get("type") == null) {
            Dispatch.dispatchSuccess(req, res, "Successfully logout", "200", PATH);
            return;
        }

        Dispatch.dispatchSuccess(req, res, "Your profile has been updated. Please reconnect on the dashboard with your new profile", "200", PATH);
        return;
    }
}
