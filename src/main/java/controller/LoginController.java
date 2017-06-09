package controller;

import bean.Userstate;
import helper.Dispatch;
import helper.RequestParse;
import account.Password;
import account.Token;
import sql.UserDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class LoginController extends HttpServlet {

    protected final String PATH = "/WEB-INF/template/login.jsp";

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        Dispatch.dispatchSuccess(req, res, "", "", PATH);
    }

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        UserDB usr = new UserDB();
        // Retrieve the user, pwd
        String[] param = {"username","password"};
        HashMap<String, String> usrData =  RequestParse.getParams(req, param);

        if (usrData.get("username").isEmpty() || usrData.get("password").isEmpty()) {
            Dispatch.dispatchError(req, res, PATH, "datas are empty");
            return;
        }

        // Select the hash and the salt from the user
        byte[][] logData = usr.selectPwd(usrData.get("username"));

        if (logData == null) {
            Dispatch.dispatchError(req, res, PATH, "login datas are empty");
            return;
        }

        Password pwd = new Password(null);

        // parse the user imput pwd with the one in the db
        Boolean issame = pwd.compareHash(usrData.get("password").toCharArray(), logData[0], logData[1]);

        if (issame) {
            Token tokenHandler = new Token();
            int id = usr.selectUserID(usrData.get("username"));

            // Set the id
            tokenHandler.setId(id);

            // Generate the token
            String token = tokenHandler.generateToken();
            Userstate bean = setBean(usrData.get("username"), token, id);
            RedirectWithBean(req, res, bean);
        } else {
            Dispatch.dispatchError(req, res, PATH, "invalid user");
            return;
        }
    }

    /**
     *
     * @param username
     * @param hash
     * @return
     */
    public Userstate setBean(String username, String hash, int id) {
        if (username.isEmpty() && hash.isEmpty())
            return null;

        Userstate state = new Userstate(username, hash, id);

        return state;
    }

    /**
     *
     * @param req
     * @param res
     * @param bean
     * @throws ServletException
     * @throws IOException
     */
    public void RedirectWithBean(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res, Userstate bean) throws ServletException, IOException{
        HttpSession session = req.getSession();
        session.setAttribute("userstate", bean);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/dashboard");
        dispatcher.forward(req, res);
    }

}
