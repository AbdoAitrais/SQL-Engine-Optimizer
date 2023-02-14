package Parsing;

public class Main {
    public static void main(String[] args) throws Exception {
        Optimizer optimizer = new Optimizer();
        String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2 WHERE column1 = 'value1' AND column2 = 'value2' OR column3 >= 5";

        optimizer.queryComponentExtraction(query);
        for (String s:optimizer.columns) {
            System.out.println(s);
        }
        for (String s:optimizer.tables) {
            System.out.println(s);
        }

        System.out.println(optimizer.whereClause);

    }
}
