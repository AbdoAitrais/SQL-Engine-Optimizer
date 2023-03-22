package view;
import javax.swing.*;

import model.bo.LogicalTree;
import model.bo.Node;
import model.exceptions.InvalidSQLException;
import model.exceptions.TableNotExistException;

import java.awt.*;
import java.awt.event.*;


public class SQLQueryOptimizer extends JFrame {

    private JPanel panel;
    
    private JLabel titleLabel;
    private JTextArea queryTextArea;
    private JButton submitButton;
    AlgebraicTreeViewer algebraicTreeViewer = null;

    public SQLQueryOptimizer() {
        setTitle("SQL Query Optimizer");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xE5E5E5));

        
        
        

        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        panel.add(overlayPanel);

        titleLabel = new JLabel("SQL Query Optimizer");
        titleLabel.setFont(new Font("Open Sans", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0x3F51B5));
        overlayPanel.add(titleLabel, BorderLayout.NORTH);

        // create a new panel to hold the text area and label
        // create the input panel and add it to the overlay panel's center
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        queryTextArea = new JTextArea();
        queryTextArea.setLineWrap(true);
        queryTextArea.setWrapStyleWord(true);
        queryTextArea.setPreferredSize(new Dimension(400, 150));
        queryTextArea.setBackground(new Color(0xFFFFFF));
        queryTextArea.setForeground(new Color(0x333333));
        queryTextArea.setFont(new java.awt.Font("DejaVu Serif", 1, 18)); 
        inputPanel.add(queryTextArea);

        // create a label and add it below the input panel
        

        // create the submit button and add it to the input panel
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.setBackground(new java.awt.Color(51, 51, 255));
        submitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        submitButton.setFont(new java.awt.Font("Chilanka", 0, 15)); // NOI18N
        submitButton.setForeground(new java.awt.Color(255, 255, 255));



        
        submitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String query = queryTextArea.getText();
            query.replace("\n","\t");
            try {                
                    algebraicTreeViewer = new AlgebraicTreeViewer(query);                
                } catch (InvalidSQLException | TableNotExistException e1) {                
                e1.printStackTrace();
                }                            
            }
        });
        inputPanel.add(submitButton);
        overlayPanel.add(inputPanel, BorderLayout.CENTER);
        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
