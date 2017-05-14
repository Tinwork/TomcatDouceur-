package sql;

import helper.Loghandler;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lookitsmarc on 14/05/2017.
 */
public class UserDB extends Connect{

    public UserDB(){
        // Connect to the database
        this.connectToDB();
    }

    /**
     * User Exist
     * @param username
     * @return
     */
    public boolean userExist(String username){
        String sql = "SELECT * FROM User WHERE user = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, username);

            // Execute the request
            ResultSet res = stmt.executeQuery();

            if (!res.next())
                return true;
        } catch (Exception e) {
            Loghandler.log(e.toString(), "warning");
        }

        return false;
    }

    /**
     * @since 1.8
     * @param username
     * @param hash
     * @param mail
     * @return
     */
    public boolean insertUser(String username, String hash, String salt, String mail) throws Exception{
        String sql = "INSERT INTO User (user, hash, salt, mail, subscribe_date) VALUES (?, ?, ?, ?, ?)";

        try {
            // Convert the byte[] into a String ([]byte is just an hexadecimal representation of string.. at least it should be)

            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hash);
            stmt.setString(3, salt);
            stmt.setString(4, mail);
            stmt.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));

            int insertstate = stmt.executeUpdate();

            if (insertstate == 0)
                return false;

        } catch (SQLException e) {
            Loghandler.log("SQLException " + e.toString(), "fatal");
            throw new Exception(e);
        }
        return true;
    }

    /**
     * Select Pwd
     * @param username
     * @return
     */
    public byte[][] selectPwd(String username) {
        String sql = "SELECT hash, salt FROM User WHERE user = ?";
        String pwd = "";
        String salt = "";
        byte[][] assembly = null;

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet res = stmt.executeQuery();

            if (!res.next()) {
                return null;
            }

            do {
                pwd = res.getString("hash");
                salt = res.getString("salt");

                Loghandler.log("password in res"+pwd, "info");
                Loghandler.log("salt in res"+salt, "info");
            } while(res.next());


            // Convert the String password into a hash
            byte[] bpwd = DatatypeConverter.parseBase64Binary(pwd);
            byte[] bsalt = DatatypeConverter.parseBase64Binary(salt);

            assembly = new byte[][]{bpwd, bsalt};

        } catch (SQLException e) {
            Loghandler.log(e.toString(), "warning");
        }

        return assembly;
    }
}