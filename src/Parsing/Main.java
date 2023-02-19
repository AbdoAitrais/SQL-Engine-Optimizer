package Parsing;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Optimizer optimizer = new Optimizer();
        String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2, table3 t3, table4 t4 WHERE t1.column1 = t2.column3 AND t1.column4 = t3.column3 AND t1.column = \"abdo\"";

        optimizer.queryComponentExtraction(query);
        for (Table t:optimizer.query.tables) {
            System.out.println(t.getName());
        }
        for (Column c:optimizer.query.columns) {
            System.out.println(c.getName());
        }
        System.out.println(optimizer.query.whereClause);

        System.out.println("");
        System.out.println("");


        optimizer.query.root = optimizer.query.createTree();
        System.out.println(optimizer.query.root);
        System.out.println(optimizer.query.root.left);
        System.out.println(optimizer.query.root.left.left);
        System.out.println(optimizer.query.root.left.right);
        System.out.println(optimizer.query.root.left.right.left);
        System.out.println(optimizer.query.root.left.right.left.right);
        System.out.println(optimizer.query.root.left.right.left.left);


    }
}
