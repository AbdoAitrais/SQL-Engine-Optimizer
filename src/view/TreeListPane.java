package view;

import model.bo.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TreeListPane extends JPanel {
    private JPanel mainPanel;
    ArrayList<Node> trees;
    public TreeListPane(ArrayList<Node> trees) {
        this.trees = trees;
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1,trees.size())); // Set an appropriate layout for overall needs
        for (Node node:trees) {
            JPanel panel = new AlgebraicTreePanel(node);
            panel.setPreferredSize(new Dimension(400,getMaximumSize().height/20));
            panel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
            mainPanel.add(panel);
        }
        add(mainPanel);
    }
}
