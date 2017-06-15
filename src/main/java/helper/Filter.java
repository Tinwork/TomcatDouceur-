package helper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 15/06/2017.
 */
public class Filter implements javax.servlet.Filter {

    private final String path = "/WEB-INF/template/404.jsp";

    /**
     * Init
     * @param conf
     */
    public void init(FilterConfig conf) {

    }

    /**
     * Destroy
     */
    public void destroy() {

    }

    /**
     *
     * @param req
     * @param res
     * @param filterChain
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException{

        HttpServletRequest reqServlet = (HttpServletRequest) req;
        HttpServletResponse resServlet = (HttpServletResponse) res;

        int length = reqServlet.getRequestURL().toString().split("/").length;

        if (length != 4) {
            // this filter does nothing for the moment a part from redirect to the servlet
            filterChain.doFilter(req, res);
        } else {
            Dispatch.dispatchError(reqServlet, resServlet, path, "invalid url");
        }
    }
}
