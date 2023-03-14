package view;

import model.bo.*;
import controler.Optimizer;
import model.exceptions.*;
import model.bo.Node;
import model.bo.Transformer;

import javax.swing.*;
import java.util.ArrayList;

public class AlgebraicTreeViewer {

    public static void main(String[] args) throws InvalidSQLException, TableNotExistException {
        // AlgebraicTreePanel panelsTrees[] = new AlgebraicTreeViewer()[];
        Optimizer optimizer = new Optimizer();
        // String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2, table3 t3, table4 t4 WHERE t1.column1 = t2.column3 AND t1.column4 = t3.column3 AND t1.column = 55";
        String query = "SELECT * FROM Al as  A,Bl as B , Cib C, Derb D where A.id = B.id and B.age = 5 AND B.id = C.id AND C.name > 18";
        //String query =  "SELECT country from employees emp, department dep where emp.department_id = dep.department_id ";
        //String query =  "SELECT country from employees emp, where  country='631' and salary > 1000";


        optimizer.queryComponentExtraction(query);

        System.out.println("");
        System.out.println("");

        optimizer.getQuery().createTree();
        Transformer transformer = new Transformer(optimizer.getQuery().getRoot());
        transformer.generateVariantTrees(transformer.mainRoot);
        transformer.createAllPhysicalTrees();

        System.out.println("Logical Trees : "+transformer.logicalTrees.size());
        System.out.println("Physical Trees : "+transformer.physicalTrees.size());
        // Create the frame to display the panel.


        JFrame frame = new JFrame("Algebraic Tree Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TreeList logicalTrees = new TreeList((ArrayList<Node>) transformer.logicalTrees);
        TreeList physicalTrees = new TreeList((ArrayList<Node>) transformer.physicalTrees);
        JTabbedPane graphicTrees = new JTabbedPane();


        graphicTrees.addTab("Logical Trees",null,logicalTrees);
        graphicTrees.addTab("Physical Trees",null,physicalTrees);

        frame.getContentPane().add(graphicTrees);
        frame.pack();
        frame.setSize(700,500);
        frame.setVisible(true);
    }
}

