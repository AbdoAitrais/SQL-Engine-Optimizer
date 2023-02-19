package Parsing;

import DefinedExceptions.InvalidSQLException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Optimizer {
    public static final String SELECT_PATTERN = "\\s*SELECT\\s+(.*?)\\s+FROM";
    public static final String FROM_PATTERN = "\\s*FROM\\s+(.*?)\\s+WHERE";
    public static final String WHERE_PATTERN = "\\s*WHERE\\s+(.*)";
    public static final String LIST_PATTERN = "(\\s*,\\s*)";
    public static final String ALIAS_PATTERN = "(\\s*as\\s*)|(\\s+)";
    public static final String POINT_PATTERN = "(\\.)";
    Query query;

    private String[] removeAlias(String name){
        return name.split(ALIAS_PATTERN);
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

    public void queryComponentExtraction(String requete) throws InvalidSQLException {
        Pattern columnPattern = Pattern.compile(SELECT_PATTERN,Pattern.CASE_INSENSITIVE);
        Pattern tablePattern = Pattern.compile(FROM_PATTERN,Pattern.CASE_INSENSITIVE);
        Pattern wherePattern = Pattern.compile(WHERE_PATTERN,Pattern.CASE_INSENSITIVE);

        Matcher columnMatcher = columnPattern.matcher(requete);
        Matcher tableMatcher = tablePattern.matcher(requete);
        Matcher whereMatcher = wherePattern.matcher(requete);
        ArrayList<Column> columns = new ArrayList<>();
        ArrayList<Table> tables = new ArrayList<>();
        String whereClause = null;



        if (columnMatcher.find() && tableMatcher.find()) {
            String[] columnsWithAlias = columnMatcher.group(1).split(LIST_PATTERN);
            String[] tablesWithAlias = tableMatcher.group(1).split(LIST_PATTERN);

            for (String tableWithAlias : tablesWithAlias) {
                String[] table = removeAlias(tableWithAlias);
                if (table.length == 1)
                    tables.add(new Table(table[0],""));
                else
                    tables.add(new Table(table[0],table[1]));
            }

            for (String columnWithAlias : columnsWithAlias) {
                String[] column = removeAlias(columnWithAlias);
                if (column.length == 1)
                    columns.add(new Column(splitOnPoint(column[0]),""));
                else
                    columns.add(new Column(splitOnPoint(column[0]),column[1]));
            }


            if (whereMatcher.find()) {
                whereClause = whereMatcher.group(1);
            }else {
                System.out.println("No WHERE conditions found");
            }
        }else {
            throw new InvalidSQLException();
        }

        query = new Query(columns,tables,whereClause);
    }
}
