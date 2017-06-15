package controller;

import bean.Constraint;
import helper.Dispatch;
import helper.Loghandler;
import sql.CountURL;
import url.Links;
import url.ParseURL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 09/06/2017.
 */
public class ParseShortURLController extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws IOException, ServletException {
        CountURL counter = new CountURL();
        Links link;

        // Create an instance of the parseurl class
        ParseURL parser = new ParseURL(req.getRequestURL().toString());
        link = parser.retrieveLinks();

        if (link == null)
            Dispatch.dispatchError(req, res, "/WEB-INF/template/404.jsp", "invalid url");

        // Retrieve the constraint of the link
        HashMap<String,Boolean> constraint = link.getConstrain();

        Loghandler.log("constraint "+constraint.toString(), "info");
        // if one of the constrait is present
        if (constraint.containsValue(true)) {
            // Now we redirect the user to the jsp and also send the bean
            Constraint beanConst = new Constraint(link, constraint);
            req.getSession().setAttribute("constraint", beanConst);
            req.getServletContext().getRequestDispatcher("/dao").forward(req, res);
            return;
        }

        String url = link.getOriginalURL();
        // Update the count table
        counter.updateCount(link.getRow());

        if (url != null)
            res.sendRedirect(url);
        else {
            res.sendRedirect("/tinwork/home");
        }

    }
}
