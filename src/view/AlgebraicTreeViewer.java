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


public class AlgebraicTreeViewer {
    
    static String query;
    JFrame  frame;
    public AlgebraicTreeViewer(String req) throws InvalidSQLException, TableNotExistException
    {
        
        query  = req;
        LogicalTree.SQLspliter sqlSpliter = new LogicalTree.SQLspliter();
        Estimator estimator = new Estimator();
        //  String query = "SELECT a.col1 as c, * FROM table1 as t1, table2 t2, table3 t3 WHERE t1.col1 = t3.col2 AND t1.col2 = 55 and t3.col1 = t2.col2";
        //  String query = "SELECT a.col1 as c, * FROM table1 as t1, table2 t2, table3 t3 WHERE t1.col1 = t3.col2 AND t1.col2 = 55 and t3.col1 = t2.col2 and t2.col2 = 26";
        //String query = "SELECT * FROM Al as  A,Bl as B , Cib C, Derb D where A.id = B.id and B.age = 5 AND B.id = C.id AND C.name > 18";
        //String query =  "SELECT country from employees emp, department dep where emp.department_id = dep.department_id ";
        //String query =  "SELECT country from employees emp, where  country='631' and salary > 1000";

        sqlSpliter.queryComponentExtraction(query);

        System.out.println();
        System.out.println();

        sqlSpliter.getQuery().createTree();
        Transformer transformer = new Transformer(sqlSpliter.getQuery().getRoot());
        transformer.generateVariantTrees(transformer.mainRoot);
        transformer.createAllPhysicalTrees();

        Dictionnary dictionnary = new Dictionnary();
        dictionnary.ReadDataFromFileDictionary();



        frame = new JFrame("Algebraic Tree Viewer");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ArrayList<Node> logTrees = new ArrayList<>();
        ArrayList<Node> phylTrees = new ArrayList<>();
        for (LogicalTree node:transformer.logicalTrees) {
            logTrees.add(node.getLogicalTree());
        }
        for (LogicalTree node:transformer.logicalTrees) {
            phylTrees.addAll(node.getPhysicalTrees());
        }
        System.out.println("Logical Trees : " +transformer.logicalTrees.size());
        System.out.println("Physical Trees : " +phylTrees.size());

        System.out.println(estimator.coutAvecMaterialisation(transformer.logicalTrees.get(0).getPhysicalTrees().get(0)));

        Optimizer optimizer = new Optimizer(estimator,transformer);
        optimizer.calculateOptimalTree();
        System.out.println("minimum Cost : "+optimizer.getMinMatValue());

        TreeList logicalTrees = new TreeList(logTrees);
        TreeList physicalTrees = new TreeList(phylTrees);
        JTabbedPane graphicTrees = new JTabbedPane();
        graphicTrees.addTab(transformer.logicalTrees.size()+" Logical Trees",null,logicalTrees);
        graphicTrees.addTab(phylTrees.size()+" Physical Trees",null,physicalTrees);

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

        new Optimalinformations(optimizer.getMinLogical(), optimizer.getMinPhysical(), optimizer.getMinPipeValue(),optimizer.getMinMatValue());
        

    }

}

