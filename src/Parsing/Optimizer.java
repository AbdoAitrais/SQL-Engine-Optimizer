package Parsing;

import DefinedExceptions.InvalidSQLException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Optimizer {
    private static final String SELECT_PATTERN = "\\s*SELECT\\s+(.*?)\\s+FROM";
    private static final String FROM_PATTERN = "\\s*FROM\\s+(.*?)\\s+WHERE";
    private static final String WHERE_PATTERN = "\\s*WHERE\\s+(.*)";
    private static final String LIST_PATTERN = "(\\s*,\\s*)";
    private static final String ALIAS_PATTERN = "(\\s*as\\s*)|(\\s+)";
    private static final String POINT_PATTERN = "(\\.)";

    ArrayList<String> columns;
    ArrayList<String> tables;
    String whereClause;

    private String removeAlias(String name){
        return name.split(ALIAS_PATTERN)[0];
    }
    private String splitOnPoint(String name){
        String[] tokens = name.split(POINT_PATTERN);
        String str = null;

        if (tokens.length == 1){
            str = tokens[0];
        } else if (tokens.length == 2) {
            str = tokens[1];
        }
        return str;
    }

    public void queryComponentExtraction(String query) throws InvalidSQLException {
        Pattern columnPattern = Pattern.compile(SELECT_PATTERN,Pattern.CASE_INSENSITIVE);
        Pattern tablePattern = Pattern.compile(FROM_PATTERN,Pattern.CASE_INSENSITIVE);
        Pattern wherePattern = Pattern.compile(WHERE_PATTERN,Pattern.CASE_INSENSITIVE);

        Matcher columnMatcher = columnPattern.matcher(query);
        Matcher tableMatcher = tablePattern.matcher(query);
        Matcher whereMatcher = wherePattern.matcher(query);

        columns = new ArrayList<>();
        tables = new ArrayList<>();

        if (columnMatcher.find() && tableMatcher.find()) {
            String[] columnsWithAlias = columnMatcher.group(1).split(LIST_PATTERN);
            String[] tablesWithAlias = tableMatcher.group(1).split(LIST_PATTERN);

            for (String columnWithAlias : columnsWithAlias) {
                columns.add(splitOnPoint(removeAlias(columnWithAlias)));
            }

            for (String tableWithAlias : tablesWithAlias) {
                columns.add(removeAlias(tableWithAlias));
            }


            if (whereMatcher.find()) {
                whereClause = whereMatcher.group(1);
            }else {
                System.out.println("No WHERE conditions found");
            }
        }else {
            throw new InvalidSQLException();
        }

    }
}
