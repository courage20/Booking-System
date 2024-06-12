package JDBC;

import GUI.CustomerHome;
import GUI.SignIn;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Basket {

    public static void AddToBasket(String quantity,Festival festival,String ticketCode,String ticketPrice){
        String fileName= "src\\JDBC\\basket.txt";
        try {
            FileWriter basketWriter = new FileWriter(fileName,true);
            String newLine= quantity+"_"+festival.ID+"_"+ticketCode+"_"+ticketPrice+"\n";
            basketWriter.write(newLine);
            basketWriter.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void removeFromBasket(ArrayList<String[]> basket, int index){
        basket.remove(index);
        File basketFile= new File("src\\JDBC\\Basket.txt");
        basketFile.delete();
        try {
            basketFile.createNewFile();

            BufferedWriter basketWriter = new BufferedWriter(new FileWriter(basketFile,true));
            for (String[] line : basket) {
                basketWriter.write(String.join("_",line)+"\n");
            }
            basketWriter.close();
        } catch (IOException ioe){ ioe.printStackTrace(); }
    }

    public static ArrayList<String[]> readBasket(){
        ArrayList<String[]> basket = new ArrayList<>();
        try {
            File basketFile = new File("src\\JDBC\\Basket.txt");
            BufferedReader basketReader = new BufferedReader(new FileReader(basketFile));

            String line;
            while ((line = basketReader.readLine()) != null) {
                basket.add(line.split("_"));
            }
            basketReader.close();

            for (int i=0; i<basket.size();i++){
                for (int k= 0; k<basket.size();k++){
                    if (i!=k && basket.get(i)[2].equals(basket.get(k)[2])){
                        basket.get(i)[0]= String.valueOf(Integer.parseInt((basket.get(i))[0])+Integer.parseInt((basket.get(k))[0]));
                        removeFromBasket(basket,k);
                    }
                }
            }
        } catch (IOException ioe){ ioe.printStackTrace(); }
        return basket;
    }

    public static void displayBasket(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,User user,Connection conn){
        JLabel basketTitle= new JLabel("Basket",SwingConstants.CENTER);
        JButton checkout= new JButton("Checkout");

        gbc.insets= new Insets(10,10,10,10);
        Main.AddObject(basketTitle,frame,layout,gbc,30,2,1,10,10);
        Main.AddObject(checkout,frame,layout,gbc,26,3,1,10,10);

        ArrayList<String[]> basket= readBasket();

        checkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (basket.isEmpty()){
                    Main.ResetFrame(frame,gbc);
                    CustomerHome.main(frame,layout,gbc,conn,user); }
                else{
                    Main.ResetFrame(frame,gbc);
                    CheckoutBasket(frame,layout,gbc,basket,user,conn); }
            }
        });


        for (int i=0; i<basket.size();i++){
            int index= i;
            JPanel ticket= new JPanel();
            ticket.setLayout(layout);
            ticket.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            Festival currentFestival= new Festival();
            currentFestival.GetFestival(basket.get(i)[1],null,conn);
            JLabel festivalName= new JLabel(currentFestival.name);
            JLabel ticketCodeLabel= new JLabel("Ticket Code");
            JLabel ticketCode= new JLabel(basket.get(i)[2]);
            JLabel quantityLabel= new JLabel("Quantity");
            JLabel quantity= new JLabel(basket.get(i)[0]);
            JLabel totalLabel= new JLabel("Original Price");
            double totalPrice= Integer.parseInt(basket.get(i)[0])*Double.parseDouble(basket.get(i)[3]);
            JLabel total= new JLabel("£"+totalPrice);
            JLabel discountedPriceLabel= new JLabel("30% Discounted Price");
            double discounted= totalPrice*0.7;
            JLabel discountedPrice= new JLabel("£"+discounted);
            JButton removeTicket= new JButton("Remove");
            removeTicket.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeFromBasket(basket,index);
                    Main.ResetFrame(frame,gbc);
                    JButton back= new JButton("Back");
                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Main.ResetFrame(frame,gbc);
                            CustomerHome.main(frame,layout,gbc,conn,user);
                        }
                    });
                    Main.AddObject(back,frame,layout,gbc,26,1,1,10,10);
                    displayBasket(frame,layout,gbc,user,conn);
                }
            });

            gbc.insets= new Insets(5,5,5,5);
            gbc.gridheight= 2;
            Main.AddObject(festivalName,ticket,layout,gbc,26,1,1,10,10);
            gbc.gridheight= 1;
            int y= 5;
            Main.AddObject(ticketCodeLabel,ticket,layout,gbc,16,2,1,10,10);
            Main.AddObject(ticketCode,ticket,layout,gbc,20,2,2,10,10);
            Main.AddObject(quantityLabel,ticket,layout,gbc,16,3,1,10,10);
            Main.AddObject(quantity,ticket,layout,gbc,20,3,2,10,10);
            Main.AddObject(totalLabel,ticket,layout,gbc,16,4,1,10,10);
            Main.AddObject(total,ticket,layout,gbc,20,4,2,10,10);
            if (user.userType.equals("Corporate")){
                Main.AddObject(discountedPriceLabel,ticket,layout,gbc,16,5,1,10,10);
                Main.AddObject(discountedPrice,ticket,layout,gbc,20,5,2,10,10);
                y++;
            }
            gbc.gridheight= 2;
            Main.AddObject(removeTicket,ticket,layout,gbc,20,y,1,10,10);
            gbc.gridheight= 1;
            Main.AddObject(ticket,frame,layout,gbc,20,2,i+2,10,10);
        }

        JLabel ticketDeliveryLabel= new JLabel("I would like my tickets:");
        String[] ticketDeliveryOptions= {"Emailed to me","Posted to me","Sent to me as an SMS"};
        JComboBox ticketDelivery= new JComboBox(ticketDeliveryOptions);

        Main.AddObject(ticketDeliveryLabel,frame,layout,gbc,18,3,2,10,10);
        Main.AddObject(ticketDelivery,frame,layout,gbc,18,3,3,10,10);
        frame.pack();
        frame.revalidate();
    }

    public static void CheckoutBasket(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,ArrayList<String[]> basket,User user,Connection conn){
        Random ID = new Random();
        int bookingID;
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate= new Date();

        PreparedStatement query;
        String SQL= "INSERT INTO Booking VALUES (?,?,?,?,?,?);";
        for (String[] iterBasket: basket) {
            bookingID= 100000 + ID.nextInt(900000);
            try {
                query = conn.prepareStatement(SQL);
                query.setString(1, Integer.toString(bookingID));
                query.setString(2, user.ID);
                query.setString(3, iterBasket[1]);
                query.setString(4, iterBasket[2]);
                query.setString(5, iterBasket[0]);
                query.setString(6, dateFormat.format(currentDate));

                query.executeUpdate();
            } catch (SQLException se){
                se.printStackTrace();
            }
        }

        basket.clear();
        File basketFile= new File("src\\JDBC\\Basket.txt");
        basketFile.delete();
        try {
            basketFile.createNewFile();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        JButton goHome= new JButton("Home");
        JButton signOut= new JButton("Sign Out");
        goHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                CustomerHome.main(frame,layout,gbc,conn,user);
            }
        });
        signOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                DatabaseConnection.CloseConnection(conn);
                SignIn.main(frame,layout,gbc,false);
            }
        });
        JLabel ticketsBooked= new JLabel("Tickets Booked!!",SwingConstants.CENTER);
        JLabel deliverySoon= new JLabel("Your tickets will be with you soon!",SwingConstants.CENTER);

        Main.AddObject(goHome,frame,layout,gbc,26,1,1,10,10);
        Main.AddObject(ticketsBooked,frame,layout,gbc,40,2,2,10,10);
        Main.AddObject(deliverySoon,frame,layout,gbc,34,2,3,10,10);
        Main.AddObject(signOut,frame,layout,gbc,26,3,1,10,10);

        frame.pack();
        frame.revalidate();
    }
}