package model.bo;

public class Jointure extends Node{
    String condition;
    String algorithm;
    Table table1;
    Table table2;

    public Jointure(String condition) {
        this.condition = condition;
    }
    public Jointure(String condition, Table table1, Table table2) {
        super(new Relation(table1),new Relation(table2));
        this.table1 = table1;
        this.table2 = table2;
        this.condition = condition;
    }

    public Table getTable1() {
        return table1;
    }

    public Table getTable2() {
        return table2;
    }

    public Jointure(Node left, Node right, String condition, String algorithm, Table table1, Table table2) {
        super(left, right);
        this.condition = condition;
        this.algorithm = algorithm;
        this.table1 = table1;
        this.table2 = table2;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        if (algorithm != null)
            return "⋈ " + algorithm;
        return "⋈" ;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
