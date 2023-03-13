package view;

import model.bo.Node;
import model.bo.Transformer;

import javax.swing.*;
import java.awt.*;

public class TreeListPane extends JPanel {
    private JPanel mainPanel;
    Transformer transformer;
    public TreeListPane(Transformer transformer) {
        this.transformer = transformer;
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1,transformer.trees.size())); // Set an appropriate layout for overall needs
        for (Node node:transformer.trees) {
            JPanel panel = new AlgebraicTreePanel(node);
            panel.setPreferredSize(new Dimension(400,getMaximumSize().height/40));
            panel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
            mainPanel.add(panel);
        }
        add(mainPanel);
    }
}
