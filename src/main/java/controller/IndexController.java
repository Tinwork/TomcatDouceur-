package controller;

import helper.Dispatch;
import helper.Loghandler;
import url.ShortFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 08/04/2017.
 */
public class IndexController extends HttpServlet {

    private final String path = "/WEB-INF/template/index.jsp";

    /**
     * Do Get
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/template/index.jsp").forward(req,res);
    }


    /**
     * Do Post
     * @param req
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        String[] param = {"url", "password", "mail", "start_date", "end_date", "captcha", "passwords-1", "passwords-2", "passwords-3", "max_use"};

        HashMap<String, String> data = helper.RequestParse.getParams(req, param);
        int userID = helper.RequestParse.retrieveUser(req);

        if (data.isEmpty()){
            Dispatch.dispatchError(req, res, path, "can not retrieve the URL");
            return;
        }

        // If we get the URL we can init the URL encoding process
        //UrlEntry processURL = new UrlEntry(data, userID);
        ShortFactory processURL = new ShortFactory(data, userID);
        Boolean isInsert = processURL.initProcess();

        if (!isInsert) {
            Loghandler.log("error", "warn");

            // Check that shortURL is different of null
            if(processURL.getShortURL() == null) {
                Dispatch.dispatchError(req, res, path, "Insert failed for URL " + data.get("url"));
                return;
            } else {
                Dispatch.dispatchSuccess(req, res, "Link already present <a href='"+processURL.getShortURL()+"'>"+processURL.getShortURL()+"</a>", "200", path);
                return;
            }
        }

        Dispatch.dispatchSuccess(req, res, "Link successfully added <a href='"+processURL.getShortURL()+"'>tinwork/"+processURL.getShortURL()+"</a>", "200", path);
        return;
    }
}