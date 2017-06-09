package helper;

import bean.Error;
import bean.Success;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by lookitsmarc on 20/05/2017.
 */
public class Dispatch {

    /**
     *
     * @param req
     * @param path
     * @param error
     */
    public static void dispatchError(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res, String path, String error) throws ServletException, IOException{
        Error e = new Error();
        e.setError(error);

        req.setAttribute("error", e);
        Loghandler.log("path "+path, "warning");
        req.getServletContext().getRequestDispatcher(path).forward(req, res);
        return;
    }

    /**
     *
     * @param req
     * @param res
     * @param success
     * @param status
     * @param path
     * @throws ServletException
     * @throws IOException
     */
    public static void dispatchSuccess(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res, String success, String status, String path) throws ServletException, IOException {
        Success s = new Success();
        s.setMessage(success);
        s.setStatus(status);

        req.setAttribute("success", s);
        req.getServletContext().getRequestDispatcher(path).forward(req, res);
        return;
    }
}
