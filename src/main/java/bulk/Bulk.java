package bulk;

import helper.Loghandler;
import sql.InsertURL;
import url.UrlEntry;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 25/05/2017.
 * In the first impression i though that we would be able to do a seperate thread
 * In order to not alter the UI rendering. But it looks like that well.. actually not
 */
public class Bulk {

    private ArrayList<HashMap<String, String>> list;
    private InsertURL urlManager;
    private int userID;

    /**
     * I am ashamed by this code...
     * @param list
     */
    public Bulk(ArrayList list, int userID) {
        this.list = list;
        this.userID = userID;
    }

    /**
     *
     * @throws Exception
     */
    public void insertData(){
        // Loop threw the array list
        for (HashMap<String, String> data : this.list) {
            // if the data is empty then skip the data
            if (data.isEmpty())
                continue;

            // Get the password
            Loghandler.log(data.toString(), "info");
            UrlEntry processURL = new UrlEntry(data, getPassword(data.get("mulpwd")), userID);

            try {
                // Init check the validity of the URL and insert the original URL into the database
                Boolean isPresValid = processURL.init();

                if (!isPresValid) {
                    processURL.insertAction();
                } else {

                }
            } catch(Exception e){
                Loghandler.log(e.toString(),"warn");
            }
        }
    }

    /**
     *
     * @param pwd
     * @return
     */
    public HashMap<String, String> getPassword(String pwd){
        Loghandler.log("pwd orig "+pwd, "info");
        HashMap<String, String> pwdMap = new HashMap<String, String>();

        if (pwd == null) {
            pwdMap.put("passwords-1", null);
            pwdMap.put("passwords-2", null);
            pwdMap.put("password-3", null);

            return pwdMap;
        }

        String[] pwds = pwd.split("/");

        int idx = 0;
        while (idx < pwds.length) {
            pwdMap.put("passwords-"+(idx+1), pwds[idx]);
            idx++;
        }

        return pwdMap;
    }
}
