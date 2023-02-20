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
    Vector<Selection> selections;
    Vector<Node> operands;
    String whereClause;
    Node root;

    public Query(ArrayList<Column> columns, ArrayList<Table> tables, String whereClause) {
        this.columns = columns;
        this.tables = tables;
        this.whereClause = whereClause;
        this.operands = new Vector<>();
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
                int index = 0;

//                if (Objects.equals(getAliasFromCondition(filter), "")){
//                    //TODO: RESEARCH for the table that contains this column
//                }else {
//                    table[index++] = getTableByAlias(getAliasFromCondition(filter));
//                }

                if (table1 == null || table2 == null)
                    throw new TableNotExistException();
                Relation relation1 = new Relation(table1);
                Relation relation2 = new Relation(table2);
                Jointure jointure = new Jointure(equation,relation1,relation2);
                jointures.add(jointure);
                operands.add(jointure);
            }else if (identifyConditionType(equation).equals(SELECTION)){
                Table table = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                if (table == null)
                    throw new TableNotExistException();
                System.out.println(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                Relation relation = new Relation(table);
                Selection selection = new Selection(equation,relation);
                selections.add(selection);
                operands.add(selection);
            }
        }
    }

    public Node createTree() throws TableNotExistException {
        makesANDconditions(whereClause);
        Node origin;
        if (jointures.size()>0){
            origin = new Projection(jointures.get(0),columns);
        }
        else {
            origin = new Projection(selections.get(0), columns);
        }

        for (int i = 0; i < operands.size(); i++) {
            Node currentOperand = operands.get(i);
            Relation relation1 = (Relation) operands.get(i).left;
            if (i+1 > operands.size()){
                Node nextOperand = operands.get(i+1);
                Relation relation2 = (Relation) operands.get(i).left;
                if (relation1 == relation2)
                    currentOperand.left = nextOperand;
                else
                    currentOperand.right = nextOperand;
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
        return origin;
    }
    private void showNode(Node leaf,int niveau_courant)
    {
        //indice pour faire les espaces entre les niveaux
        int ind;
        //s'il y a des éléments dans l'arbre
        if(leaf != null)
        {
            //on affiche d'abord l'arbre droit
            showNode(leaf.right,niveau_courant+1);
            // affichage des espaces entre les niveaux de l'arbre
            for (ind = 0; ind < niveau_courant; ind++)
                System.out.print("      ");
            //affichage du noeud courant
            System.out.println(leaf);
            //on affiche l'arbre gauche
            showNode(leaf.left,niveau_courant+1);
        } else {
            for (ind = 0; ind < niveau_courant; ind++)
                System.out.print("      ");
            System.out.println("~");
        }
    }

    void showGraphic()
    {
        if(root != null)
        {
            System.out.print("\nL'AFFICHAGE DE L'arbre\n");
            //appel de la fonction afficher_noeud avec un niveau
            //courant=0
            showNode(root,0);
            System.out.print("\n\n");
        }
    }
}
