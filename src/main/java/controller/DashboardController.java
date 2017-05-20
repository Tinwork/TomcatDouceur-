package controller;

import bean.Userstate;
import account.Token;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * Created by lookitsmarc on 15/05/2017.
 */
public class DashboardController extends HttpServlet{

    private Userstate userstate;

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     * @TODO to refactor
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        // Now what we need to do is to check the token validity
        Boolean isProfilValid = processRequest(req, res);

        if (isProfilValid)
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/dashboard.jsp").forward(req, res);
        else
            res.sendRedirect("/tinwork/account");
    }

    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        Boolean isProfilValid = processRequest(req, res);

        if (isProfilValid)
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/dashboard.jsp").forward(req, res);
        else
            res.sendRedirect("/tinwork/account");
    }

    public Boolean processRequest(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res){
        Object bean = req.getSession().getAttribute("userstate");

        if (bean == null)
            return false;

        userstate = (Userstate) bean;

        if (!tokenValidity(userstate.getToken()))
            return false;

        return true;
    }


    /**
     *
     * @param token
     * @return
     */
    public Boolean tokenValidity(String token) {
        Token tokenize = new Token();
        return tokenize.parseToken(token);
    }
}
