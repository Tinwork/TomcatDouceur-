package sql;

import helper.Loghandler;
import sun.rmi.log.LogHandler;
import url.Links;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class InsertURL extends Connect{

    private String encode_url;
    private String original_url;
    private int userID;

    /**
     * InsertURL
     *      Constructor
     * @param originalURL String
     */
    public InsertURL(String originalURL, int userID){
        this.original_url = originalURL;
        this.userID = userID;
        this.connectToDB();
    }

    /**
     * Insert Data
     *      Insert datas in the database
     */
    public int insertData(){
        int updateState = 0;
        int lastRow = 0;

        try {
            // Otherwise we try to insert the url in the db
            String sql = "INSERT INTO Link (original_link, user_id, create_date) VALUES (?, ?, ?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Bind the params
            stmt.setString(1, this.original_url);
            stmt.setInt(2, this.userID);
            stmt.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));

            updateState = stmt.executeUpdate();

            if (updateState == 0)
                throw new Exception("unable to add into the db");

            ResultSet res = stmt.getGeneratedKeys();
            if (res.next())
                lastRow = res.getInt(1);

        } catch (Exception e){
            Loghandler.log(e.toString()+" insert data", "fatal");
        }

        return lastRow;
    }


    /**
     * Check Presence Of URL
     *      Check the presence of the original_url or the short_link
     * @return boolean
     */
    public boolean checkPresenceOfURL(){
        String sql = "SELECT * FROM Link WHERE original_link = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, this.original_url);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return false;

        } catch (Exception e){
            Loghandler.log(e.toString()+" check presence of url", "fatal");
        }

        return true;
    }

}
