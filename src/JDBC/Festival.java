package JDBC;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Random;

import GUI.*;
import Main.Main;

public class Festival {
    String ID;
    String name;
    String startDate;
    String endDate;
    String address;
    String PostCode;
    String organiser;
    String OrganiserEmail;
    String[][] Performer;
    String[][] Ticket;

    public static boolean AddTicketToFestival(Connection conn,String[] details){
        boolean addSuccessful= false;
        PreparedStatement query= null;
        ResultSet festivalIDResult= null;
        String SQL;

        Random code = new Random();
        String ticketCode = Integer.toString(100000 + code.nextInt(900000));

        try{
            SQL= "SELECT Festival_ID FROM Festival WHERE Festival_Name=?;";
            query= conn.prepareStatement(SQL);
            query.setString(1,details[0]);
            festivalIDResult= query.executeQuery();
            festivalIDResult.first();
            String festivalID= festivalIDResult.getString(1);

            SQL= "INSERT INTO Ticket VALUES (?,?,?,?)";
            query= conn.prepareStatement(SQL);
            query.setString(1,festivalID);
            query.setString(2,ticketCode);
            query.setFloat(3,Float.valueOf(details[2]));
            query.setString(4,details[1]);
            query.executeUpdate();

            addSuccessful= true;
        } catch(SQLException sqle){
            sqle.printStackTrace();
        } return addSuccessful;
    }

    public static boolean AddPerformerToFestival(Connection conn,String[] details) {
        boolean addSuccessful= false;
        PreparedStatement query= null;
        String SQL;
        ResultSet festivalPerformerID;

        try{
            SQL= "SELECT Festival_ID, Band_ID FROM Festival,Band WHERE Festival.Festival_Name=? AND Band.Band_Name=? ;";
            query= conn.prepareStatement(SQL);
            query.setString(1,details[0]);
            query.setString(2,details[1]);
            festivalPerformerID= query.executeQuery();

            SQL= "INSERT INTO Band_Festival VALUES(?,?,?);";
            System.out.println(details[0]);
            System.out.println(details[1]);
            festivalPerformerID.first();
            System.out.println(festivalPerformerID.getString(1));
            System.out.println(festivalPerformerID.getString(2));
            festivalPerformerID.first();
            query= conn.prepareStatement(SQL);
            query.setString(1,festivalPerformerID.getString("Festival_ID"));
            query.setString(2,festivalPerformerID.getString("Band_ID"));
            query.setString(3,details[2]);

            query.executeUpdate();
            addSuccessful= true;

            String fileName= "src\\GUI\\Notifications.txt";
            FileWriter notificationsWriter = new FileWriter(fileName, true);
            String newLine = details[1]+" is coming to "+details[0]+"!\n";
            notificationsWriter.write(newLine);
            notificationsWriter.close();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } catch (IOException ioe){ ioe.printStackTrace(); }
        return addSuccessful;
    }

    public boolean AddFestival(Connection conn, String[] festivalDetails){
        PreparedStatement query= null;
        String SQL;
        boolean festivalAdded= false;

        Random ID = new Random();
        this.ID = Integer.toString(100000 + ID.nextInt(900000));
        this.organiser= festivalDetails[0];
        this.name= festivalDetails[1];
        this.startDate= festivalDetails[2];
        this.endDate= festivalDetails[3];
        this.address= festivalDetails[4];
        this.PostCode= festivalDetails[5];

        try{
            SQL= "INSERT INTO Festival VALUES (?,?,?,?,?,?,?);";
            query= conn.prepareStatement(SQL);
            query.setString(1,this.ID);
            query.setString(2,this.organiser);
            query.setString(3,this.name);
            query.setString(4,this.startDate);
            query.setString(5,this.endDate);
            query.setString(6,this.address);
            query.setString(7,this.PostCode);

            query.executeUpdate();

            String fileName= "src\\GUI\\Notifications.txt";
            FileWriter notificationsWriter = new FileWriter(fileName,true);
            String newLine= "New festival '"+this.name+"' has been added!\nBook your tickets now!\n";
            notificationsWriter.write(newLine);
            notificationsWriter.close();

            festivalAdded= true;
        } catch (SQLException sqle){
            return festivalAdded;
        } catch (IOException ioe){
            return festivalAdded;
        } return festivalAdded;
    }

