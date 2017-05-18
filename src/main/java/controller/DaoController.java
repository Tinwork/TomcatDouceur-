package controller;

import bean.Constraint;
import helper.Loghandler;
import helper.RequestParse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 18/05/2017.
 */
public class DaoController extends HttpServlet{

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/template/dao.jsp").forward(req, res);
    }

    /**
     * Dao Controller
     *          Checking the value
     * @method {POST}
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        String[] params = {"password", "mail", "password-1", "password-2", "password-3"};
        HashMap<String, String> postChecking = RequestParse.getParams(req, params);
        Loghandler.log("post checking "+postChecking.toString(), "info");

        Object objConst = req.getSession().getAttribute("constraint");
        Constraint constraint = (Constraint) objConst;

        Loghandler.log("constraint value mail"+constraint.getMail(), "info");
        if (objConst == null)
            return;


    }
}
