package model.bo;

import model.dictionnary.Dictionnary;
import model.exceptions.InvalidSQLException;
import model.exceptions.TableNotExistException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;
import controler.Optimizer;

public class Query {
    private static final String JOINTURE = "Jointure";
    private static final String SELECTION = "Selection";
    private static final String OPERATORS_PATTERN = "\\s*(<=|>=|<|>|=|!=)\\s*";
    private static final String OR_PATTERN = "(?i)\\s+(OR)\\s+";
    private static final String AND_PATTERN = "(?i)\\s+(AND)\\s+";
    ArrayList<Column> columns;
    ArrayList<Table> tables;
    Vector<Jointure> jointures;
    static Vector<Selection> selections;
    String whereClause;
    Node root;

    public Node getRoot() {
        return root;
    }

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

        if (lastWord.matches("^\".*\"$")  || lastWord.matches("^\'.*\'$")  || lastWord.matches("^\\d+$")) {
            return SELECTION;
        } else {
            return JOINTURE;
        }
    }

    private Table getTableByAlias(String alias){
        for (Table t:tables) {
            if (Objects.equals(t.getAlias(), alias) || Objects.equals(t.getName(), alias))
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
    private Table getTableFromOperand(String operand){
        if (Objects.equals(getAliasFromCondition(operand), "")) {
            return Catalog.getTableByColumnName(operand);
        }else {
            return getTableByAlias(getAliasFromCondition(operand));
        }
    }
    public void makesANDconditions(String conditions) throws IndexOutOfBoundsException, TableNotExistException {
        String[] equations = conditions.split(AND_PATTERN);

        for (String equation:equations) {
            if(identifyConditionType(equation).equals(JOINTURE)){
                int index = 0;
                Table[] joinTables = new Table[2];
                String[] operands = equation.split(OPERATORS_PATTERN);
                for (String operand:operands) {
                    joinTables[index++] = getTableFromOperand(operand);
                }

                if (joinTables[0] == null || joinTables[1] == null)
                    throw new TableNotExistException();
                Jointure jointure = new Jointure(equation,joinTables[0],joinTables[1]);
                jointures.add(jointure);
            }else if (identifyConditionType(equation).equals(SELECTION)){
                String operand = equation.split(OPERATORS_PATTERN)[0];
                String value = equation.split(OPERATORS_PATTERN)[1];
                Table table = null;
                table = getTableFromOperand(operand);
                if (table == null)
                    throw new TableNotExistException();
                Selection selection = new Selection(equation,table,operand,value);
                selections.add(selection);
            }
        }
    }

    public Node createTreeSelection(int index) {
        if (index >= selections.size()) {
            return selections.get(index-1).getLeft();
        }

        Node root = new Selection(selections.get(index).condition,selections.get(index).getTable());
        root.left = createTreeSelection(index + 1);
        return root;
    }
    public Node createTreeJoin(Node leaf) throws TableNotExistException {
        if (jointures.size() > 1){
            Jointure nd1 = findJointureByTable(((Jointure) leaf).getTable1());
            Jointure nd2 = findJointureByTable(((Jointure) leaf).getTable2());
            if (nd1 == null && nd2 == null){
                Node temp = jointures.get(1);
                jointures.remove(temp);
                return new Cartesianproduct(leaf,temp);
            }
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
            if (Objects.equals(table.getName(), (jointures.get(i)).table1.getName())
                    || Objects.equals(table.getName(), (jointures.get(i)).table2.getName()))
                return jointures.get(i);
        }
        return null;
    }
     public Node createAndNodes(String andConditions) throws TableNotExistException {
        makesANDconditions(andConditions);
        Node origin = null;
        if (!selections.isEmpty() && jointures.isEmpty()){
            origin = createTreeSelection(0);

        }
        if (!selections.isEmpty()){
            origin = createTreeSelection(0);
            Node temp = origin;

            if (!jointures.isEmpty()){
                while (temp.left.left != null){
                    temp = temp.left;
                }

                temp.left = createTreeJoin(jointures.get(0));
            }
            jointures.clear();
            selections.clear();
            return origin;
        }
        origin = jointures.get(0);
        jointures.clear();
        return origin;
     }
    public void createTree() throws TableNotExistException, InvalidSQLException {
        if (whereClause == null)
            throw new InvalidSQLException();
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

}

