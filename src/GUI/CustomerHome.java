package GUI;

import JDBC.Basket;
import JDBC.Festival;
import JDBC.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomerHome {

    public static JPanel LoadNotifications(GridBagLayout layout,GridBagConstraints gbc) {
        File notificationFile = new File("src\\GUI\\Notifications.txt");

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(layout);
        notificationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel notificationsTitle = new JLabel("Notifications");
        Main.AddObject(notificationsTitle, notificationPanel, layout, gbc, 30, 1, 1, 10, 10);
        try {
            BufferedReader notificationReader = new BufferedReader(new FileReader(notificationFile));

            String line;
            int y= 2;
            while ((line= notificationReader.readLine())!=null){
                JLabel notification= new JLabel(line);
                Main.AddObject(notification,notificationPanel,layout,gbc,18,1,y++,10,10);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } return notificationPanel;
    }

    public static void CustomerDetails(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, User user, Connection conn) {
        gbc.insets = new Insets(10, 10, 20, 10);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                main(frame, layout, gbc, conn, user);
            }
        });
        JButton editDetails = new JButton("Edit Details");
        editDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User.RemoveUser(conn,user);
                if (user.userType.equals("Individual")) {
                    Main.ResetFrame(frame, gbc);
                    Register.IndividualCustomer(frame, layout, gbc, conn);
                } else {
                    Main.ResetFrame(frame, gbc);
                    Register.CorporateCustomer(frame, layout, gbc, conn);
                }
            }
        });

        JLabel firstName = new JLabel("First Name: " + user.firstName);
        JLabel lastName = new JLabel("Last Name: " + user.lastName);
        JLabel organisationName = new JLabel("Organisation Name: " + user.organisationName);
        JLabel address = new JLabel("Address: " + user.address);
        JLabel town = new JLabel("Town: " + user.town);
        JLabel postcode = new JLabel("Postcode: " + user.postcode);
        JLabel email = new JLabel("Email: " + user.email);
        JLabel phoneNo = new JLabel("Phone Number: " + user.phoneNumber);
        JLabel username = new JLabel("Username: " + user.username);
        JLabel password = new JLabel("Password: " + user.password);
        JLabel paymentMethod = new JLabel("Payment Method: " + user.paymentMethod);
        JLabel accountNo = new JLabel("Account Number: " + user.accountNo);

        Main.AddObject(backButton, frame, layout, gbc, 20, 1, 1, 10, 10);
        Main.AddObject(editDetails, frame, layout, gbc, 20, 2, 1, 10, 10);
        gbc.insets = new Insets(10, 10, 10, 10);
        Main.AddObject(firstName, frame, layout, gbc, 20, 1, 2, 10, 10);
        Main.AddObject(lastName, frame, layout, gbc, 20, 1, 3, 10, 10);
        if (user.userType.equals("Corporate")) {
            Main.AddObject(organisationName, frame, layout, gbc, 20, 1, 4, 10, 10);
        }
        Main.AddObject(address, frame, layout, gbc, 20, 2, 2, 10, 10);
        Main.AddObject(town, frame, layout, gbc, 20, 2, 3, 10, 10);
        Main.AddObject(postcode, frame, layout, gbc, 20, 2, 4, 10, 10);
        Main.AddObject(email, frame, layout, gbc, 20, 1, 5, 10, 10);
        Main.AddObject(phoneNo, frame, layout, gbc, 20, 1, 6, 10, 10);
        Main.AddObject(username, frame, layout, gbc, 20, 1, 7, 10, 10);
        Main.AddObject(password, frame, layout, gbc, 20, 1, 8, 10, 10);
        Main.AddObject(paymentMethod, frame, layout, gbc, 20, 2, 5, 10, 10);
        Main.AddObject(accountNo, frame, layout, gbc, 20, 2, 6, 10, 10);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }

    public static void main(JFrame frame, GridBagLayout layout, GridBagConstraints gbc, Connection conn, User user) {
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;

        JButton userDetails = new JButton("Your Details");
        JButton upcomingFestivals = new JButton("Upcoming Festivals");
        JButton orderHistory = new JButton("Order History");
        if (user.userType.equals("Corporate")) {
            orderHistory.setText("Invoice History");
        }
        JButton basket = new JButton("Basket");
        userDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                gbc.insets = new Insets(10, 15, 10, 15);
                CustomerDetails(frame, layout, gbc, user, conn);
            }
        });
        upcomingFestivals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                String[][] festivals = Festival.GetAllFestivals(conn, user, "upcoming");
                Festival.DisplayAllFestivals(frame, layout, gbc, festivals, "upcoming", conn, user);
            }
        });
        orderHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                String[][] festivals = Festival.GetAllFestivals(conn, user, "previous");
                Festival.DisplayAllFestivals(frame, layout, gbc, festivals, "previous", conn, user);
            }
        });
        basket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame, gbc);
                JButton back = new JButton("Back");
                back.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Main.ResetFrame(frame, gbc);
                        frame.revalidate();
                        CustomerHome.main(frame, layout, gbc, conn, user);
                    }
                });
                gbc.insets = new Insets(10, 10, 10, 10);
                Main.AddObject(back, frame, layout, gbc, 26, 1, 1, 10, 10);
                Basket.displayBasket(frame, layout, gbc, user, conn);
            }
        });

        JPanel festivalPromo = new JPanel();
        int i = 0;
        ArrayList<String> images = new ArrayList<>(Arrays.asList("Glastonbury.jpg", "Wireless.jpg", "MadMusic.jpg"));
        ArrayList<String> promos = new ArrayList<>(Arrays.asList("Glastonbury - 2019", "Wireless - 2019", "MadMusic - 2020"));
        JButton promoNext = new JButton(">");
        JButton promoPrev = new JButton("<");
        JLabel promoLabel = new JLabel(promos.get(i), SwingConstants.CENTER);
        JLabel promoPic = new JLabel(new ImageIcon("Assets\\PromoPics\\" + images.get(i)));
        promoPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index= promos.indexOf(promoLabel.getText());
                --index;
                if (index<0){
                    index= promos.size()-1;
                }
                promoLabel.setText(promos.get(index));
                promoPic.setIcon(new ImageIcon("Assets\\PromoPics\\" + images.get(index)));
                frame.revalidate();
            }
        });
        promoNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index= promos.indexOf(promoLabel.getText());
                ++index;
                if (index==promos.size()){
                    index= 0;
                }
                promoLabel.setText(promos.get(index));
                promoPic.setIcon(new ImageIcon("Assets\\PromoPics\\" + images.get(index)));
                frame.revalidate();
            }
        });


        Main.AddObject(userDetails, frame, layout, gbc, 20, 1, 1, 30, 30);
        Main.AddObject(upcomingFestivals, frame, layout, gbc, 20, 2, 1, 30, 30);
        Main.AddObject(orderHistory, frame, layout, gbc, 20, 3, 1, 30, 30);
        gbc.insets = new Insets(0, 150, 0, 0);
        Main.AddObject(basket, frame, layout, gbc, 20, 4, 1, 30, 30);

        gbc.insets = new Insets(0, 0, 0, 0);
        festivalPromo.setLayout(layout);
        Main.AddObject(promoPic, festivalPromo, layout, gbc, 30, 2, 1, 100, 100);
        Main.AddObject(promoPrev, festivalPromo, layout, gbc, 30, 1, 2, 10, 10);
        Main.AddObject(promoLabel, festivalPromo, layout, gbc, 30, 2, 2, 10, 10);
        Main.AddObject(promoNext, festivalPromo, layout, gbc, 30, 3, 2, 10, 10);
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        Main.AddObject(festivalPromo, frame, layout, gbc, 30, 1, 2, 100, 100);

        gbc.gridheight= 1;
        gbc.gridwidth= 1;
        JPanel notifications= LoadNotifications(layout,gbc);
        gbc.insets = new Insets(20, 100, 0, 0);
        Main.AddObject(notifications, frame, layout, gbc, 20, 4, 2, 30, 15);

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
    }
}

// Ability to add images to GUI (line 162) learnt from:
// https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel