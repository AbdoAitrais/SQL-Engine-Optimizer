package Parsing;

import java.util.ArrayList;

public class Query {
    ArrayList<Column> columns;
    ArrayList<Table> tables;
    String whereClause;

    public Query(ArrayList<Column> columns, ArrayList<Table> tables, String whereClause) {
        this.columns = columns;
        this.tables = tables;
        this.whereClause = whereClause;
    }


    public String identifyConditionType(String condition) {
        if (condition.contains("=") || condition.contains("<") || condition.contains(">")) {
            String[] parts = condition.split("\\s*(=|<|>|<=|>=|<>)\\s*");
            String left = parts[0].trim();
            String right = parts[1].trim();
            if (left.contains(".") && right.contains(".")) {
                String[] leftParts = left.split("\\.");
                String[] rightParts = right.split("\\.");
                String leftTable = leftParts[0].trim();
                String rightTable = rightParts[0].trim();
                if (!leftTable.equals(rightTable)) {
                    return "join";
                }
            } else {
                return "selection";
            }
        }
        return "";
    }

}
