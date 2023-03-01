package BusinessObject;

public class Jointure extends Node{
    String condition;
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
    
    @Override
    public String toString() {
        return "⋈" ;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
