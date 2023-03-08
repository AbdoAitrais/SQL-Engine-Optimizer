package bo;

import java.util.Objects;

public class Table {
    private String name;
    private String alias;
    private Column[] columns;
    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
    public Table(Table table){
        this.name = table.name;
        this.alias = table.alias;
        this.columns = table.columns;
    }

    public Table(String name, String alias, Column[] columns) {
        this.name = name;
        this.alias = alias;
        this.columns = columns;
    }

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
    public boolean findColumn(String colName){
        for (Column column:columns) {
            if (Objects.equals(column.getName(), colName))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
