package sql;

import entity.CountEntity;
import helper.Helper;
import helper.Loghandler;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by lookitsmarc on 27/05/2017.
 */
public class CountURL extends ConnectionFactory {

    private JSONArray jsonArr = new JSONArray();
    private int count = 0;

    /**
     * Constructor
     */
    public CountURL(){
        super.setUp();
    }

    /**
     * @since 1.8+
     * @param id
     */
    public void updateCount(int id){
        Loghandler.log("id "+id, "info");
        Session session = this.getFactory().openSession();
        Date now = new Date();

        try {
            Transaction tr = session.getTransaction();
            tr.begin();

            CountEntity count = new CountEntity();
            count.setId_link(id);
            count.setDate(now);

            session.save(count);
            session.close();
        } catch (Exception e) {
            session.close();
            Loghandler.log(Helper.getStackTrace(e), "info");
        }
    }


    /**
     *
     * @param id
     * @return
     */
    public JSONArray getCount(int id) {
        ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();

        Session session = this.getFactory().openSession();

        try {

            Query query = session.createQuery("FROM CountEntity C WHERE C.id_link = :idLink ORDER BY date DESC");
            query.setParameter("idLink", id);
            List<CountEntity> listIterator = query.getResultList();

            if (listIterator.isEmpty())
                return new JSONArray();

            Iterator<CountEntity> iterator = listIterator.iterator();

            while(iterator.hasNext()) {
                HashMap<String, String> d = new HashMap<>();
                CountEntity tempoCount = iterator.next();

                d.put("date", tempoCount.getDate().toString());
                datas.add(d);
                count++;
            }

            session.close();
        } catch (Exception e) {
            session.close();
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
