package view;

import model.bo.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TreeList extends JPanel {
    public TreeList(ArrayList<Node> trees) {
        setLayout(new BorderLayout());
        add(new JScrollPane(new TreeListPane(trees)));
    }
}
