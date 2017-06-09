package controller;

import bean.Constraint;
import com.sun.org.apache.xpath.internal.operations.Bool;
import helper.Loghandler;
import sql.CountURL;
import url.Links;
import url.ParseURL;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 13/04/2017.
 */
public class ParseController extends HttpServlet{

    /**
     * Do Get
     * @param req
     * @param res
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    @Override
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException{

        CountURL counterURI = new CountURL();
        String requrl = req.getRequestURI();
        CharSequence template = "template";
        Loghandler.log("url exist "+req.getRequestURL(), "warn");

        if (!requrl.contains(template)){

            // Now that we have the UrlAPI we need to parse
            ParseURL parser = new ParseURL(req.getRequestURL().toString());
            Links link = parser.retrieveLinks();

            // Now that we have retrieve the link we might need to check if there're any constraint on it
            HashMap<String, Boolean> constraint = link.getConstrain();
            Loghandler.log(constraint.toString(), "constraint");

            if (constraint.containsValue(true)) {
                // Now we redirect the user to the jsp and also send the bean
                Constraint beanConst = new Constraint(link, constraint);
                req.getSession().setAttribute("constraint", beanConst);
                req.getServletContext().getRequestDispatcher("/dao").forward(req, res);
                return;
            }

            String url = link.getOriginalURL();
            // Update the count table
            counterURI.updateCount(link.getRow());

            if (url != null)
                res.sendRedirect(url);
            else {
                res.sendRedirect("/tinwork/home");
            }

            // Otherwise we need to send an error to the JSP
            Loghandler.log(url, "info");
        }
    }
}
