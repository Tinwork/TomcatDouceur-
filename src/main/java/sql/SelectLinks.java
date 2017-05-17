package sql;

import com.sun.org.apache.bcel.internal.generic.Select;
import helper.Loghandler;
import url.Links;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by lookitsmarc on 11/04/2017.
 */
public class SelectLinks extends Connect{

    // Instance of Links
    private Links link;

    public SelectLinks(){
        this.connectToDB();
    }

    /**
     * Retrieve SQL Link
     * @return Links
     */
    public Links retrieveSQLLink(int row){
        Loghandler.log("row :"+row, "info");
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM Link WHERE id = ?");
            stmt.setInt(1, row);

            // Execute the query
            ResultSet res = stmt.executeQuery();

            while(res.next()){
                link = new Links(
                        res.getString("original_link"),
                        res.getString("short_link"),
                        res.getInt("Id"),
                        res.getInt("count"),
                        res.getDate("create_date"),
                        res.getLong("hashnumber"),
                        res.getString("password"),
                        res.getString("multiple_password"),
                        res.getBoolean("captcha"),
                        res.getString("mail"),
                        res.getDate("start_date"),
                        res.getDate("end_date")
                );
            }
        } catch (SQLException e){
            Loghandler.log("Not initialized", "warn");
            Loghandler.log(e.toString()+" select fatal", "fatal");
        }

        return link;
    }

    /**
     * Select Links By Short Link
     * @param short_url
     * @return
     * @throws Exception
     */
    public Links SelectLinksByShortLink(String short_url) throws Exception{

        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM Link WHERE short_link = ?");
            stmt.setString(1, short_url);

            ResultSet res = stmt.executeQuery();

            if (!res.next()){
                return null;
            }

            do{
                link = new Links(
                        res.getString("original_link"),
                        res.getString("short_link"),
                        res.getInt("Id"),
                        res.getInt("count"),
                        res.getDate("create_date"),
                        res.getLong("hashnumber"),
                        res.getString("password"),
                        res.getString("multiple_password"),
                        res.getBoolean("captcha"),
                        res.getString("mail"),
                        res.getDate("start_date"),
                        res.getDate("end_date")
                );
            } while(res.next());

        } catch (SQLException e){
            Loghandler.log("exception "+e.toString(), "warn");
        }

        return link;
    }

}
