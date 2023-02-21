package BusinessObject;

public class Jointure extends Node{
    String condition;

    public Jointure(String condition) {
        this.condition = condition;
    }
    public Jointure(String condition, Node table1, Node table2) {
        super(table1,table2);
        this.condition = condition;
    }
    @Override
    public String toString() {
        return "⟕" + " " + condition;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
