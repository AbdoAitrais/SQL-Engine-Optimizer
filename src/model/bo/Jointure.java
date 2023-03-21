package model.bo;

import controler.Estimator;
import model.utilities.Algorithms;

public class Jointure extends Node{
    String condition;
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    String algorithm;
    Table table1;
    Table table2;

    public void setTable2(Table table2) {
        this.table2 = table2;
    }

    public void setTable1(Table tab1) {
        this.table1 = tab1;
    }
    
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
        // return "⋈ " + condition ;
        return "⋈" ;
    }

    @Override
    public double NbrLignes() {
        return ((left.NbrLignes()* right.NbrLignes())*0.6);
    }

    @Override
    public double cost() {
        switch (algorithm){
            case Algorithms.BIB : {
                return Estimator.boucleImbriqueBlocs(this);
            }
            case Algorithms.BII : {
                return Estimator.boucleImbriqueIndex(this);
            }
            case Algorithms.JH : {
                return Estimator.jointureHashage(this);
            }
            case Algorithms.JTF : {
                return Estimator.jointureTriFusion(this);
            }
            case Algorithms.PJ : {
                return Estimator.preJointure(this);
            }
        }
        return 0.0;
    }
}
