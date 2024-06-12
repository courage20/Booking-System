package GUI;

import JDBC.AdminFunctions;
import JDBC.DatabaseConnection;
import JDBC.Festival;
import JDBC.User;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

public class OrganiserHome {

    public static void AddTicketToFestival(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn, User organiser,boolean addTicketError) {
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                main(frame, layout, gbc, conn, organiser);
            }
        });
        JButton signOut = new JButton("Sign Out");
        signOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                DatabaseConnection.CloseConnection(conn);
                SignIn.main(frame, layout, gbc, false);
            }
        });
        JLabel performerNotAdded = new JLabel("<html>Something went wrong :(<br/>Make sure your details match the specified box<html/>");
        performerNotAdded.setForeground(Color.RED);
        performerNotAdded.setVisible(addTicketError);
        Main.AddObject(performerNotAdded, frame, layout, gbc, 16, 2, 1, 10, 10);

        String[][] allFestivalDetails = Festival.GetAllFestivals(conn, organiser, "organiserSpecific");
        if (allFestivalDetails == null) {
            JLabel noFestivals = new JLabel("You are not organising any festivals");
            Main.AddObject(noFestivals, frame, layout, gbc, 20, 2, 2, 10, 10);
        } else {
            String[] festivalList = new String[allFestivalDetails.length];
            for (int i = 0; i < allFestivalDetails.length; i++) {
                festivalList[i] = allFestivalDetails[i][0];
            }

            int y = 2;
            for (String festival : festivalList) {
                JLabel festivalName = new JLabel(festival);
                JLabel ticketDescLabel = new JLabel("Ticket Description (30):");
                JTextField ticketDesc = new JTextField();
                JLabel ticketPriceLabel = new JLabel("Ticket Price:");
                JTextField ticketPrice = new JTextField();
                JButton addToFestival = new JButton("Add");
                addToFestival.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String[] festivalPerformanceDetails = {festival, ticketDesc.getText(), ticketPrice.getText()};
                        boolean addSuccessful = Festival.AddTicketToFestival(conn, festivalPerformanceDetails);
                        if (addSuccessful) {
                            Main.ResetFrame(frame, gbc);
                            main(frame, layout, gbc, conn, organiser);
                        } else {
                            Main.ResetFrame(frame, gbc);
                            AddTicketToFestival(frame, layout, gbc, conn, organiser, true);
                        }
                    }
                });
                JPanel festivalTile = new JPanel();
                festivalTile.setLayout(layout);
                Main.AddObject(ticketDescLabel, festivalTile, layout, gbc, 20, 2, 1, 10, 10);
                Main.AddObject(ticketDesc, festivalTile, layout, gbc, 20, 2, 2, 10, 10);
                Main.AddObject(ticketPriceLabel, festivalTile, layout, gbc, 20, 3, 1, 10, 10);
                Main.AddObject(ticketPrice, festivalTile, layout, gbc, 20, 3, 2, 10, 10);
                gbc.gridheight = 2;
                Main.AddObject(festivalName, festivalTile, layout, gbc, 20, 1, 1, 10, 10);
                Main.AddObject(addToFestival, festivalTile, layout, gbc, 20, 4, 1, 10, 10);

                gbc.gridheight = 1;
                Main.AddObject(festivalTile, frame, layout, gbc, 20, 2, y++, 10, 10);
            }
        }
        Main.AddObject(back, frame, layout, gbc, 20, 1, 1, 10, 10);
        Main.AddObject(signOut, frame, layout, gbc, 20, 3, 1, 10, 10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void AddPerformerToFestival(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn, User organiser, String performerName,boolean addPerformerError){
        gbc.insets= new Insets(10,10,10,10);

        JButton back= new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                main(frame,layout,gbc,conn,organiser);
            }
        });
        JButton signOut= new JButton("Sign Out");
        signOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                DatabaseConnection.CloseConnection(conn);
                SignIn.main(frame,layout,gbc,false);
            }
        });
        JLabel performerNotAdded= new JLabel("<html>Something went wrong :(<br/>Make sure your details match the specified box<html/>");
        performerNotAdded.setForeground(Color.RED);
        performerNotAdded.setVisible(addPerformerError);
        Main.AddObject(performerNotAdded,frame,layout,gbc,16,2,1,10,10);

        String[][] allFestivalDetails= Festival.GetAllFestivals(conn,organiser,"organiserSpecific");
        if (allFestivalDetails== null){
            JLabel noFestivals= new JLabel("You are not organising any festivals");
            Main.AddObject(noFestivals,frame,layout,gbc,20,2,2,10,10);
        } else {
            String[] festivalList = new String[allFestivalDetails.length];
            for (int i = 0; i < allFestivalDetails.length; i++) {
                festivalList[i] = allFestivalDetails[i][0];
            }

            int y = 2;
            for (String festival : festivalList) {
                JLabel festivalName = new JLabel(festival);
                JLabel performanceDateLabel = new JLabel("<html>Performance Date<br/>(YYYY-MM-DD):<html/>");
                JTextField performanceDate = new JTextField();
                JButton addToFestival = new JButton("Add");
                addToFestival.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String[] festivalPerformanceDetails = {festival, performerName, performanceDate.getText()};
                        boolean addSuccessful = Festival.AddPerformerToFestival(conn, festivalPerformanceDetails);
                        if (addSuccessful) {
                            Main.ResetFrame(frame, gbc);
                            main(frame, layout, gbc, conn, organiser);
                        } else{
                            Main.ResetFrame(frame,gbc);
                            AddPerformerToFestival(frame,layout,gbc,conn,organiser,performerName,true);
                        }
                    }
                });
                JPanel festivalTile = new JPanel();
                festivalTile.setLayout(layout);
                Main.AddObject(performanceDateLabel, festivalTile, layout, gbc, 20, 2, 1, 10, 10);
                Main.AddObject(performanceDate, festivalTile, layout, gbc, 20, 2, 2, 10, 10);
                gbc.gridheight = 2;
                Main.AddObject(festivalName, festivalTile, layout, gbc, 20, 1, 1, 10, 10);
                Main.AddObject(addToFestival, festivalTile, layout, gbc, 20, 3, 1, 10, 10);

                gbc.gridheight = 1;
                Main.AddObject(festivalTile, frame, layout, gbc, 20, 2, y++, 10, 10);
            }
        }
        Main.AddObject(back,frame,layout,gbc,20,1,1,10,10);
        Main.AddObject(signOut,frame,layout,gbc,20,3,1,10,10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void OrganiserDetails(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn,User organiser){
        gbc.insets= new Insets(10,10,20,10);
        JButton backButton= new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                main(frame, layout, gbc, conn, organiser);
            }
        });
        JButton editDetails= new JButton("Edit Details");
        editDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                User.RemoveUser(conn,organiser);
                Register.FestivalOrganiser(frame,layout,gbc,conn);
            }
        });

        JLabel organiserName= new JLabel("Organiser Name: "+organiser.organiserName);
        JLabel contactNumber= new JLabel("Contact Number: "+organiser.phoneNumber);
        JLabel email= new JLabel("Email: "+organiser.email);
        JLabel username= new JLabel("Username: "+organiser.username);
        JLabel password= new JLabel("Password: "+organiser.password);

        gbc.fill= GridBagConstraints.NONE;
        Main.AddObject(backButton,frame,layout,gbc,20,1,1,10,10);
        gbc.fill= GridBagConstraints.BOTH;
        Main.AddObject(editDetails,frame,layout,gbc,20,2,1,10,10);
        gbc.insets= new Insets(10,10,10,10);
        Main.AddObject(organiserName,frame,layout,gbc,20,1,2,10,10);
        Main.AddObject(contactNumber,frame,layout,gbc,20,1,3,10,10);
        Main.AddObject(email,frame,layout,gbc,20,1,4,10,10);
        Main.AddObject(username,frame,layout,gbc,20,2,2,10,10);
        Main.AddObject(password,frame,layout,gbc,20,2,3,10,10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void AddFestival(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Connection conn,User user,boolean addFestivalError){
        gbc.insets= new Insets(10,10,10,10);

        JTextField[] allTextFields= new JTextField[6];
        allTextFields[0]= new JTextField(user.ID);

        JButton back= new JButton("Back");
        JLabel addFestivalTitle= new JLabel("Enter the new Festival details");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                main(frame,layout,gbc,conn,user);
            }
        });

        Main.AddObject(back,frame,layout,gbc,20,1,1,10,10);
        Main.AddObject(addFestivalTitle,frame,layout,gbc,24,2,1,10,10);

        JLabel festivalNotAdded= new JLabel("<html>Something went wrong :(<br/>Make sure your details fit within the character limit shown in the brackets<html/>");
        festivalNotAdded.setForeground(Color.RED);
        festivalNotAdded.setVisible(addFestivalError);
        Main.AddObject(festivalNotAdded,frame,layout,gbc,16,1,2,10,10);

        JLabel nameLabel= new JLabel("Festival Name (20):");
        JTextField name= new JTextField();
        allTextFields[1]= name;
        Main.AddObject(nameLabel,frame,layout,gbc,20,1,3,10,10);
        gbc.insets= new Insets(0,10,10,10);
        Main.AddObject(name,frame,layout,gbc,20,1,4,10,10);
        gbc.insets= new Insets(10,10,10,10);

        JLabel startDateLabel= new JLabel("Festival Start Date (YYYY-MM-DD):");
        JTextField startDate= new JTextField();
        allTextFields[2]= startDate;
        Main.AddObject(startDateLabel,frame,layout,gbc,20,1,5,10,10);
        gbc.insets= new Insets(0,10,10,10);
        Main.AddObject(startDate,frame,layout,gbc,20,1,6,10,10);
        gbc.insets= new Insets(10,10,10,10);

        JLabel endDateLabel= new JLabel("Festival End Date (YYYY-MM-DD):");
        JTextField endDate= new JTextField();
        allTextFields[3]= endDate;
        Main.AddObject(endDateLabel,frame,layout,gbc,20,1,7,10,10);
        gbc.insets= new Insets(0,10,10,10);
        Main.AddObject(endDate,frame,layout,gbc,20,1,8,10,10);
        gbc.insets= new Insets(10,10,10,10);

        JLabel addressLabel= new JLabel("Address (40):");
        JTextField address= new JTextField();
        allTextFields[4]= address;
        Main.AddObject(addressLabel,frame,layout,gbc,20,2,3,10,10);
        gbc.insets= new Insets(0,10,10,10);
        Main.AddObject(address,frame,layout,gbc,20,2,4,10,10);
        gbc.insets= new Insets(10,10,10,10);

        JLabel postcodeLabel= new JLabel("Postcode (10):");
        JTextField postcode= new JTextField();
        allTextFields[5]= postcode;
        Main.AddObject(postcodeLabel,frame,layout,gbc,20,2,5,10,10);
        gbc.insets= new Insets(0,10,10,10);
        Main.AddObject(postcode,frame,layout,gbc,20,2,6,10,10);
        gbc.insets= new Insets(10,10,10,10);


        JButton submit= new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] festivalDetails= new String[6];
                for (int i=0; i<festivalDetails.length; i++){
                    String currentTextField= allTextFields[i].getText();
                    festivalDetails[i]=currentTextField;
                }
                Main.ResetFrame(frame,gbc);
                Festival newFestival= new Festival();
                boolean festivalAdded= newFestival.AddFestival(conn,festivalDetails);
                if (festivalAdded){ main(frame,layout,gbc,conn,user); }
                else{ AddFestival(frame,layout,gbc,conn,user,true); }
            }
        });
        gbc.gridheight= 2;
        Main.AddObject(submit,frame,layout,gbc,20,2,7,10,10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void main(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Connection conn, User organiser){
        gbc.insets= new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.BOTH;

        JButton organiserDetails= new JButton("Your Details");
        JButton addFestival= new JButton("Add Festival");
        JButton addTicket= new JButton("Add Ticket");
        JButton editFestivalDetails= new JButton("Edit Festival");
        JButton signOut= new JButton("Sign Out");

        organiserDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                OrganiserDetails(frame,layout,gbc,conn,organiser);
            }
        });
        addFestival.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                AddFestival(frame,layout,gbc,conn,organiser,false);
            }
        });
        addTicket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                AddTicketToFestival(frame,layout,gbc,conn,organiser,false);
            }
        });
        editFestivalDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                String[][] organiserFestivals= Festival.GetAllFestivals(conn,organiser,"organiserSpecific");
                Festival.DisplayAllFestivals(frame,layout,gbc,organiserFestivals,"organiserSpecific",conn,organiser);
            }
        });
        signOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                DatabaseConnection.CloseConnection(conn);
                SignIn.main(frame,layout,gbc,false);
            }
        });

        Main.AddObject(organiserDetails,frame,layout,gbc,24,1,2,10,10);
        Main.AddObject(addFestival,frame,layout,gbc,24,1,3,10,10);
        Main.AddObject(addTicket,frame,layout,gbc,24,1,4,10,10);
        Main.AddObject(editFestivalDetails,frame,layout,gbc,24,1,5,10,10);
        Main.AddObject(signOut,frame,layout,gbc,24,2,1,10,10);
        JPanel performerTable= new JPanel();
        performerTable.setLayout(layout);
        ArrayList<String[]> performerList= AdminFunctions.getTable(conn,"Band");
        if (performerList== null){
            JLabel noPerformers= new JLabel("<html>There are no<br/>Performers<html/>",SwingConstants.CENTER);
            Main.AddObject(noPerformers,frame,layout,gbc,18,2,3,10,10);
        } else {
            int y = 1;
            for (int i = 1; i < performerList.size(); i++) {
                int index = i;
                JLabel performerName = new JLabel(performerList.get(i)[4], SwingConstants.CENTER);
                performerName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                JButton addToFestival = new JButton("Add to festival");
                addToFestival.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame, gbc);
                        AddPerformerToFestival(frame, layout, gbc, conn, organiser, performerList.get(index)[4], false);
                    }
                });

                gbc.insets = new Insets(0, 0, 0, 0);
                Main.AddObject(performerName, performerTable, layout, gbc, 18, 1, y, 10, 10);
                Main.AddObject(addToFestival, performerTable, layout, gbc, 18, 2, y++, 10, 10);
            }
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridheight = 3;
            Main.AddObject(performerTable, frame, layout, gbc, 20, 2, 2, 10, 10);
        }

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }
}

// JLabel new line using html format (line 39) adapted from:
// https://stackoverflow.com/questions/1090098/newline-in-jlabel