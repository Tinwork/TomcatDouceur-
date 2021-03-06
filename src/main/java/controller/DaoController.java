package controller;

import bean.Constraint;
import helper.Dispatch;
import helper.Helper;
import helper.Loghandler;
import helper.RequestParse;
import sql.CountURL;
import url.Links;
import url.ParseURL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 18/05/2017.
 */
public class DaoController extends HttpServlet{

    protected final String PATH = "/WEB-INF/template/dao.jsp";

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(PATH).forward(req, res);
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
        CountURL counter = new CountURL();
        String[] params = {"password", "mail", "passwords", "g-recaptcha-response"};

        // Store the datas
        HashMap<String, String> postChecking = RequestParse.getParams(req, params);

        Object objConst = req.getSession().getAttribute("constraint");
        Constraint constraint = (Constraint) objConst;

        // Get a link instance
        Links linkInstance = constraint.getLinkInstance();

        // Checking that the constraint bean is still present within the Session
        if (objConst == null)
            return;

        // Get the list of constraint
        String[] listConstraint = constraint.getAllConstraint();

        // If the datas is not empty
        if (!Helper.checkConstraintEmptyness(listConstraint, postChecking)) {
            Loghandler.log("data is empty", "warn");
            Dispatch.dispatchError(req, res, PATH,"some datas are missing");
            return;
        }

        if (!ParseURL.checkConstraint(listConstraint, postChecking, linkInstance)) {
            Loghandler.log("data is bad", "warn");
            Dispatch.dispatchError(req, res, PATH,"Constraint are invalid. If you use he set Max use, it might be possible that the link has been already been used too much");
            return;
        }

        counter.updateCount(linkInstance.getRow());

        res.sendRedirect(linkInstance.getOriginalURL());
    }
}
