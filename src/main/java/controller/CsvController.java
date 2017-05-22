package controller;

import csv.Csvparser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by lookitsmarc on 23/05/2017.
 */
public class CsvController extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        Part filePart = req.getPart("csv");
        String filename = getFileName(filePart);

        if (filename == null) {
            return;
        }

        Csvparser parser = new Csvparser(filename);
        parser.parse();
    }

    /**
     *
     * @param part
     * @return
     */
    public String getFileName(Part part){
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
