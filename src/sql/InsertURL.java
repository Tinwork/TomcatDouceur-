package sql;

import helper.Loghandler;
import sun.rmi.log.LogHandler;

import javax.xml.transform.Result;
import java.sql.*;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class InsertURL {

    private String encode_url;
    private String original_url;
    private int userID;
    private Connection connection;

    /**
     * InsertURL
     *      Constructor
     * @param encodeURL String
     * @param originalURL String
     */
    public InsertURL(String encodeURL, String originalURL, Connection connection, int userID){
        this.encode_url = encodeURL;
        this.original_url = originalURL;
        this.connection = connection;
        this.userID = userID;
    }

    /**
     * Insert Data
     *      Insert datas in the database
     */
    public String insertData(){
        try {
            Boolean isPresent = this.checkPresenceOfURL();

            // if the url is present then we return the url
            if (isPresent)
                return this.encode_url;

            // Otherwise we try to insert the url in the db
            String sql = "INSERT INTO Link (original_link , short_link, user_id, create_date) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Bind the params
            stmt.setString(1, this.original_url);
            stmt.setString(2, this.encode_url);
            stmt.setInt(3, this.userID);
            stmt.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                throw new Exception("unable to add into the db");

        } catch (Exception e){
            Loghandler.log(e.toString(), "fatal");
        }

        return this.encode_url;
    }


    /**
     * Check Presence Of URL
     *      Check the presence of the original_url or the short_link
     * @return boolean
     */
    public boolean checkPresenceOfURL(){
        String sql = "SELECT * FROM Link WHERE short_link = ? OR original_link = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, this.encode_url);
            stmt.setString(2, this.original_url);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return false;

        } catch (Exception e){
            Loghandler.log(e.toString(), "fatal");
        }

        return true;
    }
}
