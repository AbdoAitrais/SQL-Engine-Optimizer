package model.bo;

import controler.Estimator;
import model.dictionnary.Dictionnary;
import model.dictionnary.Entity;
import model.utilities.Algorithms;

public class Selection extends Node{
    String colName;
    String selectionValue;
    String algorithm;
    String condition;
    Table table;

    public void setColName(String colName) {
        this.colName = colName;
    }
    public void setSelectionValue(String selectionValue) {
        this.selectionValue = selectionValue;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public void setTable(Table table) {
        this.table = table;
    }
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

    public Selection(Node left, Node right, String colName, String selectionValue, String algorithm, String condition, Table table) {
        super(left, right);
        this.colName = colName;
        this.selectionValue = selectionValue;
        this.algorithm = algorithm;
        this.condition = condition;
        this.table = table;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
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
        if (algorithm != null)
            return "σ " + algorithm;
        return "σ " + condition;
    }

    @Override
    public double NbrLignes() {
        return left.NbrLignes()*0.6;
    }

    @Override
    public double cost() {
        switch (algorithm){
            case Algorithms.SE : {
                return Estimator.selectionEgaliteHashage(this);
            }
            case Algorithms.SB : {
                Entity entity = Dictionnary.findEntityByTableName(table.getName());
                return Estimator.selectionBalayage(left.NbrLignes(),entity);
            }
            case Algorithms.SNUK : {
                return Estimator.selectionCleUnique(this);
            }
        }
        return 0.0;
    }
}
