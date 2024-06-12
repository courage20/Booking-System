package Test;

import javax.swing.*;
import java.awt.*;

public class SwingTest{
    public static void main(JFrame frame,GridBagLayout layout,GridBagConstraints gbc){
        gbc.fill = GridBagConstraints.BOTH;
        frame.setLayout(layout);
        JLabel label1= new JLabel("Label 1");
        label1.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel label2= new JLabel("Label 2");
        label2.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel label3= new JLabel("Label 3");
        label3.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel label4= new JLabel("Label 4");
        label4.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel label5= new JLabel("Label 5");
        label5.setBorder(BorderFactory.createLineBorder(Color.black));


        Main.AddObject(label1,frame,layout,gbc,20,0,0,1,1);
        Main.AddObject(label2,frame,layout,gbc,20,1,1,1,1);
        Main.AddObject(label3,frame,layout,gbc,20,2,2,1,1);
        gbc.gridwidth= 2;
        Main.AddObject(label4,frame,layout,gbc,20,3,3,1,1);
        gbc.gridwidth= 1;
        Main.AddObject(label5,frame,layout,gbc,20,5,4,1,1);

        //AddObject(label5,frame,layout,gbc,defaultFont,2,0,10,10);


        frame.pack();
        frame.revalidate();
        frame.setVisible(true);

    }
}
