package model.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Transformer {
    public List<Node> logicalTrees;
    public List<Node> physicalTrees;
    public Node mainRoot;
    public int nodesProcessed;
    private String[] joinAlgos = {"BIB","BII","JTF","JH","PJ"};
    private String[] selectAlgos = {"SB","SE","SNUK"};
    private boolean reachedEnd;
    private boolean es;
    private boolean sc;
    private boolean jc;
    private boolean ja;
    private boolean cpj;
    private boolean cps;
    private boolean cjs;
    public Node clone(Node leaf) {
        Node newNode = null;
        if (isJoin(leaf)){
            newNode = new Jointure(((Jointure) leaf).condition,new Table(((Jointure) leaf).table1),new Table(((Jointure) leaf).table2));

        }
        else if (isSelect(leaf)){
            newNode = new Selection(((Selection) leaf).condition,((Selection) leaf).table);
        }
        else if (isProject(leaf)){
            newNode = new Projection(((Projection) leaf).left,((Projection) leaf).columns);
        } else if (isCartesianProduct(leaf)) {
            newNode = new Cartesianproduct(leaf.left,leaf.right);
        }else {
            newNode = new Relation(((Relation) leaf).table);
        }
        if (leaf.left != null) {
            newNode.left = clone(leaf.left);
        }
        if (leaf.right != null) {
            newNode.right = clone(leaf.right);
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

    public boolean sameTree(Node t1,Node t2)
    {
        if (!t1.equals(t2))
            return false;
        sameTree(t1.left,t2.left);
        sameTree(t1.right,t2.right);
        return true;
    }

    public boolean existInListOfTrees(ArrayList<Node> trees,Node nd)
    {
        for (Node node : trees) {
            if (sameTree(node, nd)) {
                return true;
            }
        }
        return false;
    }
    /****************------- Generating Variants -------****************/
    private void addVariantTree(Node tree){
        if (tree == null)
            return;
        logicalTrees.add(tree);
    }
    public Node createVaraiantTree(Node leaf,int level){
        if (leaf != null){
            if (level == nodesProcessed){
                if (isSelect(leaf) && isSelect(leaf.left) && !es){
                    System.out.println("es on : "+leaf);
                    leaf = ES(leaf);
                    es = true;
                    return leaf;
                }
                if (isSelect(leaf) && isSelect(leaf.left) && !sc){
                    System.out.println("sc on : " + leaf);
                    leaf = SC(leaf);
                    sc = true;
                    return leaf;
                }
                if (isJoin(leaf) && isJoin(leaf.right) && !ja) {
                    leaf = JA(leaf);
                    ja = true;
                    return leaf;
                }
                if (isJoin(leaf) && !jc){
                    leaf = JC(leaf);
                    jc = true;
                    return leaf;
                }
                if (isSelect(leaf) && isJoin(leaf.left) && !cjs)
                {
                    leaf = CSJ(leaf);
                    cjs = true;
                    return leaf;
                }
                if (isProject(leaf) && isJoin(leaf.left) && !cpj)
                {
                    Projection pro = (Projection)leaf;
                    leaf = CPJ(pro);
                    cpj = true;
                    return leaf;
                }
                nodesProcessed++;
            }
            createVaraiantTree(leaf.left,level+1);
            createVaraiantTree(leaf.right,level+1);
        }
        reachedEnd = true;
        return leaf;
    }
    public void createVaraiantsTree(Node leaf){
        Node arbre;            
        arbre = ESUse(clone(leaf));
        if(!existe(logicalTrees, arbre)) { System.out.println("IN ESU"); logicalTrees.add(arbre); }
        arbre = SCUse(clone(leaf));
        if(!existe(logicalTrees, arbre)) { System.out.println("IN SCUse"); logicalTrees.add(arbre); }
        arbre = JAUse(clone(leaf));
        if(!existe(logicalTrees, arbre)) { System.out.println("IN JAUse"); logicalTrees.add(arbre); }
        arbre = JCUse(clone(leaf));
        // System.out.println("table left "+arbre.getLeft().getLeft().toString());
        // System.out.println("table right "+arbre.getLeft().getRight().toString());
        // if(!existe(logicalTrees, arbre)) { System.out.println("IN JCUse"); logicalTrees.add(arbre); }
        // arbre = CPJUse(clone(leaf));
        if(!existe(logicalTrees, arbre)) { System.out.println("IN CPJUse"); logicalTrees.add(arbre); }
        arbre = CJSUse(clone(leaf));
        if(!existe(logicalTrees, arbre)) { System.out.println("IN CJSUse"); logicalTrees.add(arbre); }
        
        System.out.println("I am called");
         
    }


    


    public void generateVariantTrees(Node originalTree){
        createVaraiantsTree(originalTree);              
        // logicalTrees.add(originalTree);
        for (int index = 0; index < logicalTrees.size(); index++) {            
            System.out.println("size ++++++"+logicalTrees.size()+"  iteration ====> "+index);
            createVaraiantsTree(logicalTrees.get(index));
        }
        
        
    }

    // public void generateVariantTrees(){
    //     logicalTrees.add(clone(mainRoot));
    //     createVaraiantsTree(mainRoot);
    // }

    private void createVariantsForTree(Node node) {
        nodesProcessed = 0;
        reachedEnd = false;
        es = false;
        sc = false;
        jc = false;
        ja = false;
        cpj = false;
        cps = false;
        cjs = false;
        while (!reachedEnd){
            logicalTrees.add(createVaraiantTree(clone(node),0));
        }
    }


    public Node ESUse(Node leaf)
    {
        if (leaf != null){
            if (isSelect(leaf) && isSelect(leaf.left)){
                leaf = ES(leaf);
                ESUse(leaf);
            }
            ESUse(leaf.left);
            ESUse(leaf.right);
        }
        return leaf;
    }
    public Node ES(Node leaf){
        if (isSelect(leaf) && isSelect(leaf.left)){
            Selection temp = (Selection) leaf;
            temp.condition += " and " + ((Selection) temp.left).condition;
            temp.left = temp.left.left;
            return temp;
        }
        return leaf;
    }

    public Node JC(Node nd)
    {
        if (isJoin(nd))
        {
            Node temp = nd.left;
            nd.left = nd.right;
            nd.right = temp;
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
                JCUse(leaf.left);
                JCUse(leaf.right);
            }
        }
        return leaf;
    }


    public Node SCUse(Node leaf)// does not work very well miss something I don't fucking know
    {
        if (leaf != null && leaf.left != null) {
            leaf = SC(leaf);
            SCUse(leaf.left);
            SCUse(leaf.right);
        }
        return leaf;
    }
    public Node SC(Node nd)
    {
        if (isSelect(nd.left) && isSelect(nd.left.left)){
            Node temp = nd.left;
            nd.left = temp.left;
            temp.left = nd.left.left;
            nd.left.left = temp;
        }
        return nd;
    }

    public Node JA(Node nd) // T1 join (T2 join T3) ==>  (T1 join T2) join T3
    {
        if ((isJoin(nd) && isJoin(nd.right)) || (isJoin(nd) && isJoin(nd.left))){
            Node rightJoin  = nd.right; // save the right join (T2 join T3)
            nd.right = rightJoin.right; // mainJoin took the T3 as right child
            // form the subJoin  now
            rightJoin.right = rightJoin.left;//subJoin took the T2 as right child
            rightJoin.left = nd.left;// //subJoin took the T1 as left child
            nd.left = rightJoin;// the mainJoin took the subJoin as left child
        }
        return nd;
    }
    public Node JAUse(Node leaf)
    {
        if (leaf != null){
            if (isJoin(leaf) && isJoin(leaf.right)) {
                leaf = JA(leaf);
            }else{
                JAUse(leaf.left);
                JAUse(leaf.right);
            }

        }
        return leaf;

        // return isJoin(leaf.right) ? JA(leaf) : leaf;
    }


    public Node CJSUse(Node leaf)
    {
        if (leaf != null && leaf.left != null) {
            leaf = CSJ(leaf);
            CJSUse(leaf.left);
            CJSUse(leaf.right);
        }

        return leaf;
    }
    public Node CSJ(Node leaf) // e (T1 ⋈ T2) = e (T1) ⋈ T2
    {
        Node select = leaf.left;
        if (isSelect(leaf.left) && isJoin(leaf.left.left)){
            Node join = select.left;
            if (containsTable(join.right,((Selection) select).getTable())) {
                select.left = join.right;
                join.right = select;
            }else {
                select.left = join.left;
                join.left = select;
            }

            leaf.left= join;
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

    public Node CPJUse(Node leaf)
    {
        if(leaf != null)
        {
            if (isProject(leaf) && isJoin(leaf.left))
            {
                // System.out.println("I found the case");
                Projection pro = (Projection)leaf;
                leaf = CPJ(pro);
            }
            else  {
                CPJUse(leaf.left);
                CPJUse(leaf.right);
            }
        }
        // System.out.println("that's fucking null");
        return leaf;
    }
    public Node CPJ(Projection nd)
    {
        if(isProject(nd) && isJoin(nd.left)){
            Node  joiNode = nd.left;
            System.out.println(joiNode.left);
            System.out.println(joiNode.right);
            Projection leftProj = new Projection(joiNode.left,null);
            Projection rightProj  = new Projection(joiNode.right,null);//rightProj.left = joiNode.right;
            System.out.println("I did the chainage");

            System.out.println(leftProj);
            System.out.println(rightProj);
            //filter the columns but the columns is not related to a table in the column class
            // for ( Column col: nd.columns) {
            //     Relation rel = (Relation)(joiNode.left);
            //     if(col.getTable().equals(rel.getTable()))
            //         leftProj.columns.add(col);
            //     else
            //         rightProj.columns.add(col);
            // }
            System.out.println("leftProj = " +leftProj);
            System.out.println("rightProj = " +rightProj);
            System.out.println("infos");
            joiNode.left  =leftProj  ;
            joiNode.right =rightProj ;
            return joiNode;
        }
        return nd;
    }


//    public Node CPSUse(Node leaf)
//    {
//        if (leaf != null) {
//            if (isProject(leaf) && isSelect(leaf.left)) {
//                leaf = CPS(leaf);
//            }
//            else CPSUse(leaf.left);
//        }
//        return leaf;
//    }
//
//    public Node CPS(Node nd) // we can use the SC function it's the same thing
//    {
//
//        Node temp = nd.left;
//        nd.left = temp.left;
//        temp.left = nd;
//        return temp;
//    }










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
        
        // Si les valeurs des nœuds courants sont différentes, alors les arbres sont différents
        // if(isJoin(curr1) && isJoin(curr2))
        // {
        //     // if (!curr1.getContenu().equals(curr2.get())) {
        //         if(!(((Jointure)(curr1)).toString().equals(((Jointure)(curr2)).toString())))
        //         {
        //             System.out.println("1) not same"+((Jointure)(curr1)).toString());
        //             return false;
        //         }
        //         if(!(((Jointure)(curr1)).getLeft().toString().equals(((Jointure)(curr2)).getLeft().toString())))
        //         {
        //             System.out.println("2) not same"+((Jointure)(curr1)).getLeft().toString());
        //             System.out.println("2) not same"+((Jointure)(curr2)).getLeft().toString());
        //             return false;
        //         }
        //         if(!(((Jointure)(curr1)).getRight().toString().equals(((Jointure)(curr2)).getRight().toString())))
        //         {
        //             System.out.println("3) not same"+((Jointure)(curr1)).getRight().toString());
        //             System.out.println("3) not same"+((Jointure)(curr2)).getRight().toString());
        //             return false;
        //         }
                
        // }
        // if(isSelect(curr1) &&   isSelect(curr2))
        // {
        //     if(!(((Selection)(curr1)).condition.equals(((Selection)(curr2)).condition)))
        //             return false;   
        // }

        // if(!(((Jointure)(curr1)).getLeft().toString().equals(((Jointure)(curr2)).getLeft().toString()))))
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

public  boolean existe(List<Node>vecteur, Node arbre)
{
	for(Node n:vecteur)
	{
		if(is_same(n,arbre))
			return true;
	}
	return false;
}
























    /******************** Physical Trees ********************/
    public void createAllPhysicalTrees(){
        physicalTrees = new ArrayList<>();
        for (Node node:logicalTrees) {
            createPhysicalTreesForOneTree(node);
        }
    }
    public void createPhysicalTreesForOneTree(Node node){
        for (String joinAlgo:joinAlgos) {
            for (String selectAlgo:selectAlgos) {
                physicalTrees.add(createPhysicalTree(clone(node),joinAlgo,selectAlgo));
            }
        }
    }

    private Node createPhysicalTree(Node leaf, String joinAlgo, String selectAlgo) {
        if (leaf != null){
            if (isSelect(leaf))
                ((Selection) leaf).setAlgorithm(selectAlgo);
            else if (isJoin(leaf)) {
                ((Jointure) leaf).setAlgorithm(joinAlgo);
            }
            createPhysicalTree(leaf.left,joinAlgo,selectAlgo);
            createPhysicalTree(leaf.right,joinAlgo,selectAlgo);
        }
        return leaf;
    }
}