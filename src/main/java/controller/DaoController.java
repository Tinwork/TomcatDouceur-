package controller;

import bean.Constraint;
import helper.Helper;
import helper.Loghandler;
import helper.RequestParse;
import url.Links;
import url.ParseURL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
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
        String[] params = {"password", "mail", "passwords-1", "passwords-2", "passwords-3", "g-recaptcha-response"};

        // Store the datas
        HashMap<String, String> postChecking = RequestParse.getParams(req, params);
        Loghandler.log("post checking "+postChecking.toString(), "info");

        Object objConst = req.getSession().getAttribute("constraint");
        Constraint constraint = (Constraint) objConst;

        // Get a link instance
        Links linkInstance = constraint.getLinkInstance();

        // Checking that the constraint bean is still present within the Session
        if (objConst == null)
            return;

        // Get the list of constraint
        String[] listConstraint = constraint.getAllConstraint();
        Loghandler.log("list of constraint "+listConstraint[0], "info");

        // If the datas is not empty
        if (!Helper.checkConstraintEmptyness(listConstraint, postChecking)) {
            Loghandler.log("some data are missing", "warn");
            return;
        }

        if (!ParseURL.checkConstraint(listConstraint, postChecking, linkInstance)) {
            Loghandler.log("some datas does are invalid", "warn");
            return;
        }

        res.sendRedirect(linkInstance.getOriginalURL());
    }
}
