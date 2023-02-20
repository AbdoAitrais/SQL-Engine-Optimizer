package Parsing;

import DefinedExceptions.TableNotExistException;

import java.util.*;
import java.util.regex.*;

public class Query {
    private static final String JOINTURE = "Jointure";
    private static final String SELECTION = "Selection";
    private static final String OPERATORS_PATTERN = "\\s*(<=|>=|<|>|=|!=)\\s*";
    ArrayList<Column> columns;
    ArrayList<Table> tables;
    Vector<Jointure> jointures;
    static Vector<Selection> selections;
    String whereClause;
    Node root;

    public Query(ArrayList<Column> columns, ArrayList<Table> tables, String whereClause) {
        this.columns = columns;
        this.tables = tables;
        this.whereClause = whereClause;
        this.jointures = new Vector<>();
        this.selections = new Vector<>();
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

    private Table getTableByAlias(String alias){
        for (Table t:tables) {
            if (Objects.equals(t.getAlias(), alias))
                return t;
        }
        return null;
    }
    private String getAliasFromCondition(String conditionColumn){
//        System.out.println(conditionColumn);
//        System.out.println(conditionColumn.contains("."));
        if (conditionColumn.contains(".")){
            return conditionColumn.split(Optimizer.POINT_PATTERN)[0];
        }
        return "";
    }
    public void makesANDconditions(String conditions) throws IndexOutOfBoundsException, TableNotExistException {
        String[] equations = conditions.split("AND");
        ArrayList<Node> nodes = new ArrayList<>();
        for (String equation:equations) {
            if(identifyConditionType(equation).equals(JOINTURE)){
                Table table1 = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                Table table2 = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[1]));
                //int index = 0;



                if (table1 == null || table2 == null)
                    throw new TableNotExistException();
                Relation relation1 = new Relation(table1);
                Relation relation2 = new Relation(table2);
                Jointure jointure = new Jointure(equation,relation1,relation2);
                jointures.add(jointure);
            }else if (identifyConditionType(equation).equals(SELECTION)){
                Table table = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                if (table == null)
                    throw new TableNotExistException();
                System.out.println(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                Relation relation = new Relation(table);
                Selection selection = new Selection(equation,relation);
                selections.add(selection);
            }
        }
    }

    public Node createTreeSelection(int index) {
        if (index >= selections.size()) {
            return null;
        }

        Node root = new Selection(selections.get(index).condition);
        root.left = createTreeSelection(index + 1);
        return root;
    }
    public Node createTreeJoin(int index) throws TableNotExistException {
        
        if (index >= selections.size()) {
            return null;
        }
        Node root = new Jointure(jointures.get(index).condition);        
        root.right = createTreeSelection(index + 1);

        return root;
    }

     public Node createTree() throws TableNotExistException {
        makesANDconditions(whereClause);        
        Node origin = null;
        origin = createTreeSelection(0);
        Node temp = origin;
        while (temp.left != null)
            temp = temp.left;    
        
        temp.left = createTreeJoin(0);
        return origin;
     }       

     
}


            
            //        for (int i = 0; i < operands.size(); i++) {
            //            Node currentOperand = operands.get(i);
            //            if (i+1 > operands.size()){
            //                Node nextOperand = operands.get(i+1);
            //                if (currentOperand.left == nextOperand.left)
            //                    currentOperand.left = nextOperand;
            //                else
            //                    currentOperand.right = nextOperand;
            //            }
            //        }