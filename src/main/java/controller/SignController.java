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
public class SignController extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        this.getServletContext().getRequestDispatcher("/WEB-INF/template/signup.jsp").forward(req,res);
    }

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        // Retrieve the user, pwd
        String[] param = {"username","password","mail"};
        HashMap<String, String> usrData =  RequestParse.getParams(req, param);

        // We need to check whenever the user is already present in the database
        UserDB usr = new UserDB();
        Boolean presence = usr.userExist(usrData.get("username"));

        if (!presence)
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/signup.jsp").forward(req, res);

        // As the user does not exist we can now encrypt it's password and save it into the database
        Password pwd = new Password(usrData.get("password"));

        try {
            String hash = pwd.encrypt();
            String salt = pwd.getSalt();
            usr.insertUser(usrData.get("username"), hash, salt, usrData.get("mail"));

        } catch (Exception e) {
            Loghandler.log(e.toString(), "fatal");
            // We should return the JSP with an error..
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/signup.jsp").forward(req, res);
        }

    }
}
