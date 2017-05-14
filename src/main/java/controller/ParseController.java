package controller;

import helper.Loghandler;
import url.ParseURL;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

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


        String requrl = req.getRequestURI();
        CharSequence template = "template";

        if (!requrl.contains(template)){
            Loghandler.log("url is called", "info");
            Loghandler.log(req.getPathInfo(), "info");

            // Now that we have the url we need to parse
            ParseURL parser = new ParseURL(req.getPathInfo());
            String original_url = parser.retrieveLinks();

            if (original_url != null)
                res.sendRedirect(original_url);

            // Otherwise we need to send an error to the JSP

            Loghandler.log(original_url, "info");
        }
    }
}
