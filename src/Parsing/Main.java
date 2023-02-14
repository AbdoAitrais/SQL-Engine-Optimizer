package Parsing;

public class Main {
    public static void main(String[] args) throws Exception {
        Optimizer optimizer = new Optimizer();
        String query = "SELECT column1, * FROM table1, table2 WHERE column1 = 'value1' AND column2 = 'value2' OR column3 >= 5";

        optimizer.separateQuery(query);

    }
}
