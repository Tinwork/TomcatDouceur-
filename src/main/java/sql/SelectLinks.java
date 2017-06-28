package sql;

import entity.LinkEntity;
import helper.Helper;
import helper.Loghandler;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import url.Links;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lookitsmarc on 11/04/2017.
 */
public class SelectLinks extends ConnectionFactory{

    // Instance of Links
    private Links link;
    private JSONArray jsonArr = new JSONArray();

    public SelectLinks(){
        super.setUp();
    }

    /**
     *
     * @param userID
     * @return
     * @throws Exception
     */
    public JSONArray selectLinksByUserID(int userID) throws Exception{
        Session session = this.getFactory().openSession();

        try {
            Query query = session.createQuery("FROM LinkEntity Link WHERE Link.user_id = :userID");
            query.setParameter("userID", userID);

            List<LinkEntity> linkIterator = query.getResultList();

            if (linkIterator.isEmpty())
                return null;

            Iterator<LinkEntity> entityIterator = linkIterator.iterator();

            // Iterate threw the the result of the Query
            while(entityIterator.hasNext()) {

                LinkEntity next = entityIterator.next();

                JSONObject link = new JSONObject();
                link.put("original_link", next.getOriginaLink())
                    .put("short_link", next.getShortLink())
                    .put("craete_date", next.getCreateDate())
                    .put("id", next.getId());

                this.jsonArr.put(link);
            }

            session.close();
        } catch (Exception e) {
            Loghandler.log("select link by user id error "+e.getMessage(), "fatal");
            session.close();
            throw new Exception("unable to retrieve the links");
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

        Session session = this.getFactory().openSession();

        try {
            Loghandler.log("create !!!!!!", "info");

            Query query = session.createQuery("FROM LinkEntity Link WHERE Link.short_link = :shortLink");
            query.setParameter("shortLink", short_url);

            Loghandler.log("query fiiinndddd !!!!!!", "info");

            LinkEntity entity = (LinkEntity) query.getSingleResult();

            if (entity == null) {
                return null;
            }

            // Create a hashmap of the datas
            HashMap<String, String> datas = new HashMap<>();
            datas.put("original_link", entity.getOriginaLink());
            datas.put("short_link", entity.getShortLink());
            datas.put("password", entity.getPassword());
            datas.put("multiple_password", entity.getMultiplePwd());
            datas.put("mail", entity.getMail());

            this.link = new Links(
                    datas,
                    entity.getUserID(),
                    entity.getHashnumber(),
                    entity.getCaptcha(),
                    entity.getStartDate() == null ? null : Helper.StrToSQLDate(entity.getStartDate().toString()),
                    entity.getEndDate() == null ? null : Helper.StrToSQLDate(entity.getEndDate().toString()),
                    entity.getMaxUse()
            );

            session.close();
        } catch (Exception e) {
            Loghandler.log("exception "+Helper.getStackTrace(e), "fatal");
            session.close();
        }

        return this.link;
    }

}
