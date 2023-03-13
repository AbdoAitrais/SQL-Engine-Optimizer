package view;

import model.bo.Transformer;

import javax.swing.*;
import java.awt.*;

public class TreeList extends JPanel {
    public TreeList(Transformer transformer) {
        setLayout(new BorderLayout());
        add(new JScrollPane(new TreeListPane(transformer)));
    }
}
