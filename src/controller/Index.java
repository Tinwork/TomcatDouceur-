package controller;

import helper.Loghandler;
import sql.SelectLinks;
import url.Links;
import url.UrlEntry;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 08/04/2017.
 */
public class Index extends HttpServlet {

    private bean.Error errorBean = new bean.Error();

    /**
     * Do Get
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException {
        String[] param = {"user"};
        HashMap<String, String> data = helper.RequestParse.getParams(req, param);
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
            this.getServletContext().getRequestDispatcher("index.jsp").forward(req, res);
        }

        // If we get the URL we can init the URL encoding process
        UrlEntry processURL = new UrlEntry("", data.get("url"));

        try {
            // Init check the validity of the URL and insert the original URL into the database
            int row = processURL.init();

            // If the init process is completed and successfull then we can generate a shorten url
            if (row  != -1 && row != 0){
                SelectLinks links = new SelectLinks();
                Links link = links.retrieveSQLLink(row);

                // now we can shorten the link
                link.encodeLongURL();
            }
        } catch(Exception e){
            Loghandler.log(e.toString(),"warn");
        }

    }
}