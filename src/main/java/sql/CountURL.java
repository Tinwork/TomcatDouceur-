package sql;

import helper.Loghandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 27/05/2017.
 */
public class CountURL extends Connect {

    private JSONArray jsonArr = new JSONArray();

    /**
     * Constructor
     */
    public CountURL(){
        super.connectToDB();
    }

    /**
     * @since 1.8+
     * @param id
     */
    public void updateCount(int id){
        String sql = "INSERT INTO count (id_link, date) VALUES (?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            Loghandler.log(e.toString(), "info");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public JSONArray getCount(int id) {
        String sql = "SELECT date FROM count WHERE id_link = ? ORDER BY date DESC";
        JSONObject data = new JSONObject();
        Boolean flag = false;
        int counter = 0;
        HashMap<String, String> tempdate = new HashMap<String, String>();

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return null;

            // As getting the last row break everything we must find a walkaround...
            do {

                if (!flag) {
                    tempdate.put("date", res.getDate("date").toString());
                    flag = true;
                }

                if (!tempdate.containsValue(res.getDate("date").toString())) {
                    tempdate.put("date", res.getDate("date").toString());
                    data.put("date", res.getDate("date").toString());
                    data.put("counter", counter);
                    jsonArr.put(data);

                    // reset
                    data = new JSONObject();
                    counter = 0;
                }
                 counter++;
            } while (res.next());

            // in the case that there's only one date
            Loghandler.log("size "+tempdate.size(), "info");
            if (tempdate.size() == 1) {
                Loghandler.log("temp "+tempdate.toString(), "info");
                data.put("date", tempdate.get("date"));
                data.put("counter", counter);
                jsonArr.put(data);

                // reset
                data = new JSONObject();
                counter = 0;
            }

        } catch (SQLException e) {
            Loghandler.log(e.toString(), "info");
        }

        return this.jsonArr;
    }

}
