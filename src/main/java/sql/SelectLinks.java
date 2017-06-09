package sql;

import com.sun.org.apache.bcel.internal.generic.Select;
import helper.Loghandler;
import org.json.JSONArray;
import org.json.JSONObject;
import url.Links;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 11/04/2017.
 */
public class SelectLinks extends Connect{

    // Instance of Links
    private Links link;
    private JSONArray jsonArr = new JSONArray();

    public SelectLinks(){
        this.connectToDB();
    }

    /**
     *
     * @param userID
     * @return
     * @throws Exception
     */
    public JSONArray selectLinksByUserID(int userID) throws Exception{
        String sql = "SELECT id, original_link, short_link, create_date FROM Link WHERE user_id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, userID);

            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                Loghandler.log("data is null for user id "+userID, "info");
                return null;
            }

            do {
                JSONObject link = new JSONObject();
                link.put("original_link", res.getString("original_link"))
                    .put("short_link", res.getString("short_link"))
                    .put("create_date", res.getDate("create_date"))
                    .put("id", res.getInt("id"));

                this.jsonArr.put(link);
            } while(res.next());

        } catch (SQLException e) {
            Loghandler.log("unexpected error while retrieving the link for user "+userID, "fatal");
            Loghandler.log(e.toString(), "fatal");

            throw new Exception("unable to retrieve links");
        }

        return this.jsonArr;
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
                HashMap<String, String> datas = new HashMap<String, String>();
                datas.put("original_link", res.getString("original_link"));
                datas.put("short_link", res.getString("short_link"));
                datas.put("password", res.getString("password"));
                datas.put("multiple_password", res.getString("multiple_password"));
                datas.put("mail", res.getString("mail"));

                link = new Links(
                        datas,
                        res.getInt("Id"),
                        res.getLong("hashnumber"),
                        res.getBoolean("captcha"),
                        res.getDate("start_date"),
                        res.getDate("end_date"),
                        res.getInt("set_max_use")
                );
            } while(res.next());

        } catch (SQLException e){
            Loghandler.log("exception "+e.toString(), "warn");
        }

        return link;
    }

}
