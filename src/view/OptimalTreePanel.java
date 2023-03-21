package view;

import model.bo.Node;

import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptimalTreePanel extends JPanel {

    public OptimalTreePanel(Node arbrePhysique, Node arbreLogique, double coutPipe, double coutMat) {
        setLayout(new BorderLayout());

        // Create left panel and add to container
        AlgebraicTreePanel leftPanel = new AlgebraicTreePanel(arbrePhysique);
        leftPanel.setBackground(Color.YELLOW);
        add(leftPanel, BorderLayout.WEST);

        // Create right panel and add to container
        AlgebraicTreePanel rightPanel = new AlgebraicTreePanel(arbreLogique);
        rightPanel.setBackground(Color.CYAN);
        add(rightPanel, BorderLayout.EAST);

        // Create center label and add to container
        JLabel centerLabel = new JLabel("cout pipelinage : "+BigDecimal.valueOf(coutPipe).toPlainString() +"\ncout materialisation : "+BigDecimal.valueOf(coutMat).toPlainString());
        centerLabel.setForeground(Color.RED);
        centerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(centerLabel, BorderLayout.CENTER);
    }
}