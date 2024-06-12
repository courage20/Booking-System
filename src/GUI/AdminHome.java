package GUI;

import JDBC.AdminFunctions;
import JDBC.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

public class AdminHome {

    public static void DeleteRowConfirm(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn,String[] tableDetails){
        gbc.fill= GridBagConstraints.NONE;
        //Centres the buttons and labels in their cells
        JLabel areYouSure= new JLabel("<html>Are you sure you<br/>want to delete this row?<html/>",SwingConstants.CENTER);
        JButton no= new JButton("No");
        JButton yes= new JButton("Yes");
        no.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                ReviewTable(frame,layout,gbc,conn,tableDetails[0]);
                //Resets the frame and takes the admin back to the table review window
            }
        });
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminFunctions.DeleteTableRow(conn,tableDetails);
                Main.ResetFrame(frame,gbc);
                ReviewTable(frame,layout,gbc,conn,tableDetails[0]);
                //Deletes the row specified before going back to the table review window
            }
        });

        gbc.gridwidth= 2;
        Main.AddObject(areYouSure,frame,layout,gbc,24,1,1,10,10);
        gbc.gridwidth= 1;
        Main.AddObject(yes,frame,layout,gbc,24,1,2,10,10);
        Main.AddObject(no,frame,layout,gbc,24,2,2,10,10);
        //Add the label and two buttons to the window

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void ReviewTable(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn,String tableType) {
        ArrayList<String[]> table = AdminFunctions.getTable(conn, tableType);

        JPanel titlePanel = new JPanel();
        //Panel allows us to organise buttons and labels neatly in the frame
        titlePanel.setLayout(layout);
        JButton back = new JButton("Back");
        JLabel tableTitle = new JLabel(tableType + " Table", SwingConstants.CENTER);
        //This label will read whichever table admin selected
        JButton signOut = new JButton("Sign Out");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                main(frame, layout, gbc, conn);
                //Returns to admin home
            }
        });
        signOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DatabaseConnection.CloseConnection(conn);
                Main.ResetFrame(frame, gbc);
                SignIn.main(frame, layout, gbc, false);
                //Close the connection before returning to sign in opening page
            }
        });
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 80, 10, 80);
        Main.AddObject(back, titlePanel, layout, gbc, 24, 1, 1, 10, 10);
        Main.AddObject(tableTitle, titlePanel, layout, gbc, 26, 2, 1, 10, 10);
        Main.AddObject(signOut, titlePanel, layout, gbc, 24, 3, 1, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        Main.AddObject(titlePanel, frame, layout, gbc, 24, 1, 1, 10, 10);
        //Organise and add all the pre-defined components to the frame


        if (table == null) {
            JLabel emptyTable = new JLabel("There are no " + tableType + "s :(", SwingConstants.CENTER);
            Main.AddObject(emptyTable, frame, layout, gbc, 36, 1, 2, 10, 10);
            //If the result set returns empty there are no rows in the specified table
        } else {
            gbc.insets = new Insets(0, 0, 0, 0);
            JPanel tableGrid = new JPanel();
            tableGrid.setLayout(layout);
            //We've created the table and defined the layout it takes as GridBagLayout

            int x = 1;
            for (String columnName : table.get(0)) {
                JLabel columnLabel = new JLabel(columnName, SwingConstants.CENTER);
                columnLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                Main.AddObject(columnLabel, tableGrid, layout, gbc, 24, x++, 1, 10, 10);
                //Adds the name of each column to the top of the table
            }

            int y = 2;
            //Start from 2 because column names take first row in table
            for (int i = 1; i < table.size(); i++) {
                x = 1;
                for (String cell : table.get(i)) {
                    JLabel cellLabel = new JLabel(cell, SwingConstants.CENTER);
                    cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    Main.AddObject(cellLabel, tableGrid, layout, gbc, 20, x++, y, 10, 10);
                }
                if (tableType.equals("Booking") || tableType.equals("Band")) {
                    String[] tableDetails= {tableType,table.get(i)[0],table.get(0)[0]};
                    JButton deleteRow = new JButton("Delete");
                    deleteRow.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            Main.ResetFrame(frame, gbc);
                            DeleteRowConfirm(frame, layout, gbc, conn, tableDetails);
                        }
                    });
                    Main.AddObject(deleteRow, tableGrid, layout, gbc, 20, x++, y, 10, 10);

                    if (!tableType.equals("Band")) {
                        JButton confirmBooking = new JButton("Confirm");
                        confirmBooking.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                Main.ResetFrame(frame, gbc);
                                JLabel bookingConfirmed = new JLabel("Booking Confirmed!", SwingConstants.CENTER);
                                JButton continueButton = new JButton("Continue");
                                continueButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        Main.ResetFrame(frame, gbc);
                                        ReviewTable(frame, layout, gbc, conn, tableType);
                                    }
                                });

                                Main.AddObject(bookingConfirmed, frame, layout, gbc, 24, 1, 1, 10, 10);
                                Main.AddObject(continueButton, frame, layout, gbc, 24, 1, 2, 10, 10);
                                frame.pack();
                                frame.setLocationRelativeTo(null);
                                frame.revalidate();
                            }
                        });
                        Main.AddObject(confirmBooking, tableGrid, layout, gbc, 20, x++, y, 10, 10);
                    }
                }
                y++;
            }
            gbc.insets= new Insets(20,20,20,20);
            gbc.gridwidth= 3;
            Main.AddObject(tableGrid,frame,layout,gbc,20,1,2,10,10);
        }
        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }


    public static void DisplayInvoice(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn,String userID){
        ArrayList<String[]> invoiceList= AdminFunctions.CreateInvoice(conn,userID);

        JButton back= new JButton("Back");
        JLabel pageTitle= new JLabel("Customer Invoice",SwingConstants.CENTER);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                gbc.insets= new Insets(10,10,10,10);
                DisplayCorporateCustomers(frame,layout,gbc,conn);
            }
        });
        JButton send= new JButton("Send Invoice");
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                gbc.insets= new Insets(10,10,10,10);
                JLabel invoiceSent= new JLabel("<html>The invoice has been<br/>emailed to the customer!<html/>",SwingConstants.CENTER);
                JButton home= new JButton("Home");
                home.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame,gbc);
                        main(frame,layout,gbc,conn);
                    }
                });

                Main.AddObject(invoiceSent,frame,layout,gbc,30,1,1,10,10);
                Main.AddObject(home,frame,layout,gbc,24,1,2,10,10);
                frame.pack();
                //frame.setLocationRelativeTo(null);
                frame.revalidate();
            }
        });
        Main.AddObject(send,frame,layout,gbc,24,3,1,10,10);
        Main.AddObject(back,frame,layout,gbc,24,1,1,10,10);
        Main.AddObject(pageTitle,frame,layout,gbc,26,2,1,10,10);

        JPanel customerDetails= new JPanel();
        customerDetails.setLayout(layout);
        JLabel invoiceID= new JLabel("Invoice ID: "+invoiceList.get(0)[0]);
        JLabel customerID= new JLabel("Customer ID: "+invoiceList.get(0)[1]);
        JLabel customerName= new JLabel(invoiceList.get(0)[2]);
        JLabel customerAddress= new JLabel(invoiceList.get(0)[3]);
        JLabel customerTown= new JLabel(invoiceList.get(0)[4]);
        JLabel customerPostCode= new JLabel(invoiceList.get(0)[5]);
        JLabel customerEmail= new JLabel(invoiceList.get(0)[6]);
        JLabel customerNumber= new JLabel(invoiceList.get(0)[7]);

        gbc.insets= new Insets(0,10,0,20);
        Main.AddObject(invoiceID,customerDetails,layout,gbc,20,1,1,10,10);
        Main.AddObject(customerID,customerDetails,layout,gbc,20,1,2,10,10);
        Main.AddObject(customerName,customerDetails,layout,gbc,20,1,3,10,10);
        Main.AddObject(customerAddress,customerDetails,layout,gbc,20,1,4,10,10);
        Main.AddObject(customerTown,customerDetails,layout,gbc,20,1,5,10,10);
        Main.AddObject(customerPostCode,customerDetails,layout,gbc,20,1,6,10,10);
        Main.AddObject(customerEmail,customerDetails,layout,gbc,20,2,5,10,10);
        Main.AddObject(customerNumber,customerDetails,layout,gbc,20,2,6,10,10);
        gbc.gridwidth= 2;
        gbc.insets= new Insets(10,10,10,10);
        Main.AddObject(customerDetails,frame,layout,gbc,20,1,2,10,10);
        gbc.gridwidth= 1;


        JPanel bookingDetails= new JPanel();
        bookingDetails.setLayout(layout);
        JLabel IDLabel= new JLabel("Booking ID",SwingConstants.CENTER);
        IDLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel nameLabel= new JLabel("Festival Name",SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel codeLabel= new JLabel("Ticket Code",SwingConstants.CENTER);
        codeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel dateLabel= new JLabel("Date Booked",SwingConstants.CENTER);
        dateLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel quantityLabel= new JLabel("Quantity",SwingConstants.CENTER);
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel subtotalLabel= new JLabel("Subtotal",SwingConstants.CENTER);
        subtotalLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        gbc.fill= GridBagConstraints.BOTH;
        gbc.insets= new Insets(0,0,0,0);
        Main.AddObject(IDLabel,bookingDetails,layout,gbc,18,1,1,10,10);
        Main.AddObject(nameLabel,bookingDetails,layout,gbc,18,2,1,10,10);
        Main.AddObject(codeLabel,bookingDetails,layout,gbc,18,3,1,10,10);
        Main.AddObject(dateLabel,bookingDetails,layout,gbc,18,4,1,10,10);
        Main.AddObject(quantityLabel,bookingDetails,layout,gbc,18,5,1,10,10);
        Main.AddObject(subtotalLabel,bookingDetails,layout,gbc,18,6,1,10,10);

        double totalCost= 0.0;
        int y= 2;
        for (int i= 1; i<invoiceList.size(); i++){
            JLabel bookingID= new JLabel(invoiceList.get(i)[0],SwingConstants.CENTER);
            bookingID.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel festivalName= new JLabel(invoiceList.get(i)[1],SwingConstants.CENTER);
            festivalName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel ticketCode= new JLabel(invoiceList.get(i)[2],SwingConstants.CENTER);
            ticketCode.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel date= new JLabel(invoiceList.get(i)[5],SwingConstants.CENTER);
            date.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel quantity= new JLabel(invoiceList.get(i)[3],SwingConstants.CENTER);
            quantity.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel subtotal= new JLabel(invoiceList.get(i)[4],SwingConstants.CENTER);
            subtotal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            totalCost+= Double.parseDouble(invoiceList.get(i)[4]);

            Main.AddObject(bookingID,bookingDetails,layout,gbc,18,1,y,10,10);
            Main.AddObject(festivalName,bookingDetails,layout,gbc,18,2,y,10,10);
            Main.AddObject(ticketCode,bookingDetails,layout,gbc,18,3,y,10,10);
            Main.AddObject(date,bookingDetails,layout,gbc,18,4,y,10,10);
            Main.AddObject(quantity,bookingDetails,layout,gbc,18,5,y,10,10);
            Main.AddObject(subtotal,bookingDetails,layout,gbc,18,6,y++,10,10);
        }

        JLabel totalCostTitle= new JLabel("Total Cost:",SwingConstants.CENTER);
        totalCostTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel totalCostLabel= new JLabel("£"+totalCost,SwingConstants.CENTER);
        totalCostLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Main.AddObject(totalCostTitle,bookingDetails,layout,gbc,18,5,y,10,10);
        Main.AddObject(totalCostLabel,bookingDetails,layout,gbc,18,6,y,10,10);

        gbc.gridwidth= 3;
        gbc.fill= GridBagConstraints.NONE;
        Main.AddObject(bookingDetails,frame,layout,gbc,18,1,3,10,10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();

    }

    public static void DisplayCorporateCustomers(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn){
        ArrayList<String[]> customers= AdminFunctions.GetCorporateCustomers(conn);
        gbc.fill= GridBagConstraints.BOTH;

        JButton back= new JButton("Back");
        JLabel pageTitle= new JLabel("Corporate Customers", SwingConstants.CENTER);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                main(frame,layout,gbc,conn);
            }
        });
        Main.AddObject(back,frame,layout,gbc,24,1,1,10,10);
        Main.AddObject(pageTitle,frame,layout,gbc,26,2,1,10,10);

        gbc.insets= new Insets(0,0,0,0);
        JPanel customerList= new JPanel();
        customerList.setLayout(layout);
        JLabel IDTitle= new JLabel("Customer ID",SwingConstants.CENTER);
        IDTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel nameTitle= new JLabel("Customer Name",SwingConstants.CENTER);
        nameTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Main.AddObject(IDTitle,customerList,layout,gbc,24,1,1,10,10);
        Main.AddObject(nameTitle,customerList,layout,gbc,24,2,1,10,10);

        int y= 2;
        for (String[] customer: customers){
            JLabel customerID= new JLabel(customer[0]);
            customerID.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel customerName= new JLabel(customer[1]);
            customerName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JButton createInvoice= new JButton("Create Invoice");
            createInvoice.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Main.ResetFrame(frame,gbc);
                    DisplayInvoice(frame,layout,gbc,conn,customer[0]);
                }
            });

            Main.AddObject(customerID,customerList,layout,gbc,20,1,y,10,10);
            Main.AddObject(customerName,customerList,layout,gbc,20,2,y,10,10);
            Main.AddObject(createInvoice,customerList,layout,gbc,20,3,y++,10,10);
        }

        Main.AddObject(customerList,frame,layout,gbc,24,2,2,10,10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void AddPerformer(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn,boolean addPerformerError){
        gbc.insets= new Insets(10,15,10,15);
        JTextField[] allTextFields= new JTextField[4];

        JLabel addPerformerLabel= new JLabel("Add Performer Details",SwingConstants.CENTER);
        JButton back= new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                main(frame,layout,gbc,conn);
            }
        });
        JButton submit= new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] performerDetails= new String[4];
                int i= 0;
                for (JTextField field: allTextFields){
                    performerDetails[i++]= field.getText();
                }
                Main.ResetFrame(frame,gbc);
                boolean performerAdded= AdminFunctions.addPerformer(conn,performerDetails);
                if (performerAdded) {
                    JLabel performerAddedLabel = new JLabel("<html>" + performerDetails[0] + " has been added<br/>to the performer table</html>", SwingConstants.CENTER);
                    JButton performerTable = new JButton("Performer Table");
                    JButton home = new JButton("Admin Home");
                    performerTable.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Main.ResetFrame(frame, gbc);
                            ReviewTable(frame, layout, gbc, conn, "Band");
                        }
                    });
                    home.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Main.ResetFrame(frame, gbc);
                            main(frame, layout, gbc, conn);
                        }
                    });

                    gbc.gridwidth= 2;
                    Main.AddObject(performerAddedLabel,frame,layout,gbc,36,1,1,10,10);
                    gbc.gridwidth= 1;
                    Main.AddObject(performerTable,frame,layout,gbc,26,1,2,10,10);
                    Main.AddObject(home,frame,layout,gbc,26,2,2,10,10);
                    frame.pack();
                    frame.revalidate();

                } else{ AddPerformer(frame,layout,gbc,conn,true); }
            }
        });

        Main.AddObject(back,frame,layout,gbc,26,1,1,10,10);
        Main.AddObject(addPerformerLabel,frame,layout,gbc,30,2,1,10,10);
        Main.AddObject(submit,frame,layout,gbc,26,3,1,10,10);

        JLabel performerNotAdded= new JLabel("<html>Something went wrong :(<br/>Make sure your details fit within the character limit shown in the brackets<html/>");
        performerNotAdded.setForeground(Color.RED);
        performerNotAdded.setVisible(addPerformerError);
        Main.AddObject(performerNotAdded,frame,layout,gbc,16,2,2,10,10);

        JPanel textFieldPanel= new JPanel();
        textFieldPanel.setLayout(layout);
        JLabel nameLabel= new JLabel("Performer Name (20):");
        JTextField nameBox= new JTextField();
        allTextFields[0]= nameBox;
        Main.AddObject(nameLabel,textFieldPanel,layout,gbc,20,1,1,10,10);
        Main.AddObject(nameBox,textFieldPanel,layout,gbc,20,1,2,10,10);

        JLabel firstNameLabel= new JLabel("Agent First Name (20):");
        JTextField firstNameBox= new JTextField();
        allTextFields[1]= firstNameBox;
        Main.AddObject(firstNameLabel,textFieldPanel,layout,gbc,20,2,1,10,10);
        Main.AddObject(firstNameBox,textFieldPanel,layout,gbc,20,2,2,10,10);

        JLabel lastNameLabel= new JLabel("Agent Last Name (20):");
        JTextField lastNameBox= new JTextField();
        allTextFields[2]= lastNameBox;
        Main.AddObject(lastNameLabel,textFieldPanel,layout,gbc,20,2,3,10,10);
        Main.AddObject(lastNameBox,textFieldPanel,layout,gbc,20,2,4,10,10);

        JLabel numberLabel= new JLabel("Contact Number (15):");
        JTextField numberBox= new JTextField();
        allTextFields[3]= numberBox;
        Main.AddObject(numberLabel,textFieldPanel,layout,gbc,20,1,3,10,10);
        Main.AddObject(numberBox,textFieldPanel,layout,gbc,20,1,4,10,10);

        gbc.gridwidth= 3;
        Main.AddObject(textFieldPanel,frame,layout,gbc,20,1,3,10,10);
        gbc.gridwidth= 1;

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }



    public static void main(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Connection conn){
        gbc.insets= new Insets(0,0,0,0);
        gbc.fill = GridBagConstraints.BOTH;

        JButton signOut= new JButton("Sign Out");
        signOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DatabaseConnection.CloseConnection(conn);
                Main.ResetFrame(frame,gbc);
                SignIn.main(frame, layout, gbc, false);
            }});

        JLabel tablesTitle= new JLabel("Select a table to review/edit",SwingConstants.CENTER);
        JLabel functionTitle= new JLabel("... Or select a function");
        JButton festivalButton= new JButton("Festival");
        JButton bookingButton= new JButton("Booking");
        JButton performerFestivalButton= new JButton("Festival Performer");
        JButton performerButton= new JButton("Performer");
        JButton invoiceButton= new JButton("Create Invoice");
        JButton addPerformerButton= new JButton("Add Performer");

        festivalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                ReviewTable(frame,layout,gbc,conn,"Festival");
            }
        });
        bookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                ReviewTable(frame,layout,gbc,conn,"Booking");
            }
        });
        performerFestivalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                ReviewTable(frame,layout,gbc,conn,"Band_Festival");
            }
        });
        performerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                ReviewTable(frame,layout,gbc,conn,"Band");
            }
        });
        invoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                JButton signOut= new JButton("Sign Out");
                signOut.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DatabaseConnection.CloseConnection(conn);
                        Main.ResetFrame(frame,gbc);
                        SignIn.main(frame, layout, gbc, false);
                    }});
                Main.AddObject(signOut,frame,layout,gbc,24,3,1,10,10);
                DisplayCorporateCustomers(frame,layout,gbc,conn);
            }
        });
        addPerformerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                AddPerformer(frame,layout,gbc,conn,false);
            }
        });

        gbc.insets= new Insets(20,20,20,20);
        gbc.gridwidth= 2;
        Main.AddObject(tablesTitle,frame,layout,gbc,36,1,2,10,10);
        gbc.gridwidth= 1;
        Main.AddObject(festivalButton,frame,layout,gbc,24,1,3,10,10);
        Main.AddObject(bookingButton,frame,layout,gbc,24,2,3,10,10);
        Main.AddObject(performerFestivalButton,frame,layout,gbc,24,1,4,10,10);
        Main.AddObject(performerButton,frame,layout,gbc,24,2,4,10,10);
        gbc.insets= new Insets(20,100,20,20);
        Main.AddObject(functionTitle,frame,layout,gbc,36,3,2,10,10);
        Main.AddObject(invoiceButton,frame,layout,gbc,24,3,3,10,10);
        Main.AddObject(addPerformerButton,frame,layout,gbc,24,3,4,10,10);
        gbc.fill= GridBagConstraints.NONE;
        Main.AddObject(signOut,frame,layout,gbc,24,3,1,10,10);
        gbc.fill= GridBagConstraints.BOTH;

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }
}
