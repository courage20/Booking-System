package JDBC;


import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class AdminFunctions {
    public static boolean addPerformer(Connection conn, String[] performerDetails){
        PreparedStatement query;
        Random ID = new Random();
        int performerID = 100000 + ID.nextInt(900000);
        boolean performerAdded= false;

        try{
            String SQL= "INSERT INTO Band VALUES (?,?,?,?,?);";
            query= conn.prepareStatement(SQL);
            query.setString(1,String.valueOf(performerID));
            query.setString(2,performerDetails[1]);
            query.setString(3,performerDetails[2]);
            query.setString(4,performerDetails[3]);
            query.setString(5,performerDetails[0]);
            query.executeUpdate();
            performerAdded= true;

        } catch (SQLException sqle){
            sqle.printStackTrace();
        } return performerAdded;
    }

    public static ArrayList<String[]> GetCorporateCustomers(Connection conn){
        Statement query;
        ResultSet customerResult;
        String SQL= "SELECT Customer_ID,Organisation_Name FROM Customer WHERE User_Type='Corporate'";
        ArrayList<String[]> customerList= new ArrayList<>();

        try{
            query= conn.createStatement();
            customerResult= query.executeQuery(SQL);

            if (!customerResult.next()){
                return customerList;
            } else{
                customerResult.beforeFirst();
                while (customerResult.next()){
                    String[] row= {customerResult.getString(1),customerResult.getString(2)};
                    customerList.add(row);
                }
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } return customerList;
    }

    public static ArrayList<String[]> CreateInvoice(Connection conn, String customerID){
        PreparedStatement query;
        ResultSet customerInfo;
        ResultSet bookings;
        ArrayList<String[]> invoiceList= new ArrayList<>();

        String customerSQL= "SELECT Organisation_Name, Address, Town, Post_Code, Email, Phone_Number FROM Customer WHERE Customer_ID=?;";
        String bookingSQL= "SELECT Booking_ID,Festival_Name,Booking.Ticket_Code,Quantity,Ticket_Price,Date FROM Booking,Festival,Ticket WHERE " +
                "Booking.Customer_ID=? AND Booking.Festival_ID=Festival.Festival_ID AND Booking.Ticket_Code=Ticket.Ticket_Code AND Booking.Date>CURDATE()- INTERVAL 1 MONTH";

        try{
            query= conn.prepareStatement(customerSQL);
            query.setString(1,customerID);
            customerInfo= query.executeQuery();

            customerInfo.first();
            String[] customerInfoList= new String[8];

            Random ID = new Random();
            String invoiceID = Integer.toString(100000 + ID.nextInt(900000));

            customerInfoList[0]= invoiceID;
            customerInfoList[1]= customerID;
            customerInfoList[2]= customerInfo.getString("Organisation_Name");
            customerInfoList[3]= customerInfo.getString("Address");
            customerInfoList[4]= customerInfo.getString("Town");
            customerInfoList[5]= customerInfo.getString("Post_Code");
            customerInfoList[6]= customerInfo.getString("Email");
            customerInfoList[7]= customerInfo.getString("Phone_Number");
            invoiceList.add(customerInfoList);

            query= conn.prepareStatement(bookingSQL);
            query.setString(1,customerID);
            bookings= query.executeQuery();


            bookings.beforeFirst();
            while (bookings.next()){
                String[] bookingDetails= new String[6];
                bookingDetails[0]= bookings.getString("Booking_ID");
                bookingDetails[1]= bookings.getString("Festival_Name");
                bookingDetails[2]= bookings.getString("Ticket_Code");
                bookingDetails[3]= bookings.getString("Quantity");
                bookingDetails[4]= String.valueOf(Integer.parseInt(bookings.getString("Quantity"))*Double.parseDouble(bookings.getString("Ticket_Price"))*0.7);
                String date= bookings.getString("Date");
                String[] dateSplit1= date.split("-");
                String[] dateSplit2= {dateSplit1[2],dateSplit1[1],dateSplit1[0]};
                bookingDetails[5]= String.join("-",dateSplit2);
                invoiceList.add(bookingDetails);
            }

        } catch (SQLException sqle){
            sqle.printStackTrace();
        } return invoiceList;
    }

    public static ArrayList<String[]> getTable(Connection conn, String tableType){
        ArrayList<String[]> table= new ArrayList<>();
        ResultSet tableResult;
        PreparedStatement query;
        String SQL= "SELECT * FROM "+tableType;
        try{
            query= conn.prepareStatement(SQL);
            tableResult= query.executeQuery();
            ResultSetMetaData tableMetaData= tableResult.getMetaData();
            int y= tableMetaData.getColumnCount();
            if (!tableResult.next()){
                table= null;
                return table;
            } else{
                String[] columnNames= new String[y];
                for (int i= 0; i<y; i++){ columnNames[i] = tableMetaData.getColumnName(i+1); }
                table.add(columnNames);

                tableResult.beforeFirst();
                while (tableResult.next()){
                    String[] rowEntry= new String[y];
                    for (int j= 0; j<y; j++) {
                        rowEntry[j] = tableResult.getString(j + 1);
                    }
                    table.add(rowEntry);
                }
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } return table;
    }

    public static void DeleteTableRow(Connection conn, String[] tableDetails){
        PreparedStatement query;
        String SQL= "DELETE FROM "+tableDetails[0]+" WHERE "+tableDetails[2]+"="+tableDetails[1];

        try{
            query= conn.prepareStatement(SQL);
            query.executeUpdate();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

    }
}
