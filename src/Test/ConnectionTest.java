package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
    static final String database= "globalmusic";
    static final String host= "localhost";
    static final String databaseURL= "jdbc:mysql://"+host+"/"+database;

    public static void main(){
        Connection conn= null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(databaseURL, "root", "");
            System.out.println("Connected");
            conn.close();
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
