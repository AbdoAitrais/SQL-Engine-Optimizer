package Parsing;

public class Relation extends Node {
    Table table;

    public Relation(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    @Override
    public String toString() {
        return table.getName();
    }

    @Override
    public double estimate() {
        return 0;
    }
}
