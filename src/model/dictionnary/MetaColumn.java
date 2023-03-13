package model.dictionnary;

public class MetaColumn {

    String columnName;
    int card;
    int min;
    int max;

    public MetaColumn(String colonne, int card, int min, int max) {
        this.columnName = colonne;
        this.card = card;
        this.min = min;
        this.max = max;
    }
}