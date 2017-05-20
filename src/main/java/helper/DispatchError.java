package helper;

import bean.Error;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by lookitsmarc on 20/05/2017.
 */
public class DispatchError {

    /**
     *
     * @param req
     * @param path
     * @param error
     */
    public static void dispatch(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res, String path, String error) throws ServletException, IOException{
        Error e = new Error();
        e.setError(error);

        req.setAttribute("error", e);
        req.getServletContext().getRequestDispatcher(path).forward(req, res);

        return;
    }
}
