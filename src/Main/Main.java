package Main;

import GUI.*;

import javax.swing.*;
import java.awt.*;

public class Main{

    public static void AddObject(Component component, Container frame, GridBagLayout layout, GridBagConstraints gbc,
                                 int fontSize, int gridx, int gridy, int ipadx, int ipady) {

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        //Sets the position of the component

        gbc.ipadx = ipadx;
        gbc.ipady = ipady;
        //Sets the size of the component

        Font defaultFont = new Font("Arial", Font.PLAIN, fontSize);
        component.setFont(defaultFont);
        layout.setConstraints(component, gbc);
        frame.add(component);
        //Applies the constraints before adding the component
    }

    public static void ResetFrame(JFrame frame, GridBagConstraints gbc) {
        gbc.gridheight= 1;
        gbc.gridwidth= 1;
        gbc.fill= GridBagConstraints.BOTH;
        frame.getContentPane().removeAll();
    }

    public static void main(String[] args) {
        JFrame frame= new JFrame("Global Music");
        GridBagLayout layout= new GridBagLayout();
        GridBagConstraints gbc= new GridBagConstraints();
        layout.setConstraints(frame,gbc);
        frame.setLayout(layout);

        Image icon= Toolkit.getDefaultToolkit().getImage("Assets\\GlobalMusic.jpg");
        frame.setIconImage(icon);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //SwingTest.main(frame,layout,gbc);
        SignIn.main(frame,layout,gbc,false);
    }
}

// Lines 10 to 26 AddObject method concept referenced from:
//  https://stack(overflow.com/questions/21010799/looking-for-general-method-for-gridbaglayout-component-creation

// Administrator aspect of program is accessible using username "SystemAdmin" and password "AdminPassword"