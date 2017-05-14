package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import helper.Loghandler;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class OauthController extends HttpServlet{

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException{

        try {

            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(req);
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

            OAuthResponse resp = OAuthASResponse
                    .authorizationResponse(req, res.SC_FOUND)
                    .setCode(oauthIssuerImpl.authorizationCode())
                    .location(redirectURI)
                    .buildQueryMessage();

            //Send reponse to given URI
            res.sendRedirect(resp.getLocationUri());

        } catch (OAuthSystemException ex) {
            Loghandler.log("DoGET OAuthSystemException: "+ex.toString(), "fatal");
        } catch (OAuthProblemException e) {
            Loghandler.log("DoGET OAuthProblemException: "+e, "fatal");
        }
    }

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        OAuthTokenRequest oauthRequest = null;
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {
            oauthRequest = new OAuthTokenRequest(req);
            String authzCode = oauthRequest.getCode();

            String accessToken = oauthIssuerImpl.accessToken();
            String refreshToken = oauthIssuerImpl.refreshToken();

            // some code
            OAuthResponse r = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn("3600")
                    .setRefreshToken(refreshToken)
                    .buildJSONMessage();

            res.setStatus(r.getResponseStatus());
            PrintWriter pw = res.getWriter();
            pw.print(r.getBody());
            pw.flush();
            pw.close();
        } catch (OAuthProblemException e) {
            Loghandler.log("DoPOST OAuthProblemException: "+e, "fatal");
        } catch (OAuthSystemException e) {
            Loghandler.log("DoPOST OAuthSystemException: "+e, "fatal");
        }
    }
}
