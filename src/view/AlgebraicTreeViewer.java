package view;

import controler.Estimator;
import controler.Optimizer;
import model.bo.LogicalTree;
import model.bo.Node;
import controler.Transformer;
import model.dictionnary.Dictionnary;
import model.exceptions.InvalidSQLException;
import model.exceptions.TableNotExistException;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class AlgebraicTreeViewer {
    
    static String query;
    JFrame  frame;
    Node minLogical = null;
    Node minPhysical = null;
    double minValue;
    public AlgebraicTreeViewer(String req) throws InvalidSQLException, TableNotExistException
    {
        
        query  = req;
        Optimizer optimizer = new Optimizer();
        Estimator estimator = new Estimator();
        //  String query = "SELECT a.col1 as c, * FROM table1 as t1, table2 t2, table3 t3 WHERE t1.column4 = t3.col2 AND t1.column = 55";
        //String query = "SELECT * FROM Al as  A,Bl as B , Cib C, Derb D where A.id = B.id and B.age = 5 AND B.id = C.id AND C.name > 18";
        //String query =  "SELECT country from employees emp, department dep where emp.department_id = dep.department_id ";
        //String query =  "SELECT country from employees emp, where  country='631' and salary > 1000";


        optimizer.queryComponentExtraction(query);

        System.out.println();
        System.out.println();

        optimizer.getQuery().createTree();
        Transformer transformer = new Transformer(optimizer.getQuery().getRoot());
        transformer.generateVariantTrees(transformer.mainRoot);
        transformer.createAllPhysicalTrees();

        Dictionnary dictionnary = new Dictionnary();
        dictionnary.ReadDataFromFileDictionary();



        frame = new JFrame("Algebraic Tree Viewer");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<Node> logTrees = new ArrayList<>();
        ArrayList<Node> phylTrees = new ArrayList<>();
        for (LogicalTree node:transformer.logicalTrees) {
            logTrees.add(node.getLogicalTree());
        }
        for (LogicalTree node:transformer.logicalTrees) {
            phylTrees.addAll(node.getPhysicalTrees());
        }
        System.out.println("Logical Trees : "+transformer.logicalTrees.size());
        System.out.println("Physical Trees : "+phylTrees.size());

        System.out.println(estimator.coutAvecMaterialisation(transformer.logicalTrees.get(0).getPhysicalTrees().get(0)));

        minValue = Double.MAX_VALUE;
        for (LogicalTree logicalTree: transformer.logicalTrees){
            Hashtable<Node,Double> pt = estimator.calculateCostMaterialization(logicalTree);

            double min = Collections.min(pt.values());
            System.out.println(min);
            if (min < minValue){
                minValue = min;
                minLogical = logicalTree.getLogicalTree();
                for (Map.Entry<Node, Double> entry : pt.entrySet()) {
                    if (entry.getValue().equals(min)) {
                        minPhysical = entry.getKey();
                        break; // stop searching once we find the first key associated with the value
                    }
                }
            }
        }

        System.out.println("minimum Cost : "+minValue);

        TreeList logicalTrees = new TreeList(logTrees);
        TreeList physicalTrees = new TreeList(phylTrees);
        JTabbedPane graphicTrees = new JTabbedPane();

        graphicTrees.addTab("Logical Trees",null,logicalTrees);
        graphicTrees.addTab("Physical Trees",null,physicalTrees);

        frame.getContentPane().add(graphicTrees);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                
                frame.dispose();
                frame = null;
            }
        });
        frame.pack();
        frame.setSize(700,500);
        frame.setVisible(true);
        

    }

}

