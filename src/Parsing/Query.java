package Parsing;

import DefinedExceptions.TableNotExistException;

import java.util.*;
import java.util.regex.*;

public class Query {
    private static final String JOINTURE = "Jointure";
    private static final String SELECTION = "Selection";
    private static final String OPERATORS_PATTERN = "\\s*(<=|>=|<|>|=|!=)\\s*";
    private static final String OR_PATTERN = "(?i)\\s*(OR)\\s*";
    private static final String AND_PATTERN = "(?i)\\s*(AND)\\s*";
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

        if (conditionColumn.contains(".")){
            return conditionColumn.split(Optimizer.POINT_PATTERN)[0];
        }
        return "";
    }
    public void makesANDconditions(String conditions) throws IndexOutOfBoundsException, TableNotExistException {
        String[] equations = conditions.split(AND_PATTERN);

        for (String equation:equations) {
            if(identifyConditionType(equation).equals(JOINTURE)){
                Table table1 = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                Table table2 = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[1]));

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
            }else if (identifyConditionType(equation).equals(SELECTION)){
                Table table = getTableByAlias(getAliasFromCondition(equation.trim().split(OPERATORS_PATTERN)[0]));
                if (table == null)
                    throw new TableNotExistException();
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
    public Node createTreeJoin(Node leaf) throws TableNotExistException {
        if (jointures.size() > 1){
            Jointure nd1 = findJointureByTable(((Relation) leaf.left).table);
            Jointure nd2 = findJointureByTable(((Relation) leaf.right).table);
            if (nd1 != null){
                leaf.left = nd1;
                jointures.remove(nd1);
                createTreeJoin(leaf.left);
            }
            if (nd2 != null){
                leaf.right = nd2;
                jointures.remove(nd2);
                createTreeJoin(leaf.right);
            }
        }
        return leaf;
    }
    private Jointure findJointureByTable(Table table){
        for (int i = 1; i < jointures.size(); i++) {
            if (Objects.equals(table.getName(), ((Relation) jointures.get(i).left).table.getName())
                    || Objects.equals(table.getName(), ((Relation) jointures.get(i).right).table.getName()))
                return jointures.get(i);
        }
        return null;
    }
     public Node createAndNodes(String andConditions) throws TableNotExistException {
        makesANDconditions(andConditions);
        Node origin = null;
        if (!selections.isEmpty()){
            origin = createTreeSelection(0);
            Node temp = origin;

            while (temp.left != null)
                temp = temp.left;

            if (!jointures.isEmpty())
                temp.left = createTreeJoin(jointures.get(0));
            jointures.clear();
            selections.clear();
            return origin;
        }
        origin = jointures.get(0);
        jointures.clear();
         return origin;
     }
    public void createTree() throws TableNotExistException {
        String[] orSplitConditions = whereClause.split(OR_PATTERN);
        ArrayList<Node> nodes = new ArrayList<>();
        for (String cond:orSplitConditions) {
            nodes.add(createAndNodes(cond));
        }
        root = new Projection(unifyNodes(nodes.get(0),nodes),columns);
    }
    private Node unifyNodes(Node leaf,ArrayList<Node> nodes){
        Node temp = null;
        if (nodes.size() > 1){
            temp = nodes.get(1);
            nodes.remove(temp);
            return unifyNodes(new Union(leaf,temp),nodes);
        }
        return leaf;
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

