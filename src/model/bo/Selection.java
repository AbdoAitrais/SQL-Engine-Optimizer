package model.bo;

public class Selection extends Node{
    String colName;
    String selectionValue;
    String condition;
    Table table;

    public Selection(String condition) {
        this.condition = condition;
    }
    public Selection(String condition, Table table) {
        super(new Relation(table));
        this.table = table;
        this.condition = condition;
    }
    public Selection(String condition, Table table,String colName,String selectionValue) {
        super(new Relation(table));
        this.table = table;
        this.condition = condition;
        this.colName = colName;
        this.selectionValue = selectionValue;
    }

    public String getColName() {
        return colName;
    }

    public String getSelectionValue() {
        return selectionValue;
    }

    public String getCondition() {
        return condition;
    }

    public Table getTable() {
        return table;
    }

    @Override
    public String toString() {
        return "σ" + " " + condition;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
