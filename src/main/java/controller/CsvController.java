package controller;

import bulk.Bulk;
import csv.Csvparser;
import helper.Helper;
import helper.Loghandler;
import helper.RequestParse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 23/05/2017.
 */
@MultipartConfig
public class CsvController extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        // Retrieving the Java's bean
        Boolean isValid = Helper.processRequest(req, res);

        // @TODO should redirect towar something
        if (!isValid) {
            return;
        }

        Part filePart = req.getPart("csv");
        String filename = getFileName(filePart);

        if (filename == null) {
            Loghandler.log("data is empty", "info");
            this.getServletContext().getRequestDispatcher("/dashboard").forward(req, res);
            return;
        }

        Csvparser.init(filePart);
        ArrayList list = Csvparser.parse();

        // Launch a background process
        // Yes we get the userid using the helper... I didn't though about getting the bean here
        Bulk bulk = new Bulk(list, Helper.retrieveUserID());
        // Hope that it will pass ..
        bulk.insertData();
    }

    /**
     *
     * @param part
     * @return
     */
    public String getFileName(Part part){
        Loghandler.log("part "+part.getHeader("content-disposition"), "info");
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
