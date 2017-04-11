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
                        res.getDate("create_date")
                );
            }
        } catch (SQLException e){
            Loghandler.log("Not initialized", "warn");
            Loghandler.log(e.toString()+" select fatal", "fatal");
        }

        return link;
    }

}
