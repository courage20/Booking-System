package GUI;

import JDBC.DatabaseConnection;
import JDBC.*;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class SignIn{

    public static void main(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Boolean signInError) {
        //This is the base of the GUI; the frame to put the components on,
        // the layout to arrange the components and the constraints to place the components
        //Makes sure the components fill all the space they're given
        gbc.insets= new Insets(10,25,10,25);
        gbc.fill= GridBagConstraints.BOTH;
        //Separates the components slightly so they're not clumped together
        //I did this because the default text is tiny

        JLabel username= new JLabel("Username:");
        JTextField usernameBox= new JTextField();
        Main.AddObject(username,frame,layout,gbc,24,1,1,1,1);
        Main.AddObject(usernameBox,frame,layout,gbc,24,1,2,250,10);
        //Adds a username label and box to the frame
        // using the AddObject method created
        JLabel password= new JLabel("Password:");
        JPasswordField passwordBox= new JPasswordField();
        Main.AddObject(password,frame,layout,gbc,24,1,3,1,1);
        Main.AddObject(passwordBox,frame,layout,gbc,24,1,4,250,10);
        //Adds a password label and box and adds it to the frame

        JLabel detailsNotFound= new JLabel("Username or Password not recognized!");
        detailsNotFound.setForeground(Color.RED);
        detailsNotFound.setVisible(signInError);
        Main.AddObject(detailsNotFound,frame,layout,gbc,15,1,5,10,10);

        JButton signIn= new JButton("Sign in");
        JLabel newUser= new JLabel("New user?");
        //I want this "new user" label to be a bit
        // smaller than the other two labels
        JButton register= new JButton("Register");
        Main.AddObject(signIn,frame,layout,gbc,24,1,7,1,10);
        Main.AddObject(newUser,frame,layout,gbc,20,1,8,1,1);
        Main.AddObject(register,frame,layout,gbc,24,1,9,1,10);
        signIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username= usernameBox.getText();
                String password=String.valueOf(passwordBox.getPassword());
                Main.ResetFrame(frame,gbc);
                Connection conn= DatabaseConnection.StartConnection();
                User newUser= new User();
                boolean userFound= newUser.getUserInfo(conn,username,password,frame,layout,gbc);
                if (userFound== false){
                    try {
                        frame.dispose();
                        conn.close();
                        JFrame newFrame = new JFrame("Global Music");
                        layout.setConstraints(newFrame, gbc);
                        newFrame.setLayout(layout);
                        Image icon = Toolkit.getDefaultToolkit().getImage("Assets\\GlobalMusic.jpg");
                        newFrame.setIconImage(icon);
                        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        GUI.SignIn.main(newFrame, layout, gbc, true);
                    } catch (SQLException sqle){
                        sqle.printStackTrace();
                    }
                } else if (newUser.userType.equals("Organiser")){
                    OrganiserHome.main(frame,layout,gbc,conn,newUser);
                } else if (newUser.userType.equals("Admin")) {
                    AdminHome.main(frame,layout,gbc,conn);
                } else if (newUser.userType.equals("Individual") || newUser.userType.equals("Corporate")){
                    CustomerHome.main(frame, layout, gbc, conn, newUser);
                }
            }
        });
        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.ResetFrame(frame,gbc);
                Connection conn= DatabaseConnection.StartConnection();
                Register.main(frame,layout,gbc,conn,false);
            }
        });
        //Adds two buttons with a label between the two,
        // same method called as always

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.setVisible(true);
        //Format the frame with the components and make it visible
    }

}
