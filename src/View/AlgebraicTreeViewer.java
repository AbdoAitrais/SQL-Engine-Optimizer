package View;

import BusinessObject.Column;
import BusinessObject.Node;
import BusinessObject.Optimizer;
import BusinessObject.Table;
import DefinedExceptions.InvalidSQLException;
import DefinedExceptions.TableNotExistException;

import javax.swing.*;
import java.awt.*;

public class AlgebraicTreeViewer {
    public static void main(String[] args) throws InvalidSQLException, TableNotExistException {
        Optimizer optimizer = new Optimizer();
        // String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2, table3 t3, table4 t4 WHERE t1.column1 = t2.column3 AND t1.column4 = t3.column3 AND t1.column = 55";
        String query = "SELECT * FROM Al as  A,Bl as B , Cib C, Derb D where A.id = B.id and B.age = 5 AND D.id = C.id OR C.name > 18 OR A.name = 56";

        optimizer.queryComponentExtraction(query);


        System.out.println("");
        System.out.println("");


        optimizer.getQuery().createTree();

        // Create the panel to display the algebraic tree.
        AlgebraicTreePanel panel = new AlgebraicTreePanel(optimizer.getQuery().getRoot());

        // Create the frame to display the panel.
        JFrame frame = new JFrame("Algebraic Tree Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setSize(700,500);
        frame.setVisible(true);
    }
}

