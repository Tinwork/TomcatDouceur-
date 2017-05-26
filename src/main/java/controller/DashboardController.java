package controller;

import bean.Userstate;
import account.Token;
import helper.Helper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * Created by lookitsmarc on 15/05/2017.
 */
public class DashboardController extends HttpServlet{


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
        Boolean isProfilValid = Helper.processRequest(req, res);

        if (isProfilValid)
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/dashboard.jsp").forward(req, res);
        else
            res.sendRedirect("/tinwork/dashboard");
    }

    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{
        Boolean isProfilValid = Helper.processRequest(req, res);

        if (isProfilValid)
            this.getServletContext().getRequestDispatcher("/WEB-INF/template/dashboard.jsp").forward(req, res);
        else
            res.sendRedirect("/tinwork/dashboard");
    }
}