    public void RemoveFestival(Connection conn){
        PreparedStatement query= null;
        String SQL;

        try{
            SQL= "DELETE FROM Festival WHERE Festival_ID=?;";
            query=conn.prepareStatement(SQL);
            query.setString(1,this.ID);
            query.executeUpdate();

            String fileName= "src\\GUI\\Notifications.txt";
            FileWriter notificationsWriter = new FileWriter(fileName,true);
            String newLine= this.name+" has been cancelled!\nYour tickets may have been cancelled :(\n";
            notificationsWriter.write(newLine);
            notificationsWriter.close();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static String[][] GetAllFestivals(Connection conn, User user, String upcoming) {
        ResultSet result;
        PreparedStatement query= null;
        String[][] festivals= null;
        String SQL= null;
        try{
            if (upcoming.equals("upcoming")){
                SQL = "SELECT Festival_Name, Festival_Start_Date FROM Festival WHERE Festival_Start_Date>CURDATE();";
                query= conn.prepareStatement(SQL);
            } else if (upcoming.equals("previous")){
                SQL = "SELECT Festival_Name, Date FROM Festival,Booking WHERE Booking.Customer_ID=? AND Booking.Festival_ID=Festival.Festival_ID;";
                query= conn.prepareStatement(SQL);
                query.setString(1,user.ID);
            } else if (upcoming.equals("organiserSpecific")){
                SQL = "SELECT Festival_Name, Festival_Start_Date FROM Festival WHERE Organiser_ID=?;";
                query= conn.prepareStatement(SQL);
                query.setString(1,user.ID);
            }
            result= query.executeQuery();

            if (!result.next()){ return festivals; }
            int i= 0;
            result.last();
            festivals = new String[result.getRow()][2];
            result.beforeFirst();

            while (result.next()){
                String dateString;
                festivals[i][0]= result.getString("Festival_Name");
                if (upcoming.equals("upcoming") || upcoming.equals("organiserSpecific")) { dateString= result.getString("Festival_Start_Date"); }
                else { dateString= result.getString("Date"); }
                String[] dateSplit1= dateString.split("-");
                String[] dateSplit2= {dateSplit1[2],dateSplit1[1],dateSplit1[0]};
                festivals[i++][1]= String.join("-",dateSplit2);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return festivals;
    }

    public static void DisplayAllFestivals(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, String[][] festivals, String upcoming, Connection conn, User user){
        JButton back= new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                if (upcoming.equals("organiserSpecific")){ GUI.OrganiserHome.main(frame,layout,gbc,conn,user); }
                else{ CustomerHome.main(frame,layout,gbc,conn,user); }
            }
        });

        JButton basket= new JButton("Basket");
        if (upcoming.equals("organiserSpecific")){ basket.setText("SignOut"); }
        basket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                if (upcoming.equals("organiserSpecific")) {
                    DatabaseConnection.CloseConnection(conn);
                    SignIn.main(frame, layout, gbc, false);
                } else {
                    JButton back = new JButton("Back");
                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Main.ResetFrame(frame, gbc);
                            DisplayAllFestivals(frame, layout, gbc, festivals, upcoming, conn, user);
                        }
                    });
                    gbc.insets = new Insets(10, 10, 10, 10);
                    Main.AddObject(back, frame, layout, gbc, 26, 1, 1, 10, 10);
                    Basket.displayBasket(frame, layout, gbc, user, conn);
                }
            }
        });

        JLabel upcomingFestivals= new JLabel("Festivals",SwingConstants.CENTER);

        gbc.insets= new Insets(10,10,10,10);
        Main.AddObject(back,frame,layout,gbc,20,1,0,30,30);
        Main.AddObject(upcomingFestivals,frame,layout,gbc,26,2,0,40,20);
        Main.AddObject(basket,frame,layout,gbc,20,3,0,30,30);

        if (festivals== null){
            JLabel noFestivals= new JLabel("Nothing to see here :(",SwingConstants.CENTER);
            JLabel noFestivals2= new JLabel("There are no festivals to display",SwingConstants.CENTER);
            Main.AddObject(noFestivals,frame,layout,gbc,40,2,1,10,10);
            Main.AddObject(noFestivals2,frame,layout,gbc,34,2,2,10,10);
        } else {
            for (int i = 0, y = 1; i < festivals.length; i++, y++) {
                gbc.weightx = 1;
                JPanel festival = new JPanel();
                festival.setLayout(layout);
                festival.setBorder(BorderFactory.createLineBorder(Color.black));
                String festivalName = festivals[i][0];
                String festivalDate = festivals[i][1];
                JLabel dateLabel;
                JLabel festivalNameLabel = new JLabel(festivalName);
                if (upcoming.equals("upcoming") || upcoming.equals("organiserSpecific")){
                    dateLabel= new JLabel("Festival start date:");
                } else{
                    dateLabel= new JLabel("Date Booked:");
                }
                JLabel festivalDateLabel = new JLabel(festivalDate);
                JButton viewDetails = new JButton("View Details");
                viewDetails.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame, gbc);
                        Festival newFestival = new Festival();
                        newFestival.GetFestival(null, festivalName, conn);
                        Festival.DisplayFestival(frame, layout, gbc, newFestival, upcoming, conn, user);
                    }
                });

                gbc.insets = new Insets(5, 10, 5, 10);
                gbc.gridheight= 2;
                Main.AddObject(festivalNameLabel, festival, layout, gbc, 24, 1, 1, 10, 10);
                gbc.gridheight= 1;
                Main.AddObject(dateLabel, festival, layout, gbc, 20, 2, 1, 10, 10);
                Main.AddObject(festivalDateLabel, festival, layout, gbc, 24, 2, 2, 10, 10);
                gbc.gridheight= 2;
                Main.AddObject(viewDetails, festival, layout, gbc, 24, 3, 1, 10, 10);
                gbc.gridheight= 1;
                gbc.insets = new Insets(20, 10, 20, 10);
                Main.AddObject(festival, frame, layout, gbc, 24, 2, y, 10, 10);
            }
        }

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public void GetFestival(String festivalID, String name, Connection conn){

        ResultSet result;
        PreparedStatement query= null;
        try {
            if (festivalID == null && name == null) {
                System.out.println("ID and name are empty");
            } else if (festivalID == null) {
                String SQL = "SELECT * FROM Festival WHERE Festival_Name=?";
                query = conn.prepareStatement(SQL);
                query.setString(1,name);
            } else if (name==null){
                String SQL = "SELECT * FROM Festival WHERE Festival_ID=?";
                query = conn.prepareStatement(SQL);
                query.setString(1,festivalID);
            } else if (festivalID!=null && name!=null){
                String SQL = "SELECT * FROM Festival WHERE Festival_ID=?";
                query = conn.prepareStatement(SQL);
                query.setString(1,festivalID);
            }

            result= query.executeQuery();
            result.first();

            this.ID= result.getString("Festival_ID");
            this.name= result.getString("Festival_Name");
            String startDate= result.getString("Festival_Start_Date");
            String[] startDateSplit1= startDate.split("-");
            String[] startDateSplit2= {startDateSplit1[2],startDateSplit1[1],startDateSplit1[0]};
            this.startDate= String.join("-",startDateSplit2);
            String endDate= result.getString("Festival_End_Date");
            String[] endDateSplit1= endDate.split("-");
            String[] endDateSplit2= {endDateSplit1[2],endDateSplit1[1],endDateSplit1[0]};
            this.endDate= String.join("-",endDateSplit2);
            this.address= result.getString("Address");
            this.PostCode= result.getString("Post_Code");

            String SQL= "SELECT Organiser_Name, Email FROM Organiser, Festival " +
                    "WHERE Festival.Organiser_ID=Organiser.Organiser_ID AND Festival.Festival_ID=?";
            query= conn.prepareStatement(SQL);
            query.setString(1,this.ID);
            result= query.executeQuery();
            result.first();

            this.organiser= result.getString("Organiser_Name");
            this.OrganiserEmail= result.getString("Email");

            SQL= "SELECT Band_Name, Performance_Date FROM Band,Band_Festival " +
                    "WHERE Band_Festival.Festival_ID=? AND Band.Band_ID=Band_Festival.Band_ID";
            query= conn.prepareStatement(SQL);
            query.setString(1,this.ID);
            result= query.executeQuery();
            result.last();
            int i=0;
            this.Performer= new String[result.getRow()][2];
            result.beforeFirst();

            while (result.next()){
                this.Performer[i][0]=result.getString("Band_Name");
                String dateString= result.getString("Performance_Date");
                String[] dateSplit1= dateString.split("-");
                String[] dateSplit2= {dateSplit1[2],dateSplit1[1],dateSplit1[0]};
                this.Performer[i++][1]=String.join("-",dateSplit2);
            }


            SQL= "SELECT Ticket_Code,Ticket_Desc,Ticket_Price FROM Ticket " +
                    "WHERE Ticket.Festival_ID=?";
            query= conn.prepareStatement(SQL);
            query.setString(1,this.ID);
            result= query.executeQuery();
            result.last();
            i= 0;
            this.Ticket= new String[result.getRow()][3];
            result.beforeFirst();

            while (result.next()){
                this.Ticket[i][0]= result.getString("Ticket_Code");
                this.Ticket[i][1]= result.getString("Ticket_Desc");
                this.Ticket[i++][2]= result.getString("Ticket_Price");
            }


            result.close();
            query.close();
        } catch (SQLException sq){
            sq.printStackTrace();
        }
    }



    public static void DisplayFestival(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Festival festival, String upcoming, Connection conn, User user){

        JPanel buttons= new JPanel();
        buttons.setLayout(layout);
        JButton back= new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                String[][] festivals= GetAllFestivals(conn,user,upcoming);
                DisplayAllFestivals(frame,layout,gbc,festivals,upcoming,conn,user);
            }
        });

        JButton basket= new JButton("Basket");
        if (upcoming.equals("organiserSpecific")){ basket.setText("Edit Details"); }
        basket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                JButton back= new JButton("Back");
                back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame,gbc);
                        DisplayFestival(frame,layout,gbc,festival,upcoming,conn,user);
                    }
                });
                gbc.insets= new Insets(10,10,10,10);
                Main.AddObject(back,frame,layout,gbc,26,1,1,10,10);
                if (upcoming.equals("organiserSpecific")){
                    festival.RemoveFestival(conn);
                    OrganiserHome.AddFestival(frame,layout,gbc,conn,user,false); }
                else { Basket.displayBasket(frame,layout,gbc,user,conn); }
            }
        });

        JButton cancelFestival= new JButton("Cancel");
        if (!upcoming.equals("organiserSpecific")){ cancelFestival.setVisible(false); }
        cancelFestival.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                festival.RemoveFestival(conn);
                JButton organiserHome= new JButton("Home");
                JButton signOut= new JButton("Sign Out");
                JLabel festivalCancelled= new JLabel("<html>Festival successfully<br/>cancelled!<html/>");

                organiserHome.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame,gbc);
                        OrganiserHome.main(frame,layout,gbc,conn,user);
                    }
                });
                signOut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame,gbc);
                        DatabaseConnection.CloseConnection(conn);
                        SignIn.main(frame,layout,gbc,false);
                    }
                });

                Main.AddObject(organiserHome,frame,layout,gbc,20,1,1,10,10);
                Main.AddObject(signOut,frame,layout,gbc,20,2,1,10,10);
                gbc.gridwidth= 2;
                Main.AddObject(festivalCancelled,frame,layout,gbc,20,1,2,10,10);
                frame.pack();
                frame.revalidate();
            }
        });


        JLabel picture= new JLabel("");
        try {
            BufferedImage festivalPic = ImageIO.read(new File("Assets\\PromoPics\\" + festival.name + ".jpg"));
            picture.setIcon(new ImageIcon(festivalPic));
        } catch (Exception e){
            e.printStackTrace();
        }
        gbc.fill= GridBagConstraints.NONE;
        gbc.anchor= GridBagConstraints.FIRST_LINE_START;
        gbc.insets= new Insets(10,40,10,40);
        Main.AddObject(back,buttons,layout,gbc,26,1,1,10,10);
        gbc.anchor= GridBagConstraints.FIRST_LINE_END;
        Main.AddObject(basket,buttons,layout,gbc,26,2,1,10,10);
        Main.AddObject(cancelFestival,frame,layout,gbc,20,2,2,10,10);
        gbc.fill= GridBagConstraints.BOTH;
        gbc.insets= new Insets(10,10,10,10);


        JPanel festivalDetails= new JPanel();
        festivalDetails.setLayout(layout);
        JLabel name= new JLabel(festival.name,SwingConstants.CENTER);
        JLabel date= new JLabel(festival.startDate+" - "+festival.endDate,SwingConstants.CENTER);
        JLabel location= new JLabel(festival.address+", "+festival.PostCode,SwingConstants.CENTER);
        Main.AddObject(name,festivalDetails,layout,gbc,26,1,1,10,10);
        Main.AddObject(date,festivalDetails,layout,gbc,20,1,2,10,10);
        Main.AddObject(location,festivalDetails,layout,gbc,20,1,3,10,10);

        JPanel performers= new JPanel();
        JPanel tickets= new JPanel();
        performers.setLayout(layout);
        tickets.setLayout(layout);
        JLabel performanceName= new JLabel("Performer",SwingConstants.CENTER);
        JLabel performanceDate= new JLabel("Performance Date",SwingConstants.CENTER);
        JLabel ticketDescTitle= new JLabel("Ticket Description",SwingConstants.CENTER);
        JLabel ticketPriceTitle= new JLabel("Ticket Price",SwingConstants.CENTER);

        gbc.insets= new Insets(0,0,0,0);
        gbc.gridwidth=1;
        gbc.gridheight=1;
        Main.AddObject(performanceName,performers,layout,gbc, 16,1,1,10,10);
        Main.AddObject(performanceDate,performers, layout, gbc,16,2,1,10,10);
        for (int i=0, y=2; i<festival.Performer.length; i++, y++){
            JLabel performerName= new JLabel(festival.Performer[i][0],SwingConstants.CENTER);
            performerName.setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel performerDate= new JLabel(festival.Performer[i][1],SwingConstants.CENTER);
            performerDate.setBorder(BorderFactory.createLineBorder(Color.black));

            Main.AddObject(performerName,performers,layout,gbc,16,1,y,10,10);
            Main.AddObject(performerDate,performers,layout,gbc,16,2,y,10,10);
        }
        Main.AddObject(ticketDescTitle,tickets,layout,gbc,16,1,1,10,10);
        Main.AddObject(ticketPriceTitle,tickets,layout,gbc,16,2,1,10,10);
        for (int i=0, y=2; i<festival.Ticket.length; i++, y++){
            String ticketCode= festival.Ticket[i][0];
            String ticketPrice= festival.Ticket[i][2];
            JLabel ticketDesc= new JLabel(festival.Ticket[i][1],SwingConstants.CENTER);
            ticketDesc.setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel ticketPriceLabel= new JLabel("£"+ticketPrice,SwingConstants.CENTER);
            ticketPriceLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            String[] quantities= {"1","2","3","4","5","6","7","8","9","10"};
            JComboBox quantityBox= new JComboBox(quantities);
            JButton addToBasket= new JButton("Add To Basket");

            Main.AddObject(ticketDesc,tickets,layout,gbc,16,1,y,10,10);
            Main.AddObject(ticketPriceLabel,tickets,layout,gbc,16,2,y,10,10);
            if (upcoming.equals("upcoming")){
                Main.AddObject(quantityBox,tickets,layout,gbc,14,3,y,10,10);
                Main.AddObject(addToBasket,tickets,layout,gbc,16,4,y,10,10); }
            addToBasket.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Basket.AddToBasket(String.valueOf(quantityBox.getSelectedItem()),festival,ticketCode,ticketPrice);
                }
            });

        }

        gbc.insets= new Insets(10,10,10,10);
        gbc.gridwidth= 2;
        Main.AddObject(buttons,frame,layout,gbc,20,1,1,10,10);
        gbc.gridwidth= 1;
        Main.AddObject(picture,frame,layout,gbc,26,1,3,10,10);
        Main.AddObject(performers,frame,layout,gbc,16,1,4,10,10);
        Main.AddObject(festivalDetails,frame,layout,gbc,26,2,3,10,10);
        Main.AddObject(tickets,frame,layout,gbc,16,2,4,10,10);

        frame.pack();
        frame.revalidate();
    }

}

// Use of prepared statements learnt from:
// https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html

// Random 6-digit number generation (lines 70 & 71) adapted from:
// https://stackoverflow.com/questions/51322750/generate-6-digit-random-number
