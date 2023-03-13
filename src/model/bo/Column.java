package model.bo;

public class Column {
    private String name;
    private String alias;
    private Table table;
    public Column(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
    public String getName() {
        return name;
    }
    public String getAlias() {
        return alias;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    @Override
    public String toString() {
        return getName();
    }
}
