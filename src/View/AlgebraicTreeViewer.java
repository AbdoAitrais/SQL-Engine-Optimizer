package View;

import BusinessObject.*;
import DefinedExceptions.*;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class AlgebraicTreeViewer {
    
    public static void main(String[] args) throws InvalidSQLException, TableNotExistException {
        // AlgebraicTreePanel panelsTrees[] = new AlgebraicTreeViewer()[];
        Vector<AlgebraicTreePanel> panels = new Vector<AlgebraicTreePanel>();
        Optimizer optimizer = new Optimizer();
        // String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2, table3 t3, table4 t4 WHERE t1.column1 = t2.column3 AND t1.column4 = t3.column3 AND t1.column = 55";
        //String query = "SELECT * FROM Al as  A,Bl as B , Cib C, Derb D where A.id = B.id and B.age = 5 AND B.id = C.id OR C.name > 18 OR A.name = 56";
        String query =  "SELECT country from employees emp, department dep where emp.department_id = dep.department_id ";
        // String query =  "SELECT country from employees emp, where  country='631'";
        

        optimizer.queryComponentExtraction(query);

        System.out.println("");
        System.out.println("");

        optimizer.getQuery().createTree();
        Transformer transformer = new Transformer(optimizer.getQuery().getRoot());
        transformer.createEquivalence();        
            for (Node node: transformer.trees) {                
                panels.add(new AlgebraicTreePanel(node));            
        }

        // Create the frame to display the panel.
        JFrame frame = new JFrame("Algebraic Tree Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,5,10,10));
        for (AlgebraicTreePanel panel : panels) 
        {            
            panel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
            mainPanel.add(panel);
        }
        
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setSize(700,500);
        frame.setVisible(true);
    }
}

