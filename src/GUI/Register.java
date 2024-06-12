package GUI;
import JDBC.User;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Register{

    public static void IndividualCustomer(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn){

        JTextField[] allTextFields= new JTextField[14];

        JTextField customerType= new JTextField("Individual");
        allTextFields[0]= customerType;
        JTextField organiserName= new JTextField("null");
        allTextFields[4]= organiserName;

        JLabel firstNameLabel= new JLabel("First Name (30):");
        JTextField firstName= new JTextField();
        allTextFields[1]= firstName;
        Main.AddObject(firstNameLabel,frame,layout,gbc,20,1,2,10,10);
        Main.AddObject(firstName,frame,layout,gbc,15,1,3,250,15);

        JLabel lastNameLabel= new JLabel("Last Name (30):");
        JTextField lastName= new JTextField();
        allTextFields[2]= lastName;
        Main.AddObject(lastNameLabel,frame,layout,gbc,20,1,4,10,10);
        Main.AddObject(lastName,frame,layout,gbc,15,1,5,250,15);

        JTextField organisationName= new JTextField("N/A");
        allTextFields[3]= organisationName;

        getCustomerDetails(frame,layout,gbc,allTextFields,conn);
    }

    public static void CorporateCustomer(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn){

        JTextField[] allTextFields= new JTextField[14];

        JTextField customerType= new JTextField("Corporate");
        allTextFields[0]= customerType;
        JTextField organiserName= new JTextField("null");
        allTextFields[4]= organiserName;

        JLabel firstNameLabel= new JLabel("Representative First Name (30):");
        JTextField firstName= new JTextField();
        allTextFields[1]= firstName;
        Main.AddObject(firstNameLabel,frame,layout,gbc,20,1,2,10,10);
        Main.AddObject(firstName,frame,layout,gbc,15,1,3,250,15);

        JLabel lastNameLabel= new JLabel("Representative Last Name (30):");
        JTextField lastName= new JTextField();
        allTextFields[2]= lastName;
        Main.AddObject(lastNameLabel,frame,layout,gbc,20,1,4,10,10);
        Main.AddObject(lastName,frame,layout,gbc,15,1,5,250,15);

        JLabel organisationNameLabel= new JLabel("Organisation Name (30):");
        JTextField organisationName= new JTextField();
        allTextFields[3]= organisationName;
        Main.AddObject(organisationNameLabel,frame,layout,gbc,20,1,6,10,10);
        Main.AddObject(organisationName,frame,layout,gbc,15,1,7,250,15);

        getCustomerDetails(frame,layout,gbc,allTextFields,conn);
    }

    public static void getCustomerDetails(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,JTextField[] allTextFields, Connection conn){

        JLabel addressLabel= new JLabel("Address (30):");
        JTextField address= new JTextField();
        allTextFields[5]= address;
        Main.AddObject(addressLabel,frame,layout,gbc,20,1,8,10,10);
        Main.AddObject(address,frame,layout,gbc,15,1,9,100,15);

        JLabel townLabel= new JLabel("Town (30):");
        JTextField town= new JTextField();
        allTextFields[6]= town;
        Main.AddObject(townLabel,frame,layout,gbc,20,1,10,10,10);
        Main.AddObject(town,frame,layout,gbc,15,1,11,100,15);

        JLabel postcodeLabel= new JLabel("Post Code (10):");
        JTextField postcode= new JTextField();
        allTextFields[7]= postcode;
        Main.AddObject(postcodeLabel,frame,layout,gbc,20,1,12,10,10);
        Main.AddObject(postcode,frame,layout,gbc,15,1,13,100,15);

        JLabel emailLabel= new JLabel("Email (30):");
        JTextField email= new JTextField();
        allTextFields[8]= email;
        Main.AddObject(emailLabel,frame,layout,gbc,20,2,2,10,10);
        Main.AddObject(email,frame,layout,gbc,15,2,3,150,15);

        JLabel phoneNumberLabel= new JLabel("Phone Number (15):");
        JTextField phoneNumber= new JTextField();
        allTextFields[9]= phoneNumber;
        Main.AddObject(phoneNumberLabel,frame,layout,gbc,20,2,4,10,10);
        Main.AddObject(phoneNumber,frame,layout,gbc,15,2,5,150,15);

        JLabel usernameLabel= new JLabel("Username (30):");
        JTextField username= new JTextField();
        allTextFields[10]= username;
        Main.AddObject(usernameLabel,frame,layout,gbc,20,2,6,10,10);
        Main.AddObject(username,frame,layout,gbc,20,2,7,150,15);

        JLabel passwordLabel= new JLabel("Password (30):");
        JTextField password= new JTextField();
        allTextFields[11]= password;
        Main.AddObject(passwordLabel,frame,layout,gbc,20,2,8,10,10);
        Main.AddObject(password,frame,layout,gbc,20,2,9,150,15);

        JLabel paymentMethodLabel= new JLabel("Payment Method:");
        String[] paymentMethodList = {"Visa Debit","Credit Card","Master Card"};
        JComboBox paymentMethodBox = new JComboBox(paymentMethodList);
        Main.AddObject(paymentMethodLabel,frame,layout,gbc,20,2,10,10,10);
        Main.AddObject(paymentMethodBox,frame,layout,gbc,20,2,11,150,10);

        JLabel accountNoLabel= new JLabel("Account Number (30):");
        JTextField accountNo= new JTextField();
        allTextFields[13]= accountNo;
        Main.AddObject(accountNoLabel,frame,layout,gbc,20,2,12,10,10);
        Main.AddObject(accountNo,frame,layout,gbc,20,2,13,150,15);

        JButton submit= new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) paymentMethodBox.getSelectedItem();
                JTextField paymentMethod= new JTextField(selectedType);
                allTextFields[12]= paymentMethod;

                String[] userDetails= new String[14];
                for (int i=0; i<userDetails.length; i++){
                    userDetails[i]=allTextFields[i].getText();
                }
                Main.ResetFrame(frame,gbc);
                User newCustomer= new User();
                boolean userAdded= newCustomer.CreateNewUserCustomer(frame,layout,gbc,conn,userDetails);
                if (userAdded){
                    Main.ResetFrame(frame,gbc);
                    CustomerHome.main(frame,layout,gbc,conn,newCustomer);
                }
                else {
                    Main.ResetFrame(frame,gbc);
                    main(frame,layout,gbc,conn,true);
                }
            }
        });
        gbc.weightx= 0.5;
        Main.AddObject(submit,frame,layout,gbc,20,2,14,10,10);

        frame.pack();
        frame.revalidate();
    }

    public static void FestivalOrganiser(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn){

        JTextField[] allTextFields= new JTextField[14];

        JTextField customerType= new JTextField("Organiser");
        allTextFields[0]= customerType;
        JTextField firstName= new JTextField("null");
        allTextFields[1]= firstName;
        JTextField lastName= new JTextField("null");
        allTextFields[2]= lastName;
        JTextField organisationName= new JTextField("null");
        allTextFields[3]= organisationName;
        JTextField address= new JTextField("null");
        allTextFields[5]= address;
        JTextField town= new JTextField("null");
        allTextFields[6]= town;
        JTextField postcode= new JTextField("null");
        allTextFields[7]= postcode;
        JTextField paymentMethod= new JTextField("null");
        allTextFields[12]= paymentMethod;
        JTextField accountNo= new JTextField("null");
        allTextFields[13]= accountNo;

        JLabel organiserNameLabel= new JLabel("Organiser Name (20):");
        JTextField organiserName= new JTextField();
        allTextFields[4]= organiserName;
        Main.AddObject(organiserNameLabel,frame,layout,gbc,20,1,2,10,10);
        Main.AddObject(organiserName,frame,layout,gbc,20,1,3,200,15);

        JLabel emailLabel= new JLabel("Email (30):");
        JTextField email= new JTextField();
        allTextFields[8]= email;
        Main.AddObject(emailLabel,frame,layout,gbc,20,1,4,10,10);
        Main.AddObject(email,frame,layout,gbc,20,1,5,200,15);

        JLabel contactNumberLabel= new JLabel("Contact Phone Number (15):");
        JTextField contactNumber= new JTextField();
        allTextFields[9]= contactNumber;
        Main.AddObject(contactNumberLabel,frame,layout,gbc,20,1,6,10,10);
        Main.AddObject(contactNumber,frame,layout,gbc,20,1,7,200,15);

        JLabel usernameLabel= new JLabel("Username (20):");
        JTextField username= new JTextField();
        allTextFields[10]= username;
        Main.AddObject(usernameLabel,frame,layout,gbc,20,2,2,10,10);
        Main.AddObject(username,frame,layout,gbc,20,2,3,200,15);

        JLabel passwordLabel= new JLabel("Password (20):");
        JTextField password= new JTextField();
        allTextFields[11]= password;
        Main.AddObject(passwordLabel,frame,layout,gbc,20,2,4,10,10);
        Main.AddObject(password,frame,layout,gbc,20,2,5,200,15);

        JButton submit= new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] userDetails= new String[14];

                for (int i=0; i<allTextFields.length; i++) {
                    userDetails[i] = allTextFields[i].getText();
                }
                User newOrganiser= new User();
                boolean userAdded= newOrganiser.CreateNewUserOrganiser(frame,layout,gbc,conn,userDetails);
                if (userAdded){
                    Main.ResetFrame(frame,gbc);
                    OrganiserHome.main(frame,layout,gbc,conn,newOrganiser);
                }
                else{
                    Main.ResetFrame(frame,gbc);
                    main(frame,layout,gbc,conn,true); }
            }
        });
        Main.AddObject(submit,frame,layout,gbc,20,2,7,10,10);
        gbc.weightx= 0.5;

        frame.pack();
        frame.revalidate();
    }

    public static void main(JFrame frame,GridBagLayout layout,GridBagConstraints gbc,Connection conn,boolean addUserError) {
        gbc.fill = GridBagConstraints.BOTH;
        //Makes sure the components fill all the space they're given
        gbc.insets = new Insets(10, 15, 10, 15);
        //Separates the components slightly so they're not clumped together
        //I did this because the default text is tiny
        //Applies all the rules defined above

        JLabel userNotAdded= new JLabel("<html>Something went wrong :(<br/>Make sure your details fit within the character limit shown in the brackets<html/>");
        userNotAdded.setForeground(Color.RED);
        userNotAdded.setVisible(addUserError);
        Main.AddObject(userNotAdded,frame,layout,gbc,16,1,1,10,10);

        JLabel userTypeLabel= new JLabel("Select the user type you wish to create.");
        String[] userList = {"Customer - Corporate", "Customer - Individual", "Festival Organiser"};
        //These are our user type options that will be in the drop down menu
        JComboBox userType = new JComboBox(userList);
        //Creates the drop down menu or "combo box" in Java
        JButton userTypeSubmit = new JButton("Submit");
        //Creates the button to submit the selected user type
        gbc.gridwidth= 2;
        Main.AddObject(userTypeLabel,frame,layout,gbc,20,1,2,10,10);
        gbc.gridwidth= 1;
        Main.AddObject(userType,frame,layout,gbc,20, 1, 3, 50, 30);
        Main.AddObject(userTypeSubmit,frame,layout,gbc,20, 2, 3, 50, 15);
        //Places and adds combo box and button to the frame
        userTypeSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Main.ResetFrame(frame,gbc);
                //This clears the frame so we can put our detail boxes in
                String selectedType = (String) userType.getSelectedItem();
                //Turns the selected user type into a string
                gbc.gridwidth= 2;
                JLabel formType = new JLabel(selectedType);
                Main.AddObject(formType,frame,layout,gbc,26,1,1,50, 15);

                gbc.gridwidth= 1;
                gbc.insets = new Insets(5, 15, 5, 15);
                //Above 3 lines adds user type to top of frame as a label

                if (selectedType.equals("Customer - Corporate")) {
                    CorporateCustomer(frame,layout,gbc,conn);
                    //Registers user as company customer by calling CorporateCustomer method
                } else if (selectedType.equals("Customer - Individual")) {
                    IndividualCustomer(frame,layout,gbc,conn);
                    //Registers user as general customer by calling IndividualCustomer method
                } else if (selectedType.equals("Festival Organiser")) {
                    FestivalOrganiser(frame,layout,gbc,conn);
                    //Registers user as festival organiser by calling FestivalOrganiser method
                }
            }
        });
        frame.pack();
        frame.revalidate();
    }
}
