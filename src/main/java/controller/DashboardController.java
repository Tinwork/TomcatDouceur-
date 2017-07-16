package controller;

import bean.Userstate;
import account.Token;
import helper.Dispatch;
import helper.Helper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 15/05/2017.
 */
public class DashboardController extends HttpServlet{

    protected final String PATH = "/WEB-INF/template/dashboard.jsp";
    protected final String PATH_ERR = "/login";

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     * @TODO to refactor
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        // Now what we need to do is to check the token validity
        Boolean isProfilValid = Helper.processRequest(req, res);

        if (isProfilValid) {
            Dispatch.dispatchSuccess(req, res, "", "" ,PATH);
            return;
        }
        else {
            Dispatch.dispatchError(req, res, PATH_ERR, "bad username or password");
            return;
        }
    }

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        Boolean isProfilValid = Helper.processRequest(req, res);

        if (isProfilValid) {
            Dispatch.dispatchSuccess(req, res, "", "", PATH);
            return;
        }
        else {
            Dispatch.dispatchError(req, res, PATH_ERR, "bad username or password");
            return;
        }
    }
}
