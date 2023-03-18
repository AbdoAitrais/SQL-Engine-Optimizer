package model.bo;

import model.dictionnary.Dictionnary;

import java.util.Objects;

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
    public double NbrLignes() {
        return Objects.requireNonNull(Dictionnary.findEntityByTableName(table.getName())).getNt();
    }

    @Override
    public double cost() {
        return 0.0;
    }
}
