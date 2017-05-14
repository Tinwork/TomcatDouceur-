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
 * Created by lookitsmarc on 04/04/2017.
 */
public class Connect {
    // Define protected fields
    protected Connection connection;
    protected DataSource dataSource;
    protected String wsSql = "jdbc:mysql://172.21.0.2:3306/tinwork";

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

    /**
     * Simple Select
     *      Select data from table with where conditions or not
     * @param table String
     * @param wanted []String
     * @param filter []String
     */
    public  <T extends Comparable <T>> ResultSet simpleSelect(String table, String[] wanted, String[] filter, T[] data){
        String sql = "";
        ResultSet res = null;

        try {

            Statement stmt = this.connection.createStatement();
            // check the wanted params
            if (wanted.length == 0)
                sql += "SELECT * FROM";
            else
                for (int i = 0; i < wanted.length; i++){
                    if (i == 0)
                        sql += "SELECT "+wanted[i];
                    else if (i == wanted.length - 1)
                        sql += wanted[i]+" FROM "+table;
                    else
                        sql += wanted[i];
                }


            // check the filter param
            if (filter.length != 0)
                for (int i = 0; i < filter.length; i++){
                    if (i == 0)
                        sql += " WHERE "+filter[0]+"="+data[0];
                    else
                        sql += " AND "+filter[i]+"="+data[i];
                }

            // Execute the statement
            res = stmt.executeQuery(sql);

        } catch (Exception e){
            Loghandler.log(e.toString(), "fatal");
        }

        return res;
    }


}
