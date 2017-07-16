package sql;

import helper.Loghandler;
import sun.rmi.log.LogHandler;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 04/04/2017.
 */
public class Connect {
    // Define protected fields
    protected Connection connection;
    protected DataSource dataSource;
    protected String wsSql = "jdbc:mysql://tomcat-db:3306/tinwork";

    // Private fields only accessible within the class
    private String username = "root";
    private String pwd = "tinwork";

    /**
     * Connect
     *      Constructor
     */
    public Connect(){}

    /**
     * Connect To DB
     *      Connect to the database
     */
    public void connectToDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(this.wsSql+"?user="+this.username+"&password="+this.pwd);
        } catch (Exception e){
            Loghandler.log(e.toString(), "fatal");
            // we should log the error here
        }

    }
}
