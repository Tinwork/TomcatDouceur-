package controller;

import helper.Loghandler;
import helper.RequestParse;
import login.Password;
import sql.UserDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class LoginController extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/template/login.jsp").forward(req,res);
    }

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        // Retrieve the user, pwd
        String[] param = {"username","password"};
        HashMap<String, String> usrData =  RequestParse.getParams(req, param);

        if (usrData.get("username").isEmpty() || usrData.get("password").isEmpty())
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/login.jsp").forward(req, res);


        // Select the hash and the salt from the user
        UserDB usr = new UserDB();
        byte[][] logData = usr.selectPwd(usrData.get("username"));

        if (logData.length == 0)
            throw new ServletException("log data empty");


        Password pwd = new Password(null);

        // parse the user imput pwd with the one in the db
        Boolean issame = pwd.compareHash(usrData.get("password").toCharArray(), logData[0], logData[1]);
        Loghandler.log("is same "+String.valueOf(issame), "info");
    }
}
