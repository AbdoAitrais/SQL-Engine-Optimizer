package view;
import javax.swing.*;

import model.exceptions.InvalidSQLException;
import model.exceptions.TableNotExistException;

import java.awt.*;
import java.awt.event.*;

public class SQLQueryOptimizer extends JFrame {

    private JPanel panel;
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JTextArea queryTextArea;
    private JButton submitButton;

    public SQLQueryOptimizer() 
    {
        setTitle("SQL Query Optimizer");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the panel's background color to a blue-gray shade
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xE5E5E5));
        // create the image label and add it to the panel's center
        imageLabel = new JLabel(new ImageIcon("./query.jpg"));
        imageLabel.setPreferredSize(new Dimension(600, 500));
        panel.add(imageLabel, BorderLayout.CENTER);
        // create the overlay panel and add it to the panel
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        panel.add(overlayPanel);
        // create the title label and add it to the overlay panel's north
        titleLabel = new JLabel("SQL Query Optimizer");
        titleLabel.setFont(new Font("Open Sans", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0x3F51B5));
        overlayPanel.add(titleLabel, BorderLayout.NORTH);

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
        queryTextArea.setFont(new Font("Open Sans", Font.PLAIN, 16));
        inputPanel.add(queryTextArea);

        // create the submit button and add it to the input panel
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.setBackground(new Color(0x3F51B5));
        submitButton.setForeground(new Color(0xFFFFFF));
        submitButton.setFont(new Font("Open Sans", Font.PLAIN, 14));
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = queryTextArea.getText();
                query.replace("\n","\t");
                try {
                    new AlgebraicTreeViewer(query);
                } catch (InvalidSQLException | TableNotExistException e1) {
                    
                    e1.printStackTrace();
                }
                                
            }
        });
        inputPanel.add(submitButton);

        overlayPanel.add(inputPanel, BorderLayout.CENTER);

        // create a new panel and add it below the input panel
        JPanel additionalPanel = new JPanel();
        additionalPanel.setOpaque(false);
        additionalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // add any additional components you want here
        // JLabel additionalLabel = new JLabel("Additional Component:");
        // additionalPanel.add(additionalLabel);

        overlayPanel.add(additionalPanel, BorderLayout.SOUTH);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);

        
}

   
}
