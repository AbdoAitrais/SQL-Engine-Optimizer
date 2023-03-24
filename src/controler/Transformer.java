package controler;

import model.bo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Transformer {
    public List<LogicalTree> logicalTrees;
    public Node mainRoot;
    public int nodesProcessed;
    private final String[] joinAlgos = {"BIB","BII","JTF","JH","PJ"};
    private final String[] selectAlgos = {"SB","SE","SNUK"};
    public Node clone(Node leaf) {
        Node newNode = null;
        if (isJoin(leaf)){
            newNode = new Jointure(((Jointure) leaf).getCondition(),new Table(((Jointure) leaf).getTable1()),new Table(((Jointure) leaf).getTable2()));

        }
        else if (isSelect(leaf)){
            newNode = new Selection(((Selection) leaf).getCondition(),((Selection) leaf).getTable());
        }
        else if (isProject(leaf)){
            newNode = new Projection(((Projection) leaf).getLeft(),((Projection) leaf).getColumns());
        } else if (isCartesianProduct(leaf)) {
            newNode = new Cartesianproduct(leaf.getLeft(),leaf.getRight());
        }else {
            newNode = new Relation(((Relation) leaf).getTable());
        }
        if (leaf.getLeft() != null) {
            newNode.setLeft( clone(leaf.getLeft()));
        }
        if (leaf.getRight() != null) {
            newNode.setRight( clone(leaf.getRight()));
        }
        return newNode;
    }
    public Transformer(Node root){
        this.logicalTrees = new ArrayList<>();
        this.mainRoot = root;
        this.nodesProcessed = 0;
    }
    private boolean isProject(Node nd){
        return nd.toString().charAt(0) == 'π';
    }
    private boolean isSelect(Node nd){
        return nd.toString().charAt(0) == 'σ';
    }
    private boolean isJoin(Node nd){
        if (nd != null)
            return nd.toString().charAt(0) == '⋈';
        return false;
    }
    private boolean isCartesianProduct(Node nd){
        return nd.toString().charAt(0) == 'X';
    }

    /****************------- Generating Variants -------****************/
    private void addLogicalTree(Node tree){
        if (tree == null)
            return;
        logicalTrees.add(new LogicalTree(tree));
    }

    public void createVaraiantsTree(Node leaf){
        Node arbre;            
        arbre = ESUse(clone(leaf));
        if(!exists(logicalTrees, arbre)) { System.out.println("IN ESU"); addLogicalTree(arbre); }
        arbre = SCUse(clone(leaf));
        if(!exists(logicalTrees, arbre)) { System.out.println("IN SCUse"); addLogicalTree(arbre); }
        arbre = JAUse(clone(leaf));
        if(!exists(logicalTrees, arbre)) { System.out.println("IN JAUse"); addLogicalTree(arbre); }
        arbre = JCUse(clone(leaf));
        if(!exists(logicalTrees, arbre)) { System.out.println("IN CPJUse"); addLogicalTree(arbre); }
        arbre = CJSUse(clone(leaf));
        if(!exists(logicalTrees, arbre)) { System.out.println("IN CJSUse"); addLogicalTree(arbre); }
        
        System.out.println("I am called");
         
    }

    public void generateVariantTrees(Node originalTree){
        createVaraiantsTree(originalTree);              
        // logicalTrees.add(originalTree);
        for (int index = 0; index < logicalTrees.size(); index++) {            
            System.out.println("size ++++++"+logicalTrees.size()+"  iteration ====> "+index);
            createVaraiantsTree(logicalTrees.get(index).getLogicalTree());
        }
    }

    public Node ESUse(Node leaf)
    {
        if (leaf != null){
            if (isSelect(leaf) && isSelect(leaf.getLeft())){
                leaf = ES(leaf);
                ESUse(leaf);
            }
            ESUse(leaf.getLeft());
            ESUse(leaf.getRight());
        }
        return leaf;
    }
    public Node ES(Node leaf){
        if (isSelect(leaf) && isSelect(leaf.getLeft())){
            Selection temp = (Selection) leaf;
            temp.setCondition(temp.getCondition() + " and " + ((Selection) temp.getLeft()).getCondition());
            temp.setLeft( temp.getLeft().getLeft());
            return temp;
        }
        return leaf;
    }

    public Node JC(Node nd)
    {
        if (isJoin(nd))
        {
            Node temp = nd.getLeft();
            nd.setLeft(nd.getRight());
            nd.setRight( temp);
        }
        return nd;
    }
    public Node JCUse(Node leaf)
    {
        if (leaf != null){
            if (isJoin(leaf)){
                leaf = JC(leaf);
            }
            else {
                JCUse(leaf.getLeft());
                JCUse(leaf.getRight());
            }
        }
        return leaf;
    }


    public Node SCUse(Node leaf)// does not work very well miss something I don't fucking know
    {
        if (leaf != null && leaf.getLeft() != null) {
            leaf = SC(leaf);
            SCUse(leaf.getLeft());
            SCUse(leaf.getRight());
        }
        return leaf;
    }
    public Node SC(Node nd)
    {
        if (isSelect(nd.getLeft()) && isSelect(nd.getLeft().getLeft())){
            Node temp = nd.getLeft();
            nd.setLeft(temp.getLeft());
            temp.setLeft( nd.getLeft().getLeft());
            nd.getLeft().setLeft(temp);
        }
        return nd;
    }

    public Node JA(Node nd) // T1 join (T2 join T3) ==>  (T1 join T2) join T3
    {
        if ((isJoin(nd) && isJoin(nd.getRight())) || (isJoin(nd) && isJoin(nd.getLeft()))){
            Node rightJoin  = nd.getRight(); // save the right join (T2 join T3)
            nd.setRight( rightJoin.getRight()); // mainJoin took the T3 as right child
            // form the subJoin  now
            rightJoin.setRight(rightJoin.getLeft());//subJoin took the T2 as right child
            rightJoin.setLeft( nd.getLeft());// //subJoin took the T1 as left child
            nd.setLeft(rightJoin);// the mainJoin took the subJoin as left child
        }
        return nd;
    }
    public Node JAUse(Node leaf)
    {
        if (leaf != null){
            if (isJoin(leaf) && isJoin(leaf.getRight())) {
                leaf = JA(leaf);
            }else{
                JAUse(leaf.getLeft());
                JAUse(leaf.getRight());
            }

        }
        return leaf;
    }


    public Node CJSUse(Node leaf)
    {
        if (leaf != null && leaf.getLeft() != null) {
            leaf = CSJ(leaf);
            CJSUse(leaf.getLeft());
            CJSUse(leaf.getRight());
        }

        return leaf;
    }
    public Node CSJ(Node leaf) // e (T1 ⋈ T2) = e (T1) ⋈ T2
    {
        Node select = leaf.getLeft();
        if (isSelect(leaf.getLeft()) && isJoin(leaf.getLeft().getLeft())){
            Node join = select.getLeft();
            if (containsTable(join.getRight(),((Selection) select).getTable())) {
                select.setLeft(join.getRight());
                join.setRight( select);
            }else {
                select.setLeft(join.getLeft());
                join.setLeft( select);
            }

            leaf.setLeft( join);
        }
        return leaf;
    }

    private boolean containsTable(Node node,Table table){
        if (isJoin(node))
            return ((Jointure) node).getTable1().getName().equals(table.getName()) || ((Jointure) node).getTable2().getName().equals(table.getName());
        if (isSelect(node))
            return ((Selection) node).getTable().getName().equals(table.getName());
        return node.toString().equals(table.getName());
    }

    public  boolean is_same(Node node1, Node node2) {
        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();

        // Vérifier si les deux arbres sont vides
        if (node1 == null && node2 == null) {
            return true;
        }
        // Si l'un des arbres est vide, alors les arbres sont différents
        if (node1 == null || node2 == null) {
            return false;
        }

        // Empiler les racines des deux arbres sur les piles respectives
        stack1.push(node1);
        stack2.push(node2);

        // Parcourir les deux arbres de manière itérative en utilisant des piles
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            // Dépiler les nœuds courants
            Node curr1 = stack1.pop();
            Node curr2 = stack2.pop();
               if(!curr1.toString().equals(curr2.toString()))
               {
                    System.out.println("3) not same"+(curr1.toString()));
                    System.out.println("3) not same"+(curr2.toString()));
                    return false;
                }

            // Empiler les nœuds enfants gauche et droit des deux arbres sur les piles respectives
            if (curr1.getLeft() != null && curr2.getLeft() != null) {
                stack1.push(curr1.getLeft());
                stack2.push(curr2.getLeft());
            } else if (curr1.getLeft() != null || curr2.getLeft() != null) {
                return false;
            }
            if (curr1.getRight() != null && curr2.getRight() != null) {
                stack1.push(curr1.getRight());
                stack2.push(curr2.getRight());
            } else if (curr1.getRight() != null || curr2.getRight() != null) {
                return false;
            }
        }

        // Vérifier si les deux piles sont vides
        return stack1.isEmpty() && stack2.isEmpty();
    }

    public  boolean exists(List<LogicalTree>vecteur, Node arbre)
    {
        for(LogicalTree n:vecteur)
        {
            if(is_same(n.getLogicalTree(),arbre))
                return true;
        }
        return false;
    }

    /******************** Physical Trees ********************/
    public void createAllPhysicalTrees(){
        for (LogicalTree node:logicalTrees) {
            createPhysicalTreesForOneTree(node);
        }
    }
    public void createPhysicalTreesForOneTree(LogicalTree node){
//        Hashtable<Node,Double> physicalTrees = new Hashtable<>();
        ArrayList<Node> physicalTrees = new ArrayList<>();
        for (String joinAlgo:joinAlgos) {
            for (String selectAlgo:selectAlgos) {
                physicalTrees.add(createPhysicalTree(clone(node.getLogicalTree()),joinAlgo,selectAlgo));
            }
        }
        node.setPhysicalTrees(physicalTrees);
    }

    private Node createPhysicalTree(Node leaf, String joinAlgo, String selectAlgo) {
        if (leaf != null){
            if (isSelect(leaf))
                ((Selection) leaf).setAlgorithm(selectAlgo);
            else if (isJoin(leaf)) {
                ((Jointure) leaf).setAlgorithm(joinAlgo);
            }
            createPhysicalTree(leaf.getLeft(),joinAlgo,selectAlgo);
            createPhysicalTree(leaf.getRight(),joinAlgo,selectAlgo);
        }
        return leaf;
    }
}