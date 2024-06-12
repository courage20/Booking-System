package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    static final String database= "1718805_BOOKS";
    static final String host= "10.1.63.200";
    static final String databaseURL= "jdbc:mysql://"+host+"/"+database;

    public static Connection StartConnection(){
        Connection conn= null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(databaseURL, "cstmysql26", "mihov0mu");
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } return conn;
    }

    public static void CloseConnection(java.sql.Connection conn){
        try{
            conn.close();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

}
