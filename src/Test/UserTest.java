package Test;

import JDBC.DatabaseConnection;
import JDBC.User;

import java.sql.*;

public class UserTest {
    public static void GetUser(java.sql.Connection conn, String username, String password){
        PreparedStatement query= null;
        ResultSet result= null;
        try {
            String SQLCustomer = "SELECT UserType, FirstName, LastName, OrganisationName,Address,Town," +
                    "PostCode, Email, PhoneNumber, UserName,Password,PaymentMethod, AccountNo " +
                    "FROM customer WHERE UserName=? AND Password=?;";
            query = conn.prepareStatement(SQLCustomer);
            query.setString(1, username);
            query.setString(2, password);
            result = query.executeQuery();
            result.first();

            System.out.println(result.getString("LastName"));

        } catch (SQLException se){
            se.printStackTrace();
        }
    }

    public static void displayUserInfo(User user){
        System.out.println("First Name: "+user.firstName);
        System.out.println("Last Name: "+user.lastName);
        System.out.println("Organisation Name: "+user.organisationName);
        System.out.println("Address: "+user.address);
        System.out.println("Town: "+user.town);
        System.out.println("Postcode: "+user.postcode);
        System.out.println("Email: "+user.email);
        System.out.println("Phone Number: "+user.phoneNumber);
        System.out.println("Username: "+user.username);
        System.out.println("Password: "+user.password);
        System.out.println("Payment Method: "+user.paymentMethod);
        System.out.println("Account Number: "+user.accountNo);

    }

    public static void createUser(String[] userdetails){
        java.sql.Connection conn= DatabaseConnection.StartConnection();
    }

}
