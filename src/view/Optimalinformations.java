
package view ;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.bo.Node;

public class Optimalinformations {
    private JFrame frame =  new JFrame("Optimal Trees");
    private JPanel panel1, panel2;
    private JLabel label1, label2;
    
    public Optimalinformations(Node logic,Node physic,double pip, double mat) {
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // create the two panels
        panel1 = new AlgebraicTreePanel(logic);
        // panel1.setBackground(Color.BLUE);
        panel2 = new AlgebraicTreePanel(physic);
        // panel2.setBackground(Color.RED);        // create the labels
        label1 = new JLabel("cout avec materialisation  : "+mat);
        label2 = new JLabel("cout avec piplinage  : "+pip);
        label1.setFont(new java.awt.Font("DejaVu Sans Mono", 1, 18)); label2.setFont(new java.awt.Font("DejaVu Sans Mono", 1, 18)); 
        label1.setForeground(new java.awt.Color(51, 51, 255));label2.setForeground(new java.awt.Color(51, 51, 255));
        
        
        // create a GridBagLayout with default constraints
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // set the panel1 to occupy the left half of the frame and fill the available space
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(panel1, gbc);
        
        // set the label1 to occupy the space below panel1 and fill the horizontal space
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(label1, gbc);
        
        // set the panel2 to occupy the right half of the frame and fill the available space
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(panel2, gbc);
        
        // set the label2 to occupy the space below panel2 and fill the horizontal space
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(label2, gbc);
        
        // set the preferred size of the panels and labels
        panel1.setPreferredSize(new java.awt.Dimension(300, 300));
        panel2.setPreferredSize(new java.awt.Dimension(300, 300));
        label1.setPreferredSize(new java.awt.Dimension(300, 30));
        label2.setPreferredSize(new java.awt.Dimension(300, 30));
        

        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {                
                frame.dispose();
                frame = null;
                
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null); // center the frame on the screen
        frame.setVisible(true);

    }
    
    
}
