package Parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Optimiseur {

    private static final String SELECT_PATTERN = "^\\s*SELECT\\s+(.*)\\s+FROM\\s+(\\S+)\\s*(.*)$";
    private static final String WHERE_PATTERN = "^\\s*WHERE\\s+(.*)$";
    private static final String COLUMN_PATTERN = "([^,]+)";
    private static final Pattern selectPattern = Pattern.compile(SELECT_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern wherePattern = Pattern.compile(WHERE_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern columnPattern = Pattern.compile(COLUMN_PATTERN);

    public static void main(String[] args) {
        String query = "SELECT col1, col2, col3 FROM table_name WHERE condition1 = value1 AND condition2='value 2' OR condition3 IS NULL";

        Matcher selectMatcher = selectPattern.matcher(query);

        if (selectMatcher.find()) {
            String columns = selectMatcher.group(1);
            String tableName = selectMatcher.group(2);
            String conditions = selectMatcher.group(3);

            System.out.println("Table Name: " + tableName);

            Matcher columnMatcher = columnPattern.matcher(columns);
            System.out.println("Columns:");
            while (columnMatcher.find()) {
                System.out.println(columnMatcher.group(1));
            }

            Matcher whereMatcher = wherePattern.matcher(conditions);

            if (whereMatcher.find()) {
                String whereConditions = whereMatcher.group(1);

                System.out.println("Where Conditions:"+whereConditions);

            } else {
                    System.out.println("No WHERE conditions found");
            }
        } else {
            System.out.println("Invalid SQL query");
        }
    }

}

