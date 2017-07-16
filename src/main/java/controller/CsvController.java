package controller;

import bulk.Bulk;
import csv.Csvparser;
import helper.Dispatch;
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
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 23/05/2017.
 */
@MultipartConfig
public class CsvController extends HttpServlet {

    protected final String PATH = "/WEB-INF/template/dashboard.jsp";

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

        if (!isValid) {
            Dispatch.dispatchError(req, res, PATH, "CSV is not valid");
            return;
        }

        Part filePart = req.getPart("csv");
        String filename = getFileName(filePart);

        if (filename == null) {
            Loghandler.log("data is empty", "info");
            Dispatch.dispatchError(req, res, PATH, "datas are empty");
            return;
        }

        Csvparser.init(filePart);
        ArrayList list = Csvparser.parse();

        // Launch a background process
        // Yes we get the userid using the helper... I didn't though about getting the bean here
        Bulk bulk = new Bulk(list, Helper.retrieveUserID());
        Boolean insertStatus = bulk.insertData();

        if (!insertStatus) {
            Dispatch.dispatchError(req, res, PATH, "unable to save the CSV in the database");
            return;
        }

        Dispatch.dispatchSuccess(req, res, "Data insert", "200", PATH);
        return;
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
