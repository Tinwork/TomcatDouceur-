package controller;

import helper.Loghandler;
import sql.InsertURL;
import sql.SelectLinks;
import url.Links;
import url.UrlEntry;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 08/04/2017.
 */
@WebServlet("/index.jsp")
public class Index extends HttpServlet {

    private bean.Error errorBean = new bean.Error();

    /**
     * Do Get
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException {
        Loghandler.log("get url passed here", "info");
        this.getServletContext().getRequestDispatcher("/WEB-INF/template/index.jsp").forward(req,res);
        //res.sendRedirect("/test.jsp");
    }


    /**
     * Do Post
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException{
        String[] param = {"url"};
        HashMap<String, String> data = helper.RequestParse.getParams(req, param);

        if (data.isEmpty()){
            Loghandler.log("data is empty", "warn");

            errorBean.setLevel("warning");
            errorBean.setError("can't retrieve the URL");
            req.setAttribute("errorbean", errorBean);
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/index.jsp").forward(req, res);
        }

        // If we get the URL we can init the URL encoding process
        UrlEntry processURL = new UrlEntry("", data.get("url"));

        try {
            // Init check the validity of the URL and insert the original URL into the database
            Boolean isPresent = processURL.init();

            if (!isPresent) {
                Loghandler.log("pass here insert", "info");
                processURL.insertAction(data.get("url"));
            }

        } catch(Exception e){
            Loghandler.log(e.toString(),"warn");
        }

    }
}