package model.bo;

import java.util.ArrayList;

public class Projection extends Node{
    ArrayList<Column> columns;

    public Projection(Node left, ArrayList<Column> columns) {
        super(left);
        this.columns = columns;
    }
    public Projection(){}

    @Override
    public String toString() {
        StringBuilder bf = new StringBuilder();
        bf.append("π").append(" ");    
        // bricolage until the columns get related to the tables
        if (columns != null) {
            
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null)
                {
                    
                    if (i == columns.size()-1){
                        bf.append(columns.get(i));
                        break;
                    }
                    bf.append(columns.get(i)).append(",");
                }
        }    
                       
        }
        return bf.toString();
    }

    @Override
    public double NbrLignes() {
        return 0;
    }

    @Override
    public double cost() {
        return 0.0;
    }
}
