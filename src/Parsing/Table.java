package Parsing;

import java.util.ArrayList;

public class Table {
    private String name;
    private String alias;
    ArrayList<Column> columns;
    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return getName();
    }
}
