package csv;

import helper.Loghandler;
import javax.servlet.http.Part;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 23/05/2017.
 */
public class Csvparser {

    private static InputStream fileContent;
    private static final String[] HEADER = {"url", "password", "captcha", "start_date", "end_date", "multiple_password"};
    private static BufferedReader buffer;
    private static final String SEPERATOR = ",";


    /**
     *
     * @param file
     */
    public static void init(Part file){
        try {
            fileContent = file.getInputStream();
        } catch (IOException e) {
            Loghandler.log("an error happened while reading the csv file "+e.toString(), "fatal");
        }
    }

    /**
     *
     * @return
     */
    public static ArrayList parse(){
        HashMap<String, String> parseData;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        String line = "";
        try {
            buffer = new BufferedReader(new InputStreamReader(fileContent, "ISO-8859-1"));

            while ((line = buffer.readLine()) != null ) {
                String[] data = line.split(SEPERATOR);
                parseData = buildData(data);

                if (parseData != null) {
                    list.add(parseData);
                }
            }
        } catch (UnsupportedEncodingException e) {
            Loghandler.log(e.toString(), "info");
        } catch (IOException e) {
            Loghandler.log(e.toString(), "info");
        }

        return list;
    }

    /**
     * I am ashamed by this code
     * @param data
     * @return
     */
    private static HashMap buildData(String[] data){
        HashMap<String, String> csvData = new HashMap<String, String>();
        String[] line = data[0].split(";");

        // we check that at least the URL is present within the datas
        if (line.length == 0) {
            return null;
        }

        if (line[0] == null)
            return null;

        for (int idx = 0; idx < line.length; idx++) {
            Loghandler.log(line[0], "info");
            Loghandler.log("length "+line.length, "info");
            switch (idx) {
                case 0:
                    csvData.put("url", line[0]);
                    break;
                case 1:
                    csvData.put("password", line[1]);
                    break;
                case 2:
                    csvData.put("captcha", line[2]);
                    break;
                case 3:
                    csvData.put("start_date", line[3]);
                    break;
                case 4:
                    csvData.put("end_date", line[4]);
                    break;
                case 5:
                    csvData.put("mulpwd", line[5]);
                    break;
            }
        }

        return csvData;
    }
}
