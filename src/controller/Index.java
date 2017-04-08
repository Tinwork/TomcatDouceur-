package controller;

import helper.Loghandler;

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

        url.Shortern processURL = new url.Shortern("", data.get("url"));
        try {
            processURL.init();
        } catch(Exception e){
            Loghandler.log("an error happened","warn");
        }

    }
}