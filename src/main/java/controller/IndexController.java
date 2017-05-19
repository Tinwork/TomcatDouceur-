package controller;

import helper.Loghandler;
import url.UrlEntry;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 08/04/2017.
 */
public class IndexController extends HttpServlet {

    private bean.Error errorBean = new bean.Error();

    /**
     * Do Get
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/template/index.jsp").forward(req,res);
    }


    /**
     * Do Post
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException{
        String[] param = {"url", "password", "mail", "start_date", "end_date", "captcha"};
        String []mulpwd = {"passwords-1", "passwords-2", "passwords-3"};

        HashMap<String, String> data = helper.RequestParse.getParams(req, param);
        HashMap<String, String> datapwd = helper.RequestParse.getParams(req, mulpwd);

        if (data.isEmpty()){
            req = setBeanAttr(req, "warning", "can't retrieve the URL");
            this.getServletContext().getRequestDispatcher("/home").forward(req, res);
        }

        // If we get the URL we can init the URL encoding process
        UrlEntry processURL = new UrlEntry(data, datapwd);

        try {
            // Init check the validity of the URL and insert the original URL into the database
            Boolean isPresValid = processURL.init();

            if (!isPresValid) {
                processURL.insertAction();
            } else {
                req = setBeanAttr(req, "warning", "The same URL already exist");
                this.getServletContext().getRequestDispatcher("/home").forward(req, res);
            }

        } catch(Exception e){
            Loghandler.log(e.toString(),"warn");

            // Set the bean error
            req = setBeanAttr(req, "fatal", "can't insert the URL");
            this.getServletContext().getRequestDispatcher("/home").forward(req, res);
        }
    }

    /**
     *
     * @param req
     * @param level
     * @param m
     * @return
     */
    public javax.servlet.http.HttpServletRequest setBeanAttr(javax.servlet.http.HttpServletRequest req, String level, String m) {
        errorBean.setLevel(level);
        errorBean.setError(m);

        req.setAttribute("error", errorBean);
        return req;
    }
}