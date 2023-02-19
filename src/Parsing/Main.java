package Parsing;

public class Main {
    public static void main(String[] args) throws Exception {
        Optimizer optimizer = new Optimizer();
        String query = "SELECT a.column1 as c, * FROM table1 as t1, table2 t2 WHERE column1 = 'value1' AND column2 = 'value2' OR column3 >= 5";

        optimizer.queryComponentExtraction(query);
        for (Table t:optimizer.query.tables) {
            System.out.println(t.getName());
        }
        for (Column c:optimizer.query.columns) {
            System.out.println(c.getName());
        }
        System.out.println(optimizer.query.whereClause);

    }
}
