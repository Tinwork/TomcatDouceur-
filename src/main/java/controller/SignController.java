package controller;

import account.Mailer;
import helper.Dispatch;
import helper.Helper;
import helper.Loghandler;
import helper.RequestParse;
import account.Password;
import sql.UserDB;
import sun.rmi.server.Dispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class SignController extends HttpServlet {

    protected final String PATH = "/WEB-INF/template/signup.jsp";

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        Dispatch.dispatchSuccess(req, res, "", "", PATH);
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

        if (presence) {
            Dispatch.dispatchError(req, res, PATH, "user already exist");
            return;
        }

        if (!Helper.validateMail(usrData.get("mail"))) {
            Dispatch.dispatchError(req, res, PATH, "mail address invalid");
            return;
        }

        // As the user does not exist we can now encrypt it's password and save it into the database
        Password pwd = new Password(usrData.get("password"));

        try {
            String hash = pwd.encrypt();
            String salt = pwd.getSalt();
            Boolean isInsert = usr.insertUser(usrData.get("username"), hash, salt, usrData.get("mail"));

            if (!isInsert) {
                Dispatch.dispatchError(req, res, PATH, "user has not been saved");
            }

            // Otherwise send a mail
            Mailer mail = new Mailer(usrData.get("mail"), usrData.get("username"));
            mail.sendMail();

            Dispatch.dispatchSuccess(req, res, "Congratulations", "Success", "/login");
            return;

        } catch (Exception e) {
            Dispatch.dispatchError(req, res, PATH, "unhandled error");
        }

    }
}
