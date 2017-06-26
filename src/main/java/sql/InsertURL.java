package sql;

import entity.LinkEntity;
import helper.Loghandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONObject;
import sun.plugin.javascript.navig.Link;

import javax.persistence.EntityManager;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class InsertURL extends ConnectionFactory {

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
        super.setUp();
    }

    /**
     * Insert Data
     *      Insert datas in the database
     */
    public int insertOriginalURL(String password, String mail, Date start, Date end, Boolean captcha, JSONObject json, int max_use){
        Date d = new Date();
        Integer insert = null;

        try {
            SessionFactory factory = this.getFactory();
            Session session = factory.openSession();

            Transaction tr = session.getTransaction();
            tr.begin();

            LinkEntity link = new LinkEntity();
            link.setOriginaLink(this.original_url);
            link.setUserID(this.userID);
            link.setCreateDate(d);
            link.setPassword(password);
            link.setMail(mail);
            link.setStartDate(start);
            link.setEndDate(end);
            link.setCaptcha(captcha);
            link.setMultiplePwd(json.length() == 0 ? null : json.toString());
            link.setMaxUse(max_use == 0 ? null : max_use);


            insert = (Integer) session.save(link);
            tr.commit();
        } catch(Exception e) {
            Loghandler.log("Hibernate insert url error "+e.toString(), "fatal");
        }

        return insert;
    }


    /**
     * Check Presence Of URL
     *      Check the presence of the original_url or the short_link
     * @return boolean
     */
    public String checkPresenceOfURL() throws Exception{
        String sURL = null;

       try {
           EntityManager en = null;
           Session se = this.getFactory().openSession();

           if (se == null) {
               Loghandler.log("session is null ! ", "fatal");
           }

           Query query = se.createQuery("FROM LinkEntity Link where Link.original_link = :url");
           query.setParameter("url", this.original_url);

           LinkEntity link = (LinkEntity) query.uniqueResult();

           if (link == null) {
             return null;
           }

           sURL = link.getShortLink();
       } catch (Exception e) {
            Loghandler.log("Get present url "+e, "fatal");
       }

       return sURL;
    }

    /**
     * Check Presence Of Short URL
     * @return
     */
    public boolean checkPresenceOfShortURL(String shortURL){
        try {
            Session se = this.getFactory().getCurrentSession();

            if (se == null)
                se = this.getFactory().openSession();

            Query query = se.createQuery("FROM * LinkEntity Link WHERE Link.short_link = :shortLink");
            query.setParameter("shortLink", shortURL);

            LinkEntity link = (LinkEntity) query.uniqueResult();

            if (link == null)
                return false;

        } catch (Exception e) {
            Loghandler.log("check presence error "+e.toString(), "fatal");
        }

      return true;
    }


    /**
     *
     * @param hash
     * @param shortURL
     * @throws Exception
     */
    public void insertShortLink(long hash, String shortURL, int row) throws Exception{

       try {
            Session session = this.getFactory().openSession();
            Transaction ts = session.getTransaction();
            ts.begin();

           /** LinkEntity entity = new LinkEntity();
            entity.setId(row);
            entity.setHashnumber(hash);
            entity.setShortLink(shortURL);**/

           Query query = session.createQuery("UPDATE LinkEntity Link SET Link.hashnumber = :hashnumber, Link.short_link = :short_link");
           query.setParameter("hashnumber", hash);
           query.setParameter("short_link", shortURL);
           query.executeUpdate();

            //session.update(entity);
            //ts.commit();

       } catch (Exception e) {
            Loghandler.log("Update link has failed "+e.toString(), "fatal");
       }
    }
}
