package sql;

import account.Password;
import entity.UserEntity;
import helper.Helper;
import helper.Loghandler;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class UserDB extends ConnectionFactory{

    public UserDB(){
        // Connect to the database
        super.setUp();
    }

    /**
     * User Exist
     * @param username
     * @return
     */
    public boolean userExist(String username, String mail){
        Session session = this.getFactory().openSession();

        try {
            Query query = session.createQuery("FROM UserEntity U WHERE U.user = :user OR U.mail = :mail");
            query.setParameter("user", username);
            query.setParameter("mail", mail);

            try {
                query.getSingleResult();
            } catch (NoResultException e ){
                session.close();
                return false;
            }

        } catch (Exception e) {
            session.close();
            Loghandler.log(Helper.getStackTrace(e), "warning");
        }

        return true;
    }

    /**
     * @since 1.8
     * @param username
     * @param hash
     * @param mail
     * @return
     */
    public boolean insertUser(String username, String hash, String salt, String mail, String type) throws Exception{
        Date d = new Date();
        Session session = this.getFactory().openSession();

        try {
            Transaction tr = session.getTransaction();
            tr.begin();

            UserEntity user = new UserEntity();
            user.setUser(username);
            user.setHash(hash);
            user.setSalt(salt);
            user.setMail(mail);
            user.setSubscribe_date(d);
            user.setType(UserEntity.Type.valueOf(type));
            user.setStatus(false);

            Integer insertstate = (Integer) session.save(user);

            if (insertstate == null)
                return false;

            session.close();
        } catch (Exception e) {
            session.close();
            Loghandler.log("SQLException " +Helper.getStackTrace(e), "fatal");
            throw new Exception(e);
        }

        return true;
    }

    /**
     * Select Pwd
     * @param username
     * @return
     */
    public byte[][] selectPwd(String username){
        String pwd = "";
        String salt = "";
        byte[][] assembly = null;
        UserEntity user;

        Session session = this.getFactory().openSession();

        try {
            Query query = session.createQuery("FROM UserEntity U WHERE U.user = :user AND U.status = :status");
            query.setParameter("user", username);
            query.setParameter("status", true);

            try {
                user = (UserEntity) query.getSingleResult();
            } catch(NoResultException e) {
                return null;
            }

            if (user == null)
                return assembly;

            pwd = user.getHash();
            salt = user.getSalt();

            // Convert the String password into a hash
            byte[] bpwd = DatatypeConverter.parseBase64Binary(pwd);
            byte[] bsalt = DatatypeConverter.parseBase64Binary(salt);

            assembly = new byte[][]{bsalt, bpwd};
            session.close();
        } catch (Exception e) {
            session.close();
            Loghandler.log(Helper.getStackTrace(e), "warning");
        }

        return assembly;
    }

    /**
     *
     * @param username
     * @return
     */
    public int selectUserID(String username){

        Session session = this.getFactory().openSession();
        int userID = 0;
        UserEntity user;

        try {
            Query query = session.createQuery("FROM UserEntity U WHERE U.user = :user");
            query.setParameter("user", username);


            try {
                user = (UserEntity) query.getSingleResult();
            } catch (NoResultException e) {
                session.close();
                return 1;
            }

            userID = user.getId();
            session.close();
        } catch (Exception e) {
            session.close();
            Loghandler.log(e.toString(), "fatal");
        }

        return userID;
    }

    /**
     *
     * @param username
     * @return
     */
    public String[] selectUserExtraInfo(String username) {
        Session session = this.getFactory().openSession();
        String[] data = new String[2];
        UserEntity user;

        try {
            Query query = session.createQuery("FROM UserEntity U WHERE U.user = :user");
            query.setParameter("user", username);

            try {
                user = (UserEntity) query.getSingleResult();
            } catch (NoResultException e) {
                session.close();
                return null;
            }

            data[0] = user.getMail();
            data[1] = user.getType().toString();

            session.close();
        } catch (Exception e) {
            session.close();
            Loghandler.log(e.toString(), "fatal");
        }
        return data;
    }

    /**
     *
     * @param username
     * @return
     */
    public Boolean activateUser(String username) {
        Session session = this.getFactory().openSession();

        try {
            Transaction ts = session.getTransaction();
            ts.begin();
            Query query = session.createQuery("UPDATE UserEntity U SET U.status = :status WHERE U.user = :user");
            query.setParameter("status", true);
            query.setParameter("user", username);

            Integer isUpdate = (Integer) query.executeUpdate();

            ts.commit();
            session.close();
            if (isUpdate == null)
                return false;

        } catch (Exception e) {
            session.close();
            Loghandler.log(e.toString(), "warn");
            return false;
        }

        return true;
    }

    /**
     * I am ashamed by this flag system. At least it should works great
     * @param userid
     * @param data
     * @return
     */
    public Boolean UpdateUserFlow(int userid, HashMap<String, String> data) {
        // First we need to check if the old password is valid
        String oldpwd = data.get("oldpwd");
        byte[][] dbPwd = this.selectPwd(data.get("username"));

        // we need to check if the old password is the same with the current one input
        // Checking the validity of both password..
        Password pwd = new Password(data.get("password"));
        Boolean isSame = pwd.compareHash(data.get("oldpwd").toCharArray(), dbPwd[0], dbPwd[1]);

        if (!isSame) {
            return false;
        }

        Session session = this.getFactory().openSession();

        try {
            String hashes = pwd.encrypt();
            String salt = pwd.getSalt();

            Transaction ts = session.getTransaction();
            ts.begin();

            // Now we can update the user
            Query query = session.createQuery("UPDATE UserEntity U SET U.user = :user, U.hash = :hash, U.salt = :salt, U.mail = :mail WHERE U.Id = :id");

            query.setParameter("user", data.get("username"));
            query.setParameter("hash", hashes);
            query.setParameter("salt", salt);
            query.setParameter("mail", data.get("mail"));
            query.setParameter("id", userid);

            Integer resUpdate = query.executeUpdate();

            ts.commit();
            session.close();

            if (resUpdate == null)
                return false;

        } catch (Exception e) {
            session.close();
            Loghandler.log("Password exception "+Helper.getStackTrace(e), "warn");
        }

        return true;
    }

}
