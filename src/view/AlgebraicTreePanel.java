package view;

import model.bo.Node;

import javax.swing.*;
import java.awt.*;

public class AlgebraicTreePanel extends JPanel {
    private Node root;

    public AlgebraicTreePanel(Node root) {
        this.root = root;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the tree recursively starting from the root node.
        drawNode(g, root, getWidth() / 2, 30, getWidth() / 6);
    }

//    private void drawNode2(Graphics g, Node node, int x, int y, int xOffset) {
//        // Draw the current node.
//        g.drawString(node.toString(), x, y);
//
//        // Draw the left child node, if it exists.
//        if (node.getLeft() != null) {
//            int childX = x - xOffset;
//            int childY = y + 50;
//            g.drawLine(x, y + 5, childX, childY - 15);
//            drawNode(g, node.getLeft(), childX, childY, xOffset / 2);
//        }
//
//        // Draw the right child node, if it exists.
//        if (node.getRight() != null) {
//            int childX = x + xOffset;
//            int childY = y + 50;
//            g.drawLine(x, y + 5, childX, childY - 15);
//            drawNode(g, node.getRight(), childX, childY, xOffset / 2);
//        }
//    }

    private void drawNode(Graphics g, Node node, int x, int y, int xOffset) {
        // Draw the current node.
        g.drawString(node.toString(), x, y);

        // Draw the left child node, if it exists.
        if (node.getLeft() != null && node.getRight() == null) {
            int childX = x;
            int childY = y + 50;
            g.drawLine(x, y + 5, childX, childY - 15);
            drawNode(g, node.getLeft(), childX, childY, xOffset / 2);
            return;
        }

        if (node.getLeft() != null) {
            int childX = (int) (x - (xOffset*2));
            int childY = y + 50;
            g.drawLine(x, y + 5, childX, childY - 15);
            drawNode(g, node.getLeft(), childX, childY, (int) (xOffset/1.1));
        }

        // Draw the right child node, if it exists.
        if (node.getRight() != null) {
            int childX = (int) (x + (xOffset*2));
            int childY = y + 50;
            g.drawLine(x, y + 5, childX, childY - 15);
            drawNode(g, node.getRight(), childX, childY, (int) (xOffset/1.1));
        }
    }


}

