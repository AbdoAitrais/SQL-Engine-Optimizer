package Parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Optimizer {

    private ArrayList<String> columns;
    private ArrayList<String> tables;
    private String whereClause;

    public void separateQuery(String query){
        Pattern columnPattern = Pattern.compile("\\s*SELECT\\s+(.*?)\\s+FROM",Pattern.CASE_INSENSITIVE);
        Pattern tablePattern = Pattern.compile("\\s*FROM\\s+(.*?)\\s+WHERE",Pattern.CASE_INSENSITIVE);
        Pattern wherePattern = Pattern.compile("\\s*WHERE\\s+(.*)",Pattern.CASE_INSENSITIVE);

        Matcher columnMatcher = columnPattern.matcher(query);
        Matcher tableMatcher = tablePattern.matcher(query);
        Matcher whereMatcher = wherePattern.matcher(query);

        columns = new ArrayList<>();
        tables = new ArrayList<>();


        if (columnMatcher.find() && tableMatcher.find()) {
            columns.addAll(Arrays.asList(columnMatcher.group(1).split("(\\s*,\\s*)")));
            //System.out.println("Columns: " + columns.size());
            tables.addAll(Arrays.asList(tableMatcher.group(1).split("(\\s*,\\s*)")));
            //System.out.println("Tables: "+ tables.size());
            if (whereMatcher.find()) {
                //System.out.println("Where Clause: " + whereMatcher.group(1));
                whereClause = whereMatcher.group(1);
            }else {
                System.out.println("No WHERE conditions found");
            }
        }else {
            System.out.println("Invalid SQL query");
        }

    }
}
