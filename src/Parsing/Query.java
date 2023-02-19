package Parsing;

import java.util.*;
import java.util.regex.*;

public class Query {
    private static final String JOINTURE = "Jointure";
    private static final String SELECTION = "Selection";
    private static final String OPERATORS_PATTERN = "<=|>=|<|>|=|!=";
    ArrayList<Column> columns;
    ArrayList<Table> tables;
    String whereClause;

    public Query(ArrayList<Column> columns, ArrayList<Table> tables, String whereClause) {
        this.columns = columns;
        this.tables = tables;
        this.whereClause = whereClause;
    }

    public static String identifyConditionType(String whereClause) {
        String[] words = whereClause.split(OPERATORS_PATTERN);
        String lastWord = words[words.length - 1].trim();

        if (lastWord.matches("^\".*\"$") || lastWord.matches("^\\d+$")) {
            return SELECTION;
        } else {
            return JOINTURE;
        }
    }

    public static ArrayList<String> extractLogicOrder(String query) {
        ArrayList<String> order = new ArrayList<>();

        String regex = "(\\s+(AND|OR)\\s+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);

        int previousIndex = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            String operator = matcher.group(2);

            // Add any characters before the logical operator to the list
            if (previousIndex < startIndex) {
                order.add(query.substring(previousIndex, startIndex));
            }

            // Add the logical operator to the list
            order.add(operator);

            previousIndex = endIndex;
        }

        // Add any characters after the last logical operator to the list
        if (previousIndex < query.length()) {
            order.add(query.substring(previousIndex));
        }

        return order;
    }
    public static void main(String[] args) {
        String query1 = "age > 25 AND department = 'Sales' OR salary < 5000";
        String query = "age > 25 AND department = 'Sales' AND salary < 5000";
        ArrayList<String> order = extractLogicOrder(query);
        System.out.println(order); // Output: [age > 25, AND, department = 'Sales', OR, salary < 5000]
    }

    private Table getTableByAlias(String alias){
        for (Table t:tables) {
            if (Objects.equals(t.getAlias(), alias))
                return t;
        }
        return null;
    }
    private String getAliasFromCondition(String conditionColumn){

        if (conditionColumn.contains(Optimizer.POINT_PATTERN)){
            return conditionColumn.split(Optimizer.POINT_PATTERN)[0];
        }
        return "";
    }
    public Node makesANDconditions(String conditions){
        String[] equations = conditions.split("AND");
        Stack<Node> nodes = new Stack<>();
        for (String equation:equations) {
            if(identifyConditionType(equation).equals(JOINTURE)){
                /// e.id = d.id
                /// getTableByAlias(alias);
                ArrayList<Table> table = new ArrayList<>();
                int index = 0;
                for (String filter:equation.split(OPERATORS_PATTERN)) {
                    if (Objects.equals(getAliasFromCondition(filter), "")){
                        //TODO: RESEARCH for the table that contains this column
                    }else {
                        table.add(getTableByAlias(getAliasFromCondition(filter)));
                    }
                }
                Jointure jointure = new Jointure(equation);
                jointure.left = new Relation(table.get(0));
                jointure.right = new Relation(table.get(1));
                //nodes.push()
            }else if (identifyConditionType(equation).equals(SELECTION)){
                Selection selection = new Selection();
            }
        }
        return null;
    }

}
