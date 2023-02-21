package BusinessObject;

import java.util.ArrayList;

public class Projection extends Node{
    ArrayList<Column> columns;

    public Projection(Node left, ArrayList<Column> columns) {
        super(left);
        this.columns = columns;
    }

    @Override
    public String toString() {
        StringBuilder bf = new StringBuilder();
        bf.append("π").append(" ");
        for (int i = 0; i < columns.size(); i++) {
            if (i == columns.size()-1){
                bf.append(columns.get(i));
                break;
            }
            bf.append(columns.get(i)).append(",");
        }
        return bf.toString();
    }

    @Override
    public double estimate() {
        return 0;
    }
}
