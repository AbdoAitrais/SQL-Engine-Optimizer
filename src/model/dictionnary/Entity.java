package model.dictionnary;

import model.bo.Selection;
import model.bo.Table;
import model.exceptions.ColumnNotFoundException;

import java.util.List;
import java.util.Objects;

public class Entity {
    Table table;
    int Nt;
    int tailleLigneT;
    int FBT;
    double AvgOrder = 5;
    boolean indexSecondaire;
    List<MetaColumn> metaColumns;

    public Table getTable() {
        return table;
    }

    public int getNt() {
        return Nt;
    }

    public int getFBT() {
        return FBT;
    }

    public double getAvgOrder() {
        return AvgOrder;
    }

    public boolean isIndexSecondaire() {
        return indexSecondaire;
    }

    public Entity(Table table, int nt, int tailleLigneT, int FBT, List<MetaColumn> metaColumns) {
        this.table = table;
        Nt = nt;
        this.tailleLigneT = tailleLigneT;
        this.FBT = FBT;
        this.metaColumns = metaColumns;
    }
    public double calculateFBMT(){
        return (double) ((Dictionnary.TailleBloc - Dictionnary.TailleDescripteurBloc)/ tailleLigneT);
    }

    public double calculateBT(){
        return (double) (Nt / FBT);
    }

    private MetaColumn findByColumnName(String colName){
        for (MetaColumn col:metaColumns) {
            if (Objects.equals(col.columnName, colName)){
                return col;
            }
        }
        return null;
    }
    public double calucalteSelectivityFactor(Selection selection) throws ColumnNotFoundException {
        MetaColumn metaColumn = findByColumnName(selection.getColName());
        if (metaColumn == null)
            throw new ColumnNotFoundException();
        return (double) (1 / (metaColumn.card));
    }
    public double calculateSel(Selection selection) throws ColumnNotFoundException {
        return calucalteSelectivityFactor(selection)*Nt;
    }
    public double calculateHauteur(){
        return Math.log(Nt) / Math.log(AvgOrder);///TODO::estimate Nt
    }
    public double calculateFBMt(){
        return (Dictionnary.TailleBloc-Dictionnary.TailleDescripteurBloc)/tailleLigneT;
    }
    public double calculateFBt(){
        return Dictionnary.TR*calculateFBMt();
    }
    public double calculateTHt(){
        return Nt/calculateFBt();
    }
}