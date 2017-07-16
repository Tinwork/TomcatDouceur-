package controller;

import account.Mailer;
import account.UserFactory;
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
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 14/05/2017.
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
        String[] param = {"username","password","mail", "type"};
        HashMap<String, String> usrData =  RequestParse.getParams(req, param);
        UserFactory usr = new UserFactory(usrData);

        try {
            Boolean isRegisterDone = usr.registerProcess();

            if (isRegisterDone) {
                // Otherwise send a mail
                Mailer mail = new Mailer(usrData.get("mail"), usrData.get("username"));
                mail.sendMail();

                Dispatch.dispatchSuccess(req, res, "Congratulations you've just subscribe to tinwork. You should have received an activation email", "200", PATH);
                return;
            }
        } catch (Exception e) {
            Dispatch.dispatchError(req, res, PATH, e.getMessage());
            return;
        }
    }
}
