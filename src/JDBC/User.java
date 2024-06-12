package JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class User {
    public String ID;
    public String userType;
    public String firstName;
    public String lastName;
    public String organisationName;
    public String organiserName;
    public String address;
    public String town;
    public String postcode;
    public String email;
    public String phoneNumber;
    public String username;
    public String password;
    public String paymentMethod;
    public String accountNo;
//
    public static void RemoveUser(Connection conn, User user){
        PreparedStatement query= null;
        String SQL;
        if (user.userType.equals("Individual") || user.userType.equals("Corporate")){
            SQL= "DELETE FROM Customer WHERE Customer_ID=?;";
        } else{ SQL= "DELETE FROM Organiser WHERE Organiser_ID=?;"; }

        try{
            query= conn.prepareStatement(SQL);
            query.setString(1,user.ID);
            query.executeUpdate();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public boolean CreateNewUserCustomer(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Connection conn, String[] userDetails){
        boolean userAdded= false;

        this.userType= userDetails[0]; // These brackets are indexes, allows to store seperate values.
        this.firstName= userDetails[1];
        this.lastName= userDetails[2];
        this.organisationName= userDetails[3];
        this.address= userDetails[5];
        this.town= userDetails[6];
        this.postcode= userDetails[7];
        this.email= userDetails[8];
        this.phoneNumber= userDetails[9];
        this.username= userDetails[10];
        this.password= userDetails[11];
        this.paymentMethod= userDetails[12];
        this.accountNo= userDetails[13];

        PreparedStatement query= null;
        try{
            Random ID = new Random();
            this.ID = Integer.toString(100000 + ID.nextInt(900000));

            String SQL= "INSERT INTO Customer VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            query= conn.prepareStatement(SQL);
            query.setString(1,this.ID);
            query.setString(2,this.userType);
            query.setString(3,this.firstName);
            query.setString(4,this.lastName);
            query.setString(5,this.organisationName);
            query.setString(6,this.address);
            query.setString(7,this.town);
            query.setString(8,this.postcode);
            query.setString(9,this.email);
            query.setString(10,this.phoneNumber);
            query.setString(11,this.username);
            query.setString(12,this.password);
            query.setString(13,this.paymentMethod);
            query.setString(14,this.accountNo);

            query.executeUpdate();
            userAdded= true;
        } catch (SQLException se){
            return userAdded;
        } return userAdded;
    }

    public boolean CreateNewUserOrganiser(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Connection conn, String[] userDetails){
        boolean userAdded= false;
        this.userType= userDetails[0];
        this.organiserName= userDetails[4];
        this.email= userDetails[8];
        this.phoneNumber= userDetails[9];
        this.username= userDetails[10];
        this.password= userDetails[11];

        PreparedStatement query= null;
        try{
            Random ID = new Random();
            int organiserID = 100000 + ID.nextInt(900000);
            String SQL= "INSERT INTO Organiser VALUES (?,?,?,?,?,?,?);";

            query= conn.prepareStatement(SQL);
            query.setString(1,String.valueOf(organiserID));
            query.setString(2,this.organiserName);
            query.setString(3,this.phoneNumber);
            query.setString(4,this.email);
            query.setString(5,this.username);
            query.setString(6,this.password);
            query.setString(7,this.userType);

            query.executeUpdate();
            userAdded= true;
        } catch (SQLException se){
            return userAdded;
        } return userAdded;
    }

    public boolean getUserInfo(Connection conn, String username, String password, JFrame frame, GridBagLayout layout, GridBagConstraints gbc){
        boolean userFound= false;
        PreparedStatement query;
        ResultSet resultCustomer;
        ResultSet resultOrganiser;
        ResultSet resultAdmin;
        try{
            String SQLCustomer= "SELECT * FROM Customer WHERE User_Name=? AND Password=?;";
            query= conn.prepareStatement(SQLCustomer);
            query.setString(1,username); // Parameter index is important because if you entered username instead of password it would not work.
            query.setString(2,password);
            resultCustomer= query.executeQuery();
            resultCustomer.beforeFirst();

            String SQLOrganiser= "SELECT * FROM Organiser WHERE User_Name=? AND Password=?;";
            query= conn.prepareStatement(SQLOrganiser);
            query.setString(1,username);
            query.setString(2,password);
            resultOrganiser= query.executeQuery();
            resultOrganiser.beforeFirst();

            String SQLAdmin= "SELECT * FROM Admin WHERE User_Name=? AND Password=?;";
            query= conn.prepareStatement(SQLAdmin);
            query.setString(1,username);
            query.setString(2,password);
            resultAdmin= query.executeQuery();
            resultAdmin.beforeFirst();

            if (!resultCustomer.isBeforeFirst() && !resultOrganiser.isBeforeFirst() && !resultAdmin.isBeforeFirst()){
                userFound= false;
            } if (resultCustomer.isBeforeFirst() && !resultOrganiser.isBeforeFirst() && !resultAdmin.isBeforeFirst()) {
                resultCustomer.first();
                this.ID = resultCustomer.getString("Customer_ID");
                this.userType = resultCustomer.getString("User_Type");
                this.firstName = resultCustomer.getString("First_Name");
                this.lastName = resultCustomer.getString("Last_Name");
                this.organisationName = resultCustomer.getString("Organisation_Name");
                this.address = resultCustomer.getString("Address");
                this.town = resultCustomer.getString("Town");
                this.postcode = resultCustomer.getString("Post_Code");
                this.email = resultCustomer.getString("Email");
                this.phoneNumber = resultCustomer.getString("Phone_Number");
                this.username = resultCustomer.getString("User_Name");
                this.password = resultCustomer.getString("Password");
                this.paymentMethod = resultCustomer.getString("Payment_Method");
                this.accountNo = resultCustomer.getString("Account_No");
                userFound= true;
            } if (resultOrganiser.isBeforeFirst() && !resultCustomer.isBeforeFirst() && !resultAdmin.isBeforeFirst()){
                resultOrganiser.first();
                this.ID = resultOrganiser.getString("Organiser_ID");
                this.userType = resultOrganiser.getString("User_Type");
                this.organiserName = resultOrganiser.getString("Organiser_Name");
                this.email = resultOrganiser.getString("Email");
                this.phoneNumber = resultOrganiser.getString("Contact_Number");
                this.username = resultOrganiser.getString("User_Name");
                this.password = resultOrganiser.getString("Password");
                userFound= true;
            } if (resultAdmin.isBeforeFirst() && !resultCustomer.isBeforeFirst() && !resultOrganiser.isBeforeFirst()){
                resultAdmin.first();
                this.ID = resultAdmin.getString("Admin_ID");
                this.userType = resultAdmin.getString("User_Type");
                this.username = resultAdmin.getString("User_Name");
                this.password = resultAdmin.getString("Password");
                userFound= true;
            }
        } catch (SQLException se){
            se.printStackTrace();
        } return userFound;
    }
}

// Use of result sets to find and store user information learnt from:
// https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
