package sql;

import helper.Loghandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 27/05/2017.
 */
public class CountURL extends Connect {

    private JSONArray jsonArr = new JSONArray();
    private int count = 0;

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
        ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return new JSONArray();

            do {
                HashMap<String, String> d = new HashMap<>();
                d.put("date", res.getDate("date").toString());
                datas.add(d);
                count++;
            } while (res.next());

        } catch (SQLException e) {
            Loghandler.log(e.toString(), "info");
        }


        return this.buildDatas(datas);
    }

    /**
     *
     * @param d
     * @return
     */
    public JSONArray buildDatas(ArrayList<HashMap<String, String>> d) {

        JSONObject json = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        String tempdate = d.get(0).get("date");

        int idx = 0;
        int click = 0;
        while(idx < d.size()) {

            HashMap<String, String> date = d.get(idx);

            if (!tempdate.equals(date.get("date"))) {
                // set the date
                json.put("counter", click);
                json.put("date", tempdate);
                jsonArr.put(json);

                // reset
                json = new JSONObject();
                tempdate = date.get("date");
                click = 0;
            }

            // if the ArrayList is going to end
            if (idx == d.size() - 1) {
                click++;
                json.put("counter", click);
                json.put("date", tempdate);
                jsonArr.put(json);
            }


            click++;
            idx++;
        }

        return jsonArr;
    }

    /**
     * Get Count
     * @return
     */
    public int getCount() {
        return this.count;
    }
}
