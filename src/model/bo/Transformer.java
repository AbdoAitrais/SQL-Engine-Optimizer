package model.bo;

import java.util.ArrayList;
import java.util.List;

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
        logicalTrees.add(ESUse(clone(leaf)));
        logicalTrees.add(SCUse(clone(leaf)));
        logicalTrees.add(JAUse(clone(leaf)));
        logicalTrees.add(JCUse(clone(leaf)));
        logicalTrees.add(CPJUse(clone(leaf)));
    }
    public void generateVariantTrees(Node originalTree){
        createVaraiantsTree(originalTree);

        List<Node> temp = new ArrayList<>(logicalTrees);
        for (Node node:temp) {
            createVaraiantsTree(node);
        }
        logicalTrees.add(clone(originalTree));
    }

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
        // return ( isSelect(leaf) && isJoin(leaf.left) ) ? CSJ(leaf) : leaf;
        // if (leaf ==null )System.out.println("the node is null");
        if(leaf != null)
        {
            // System.out.println("I am not null");
            if (isSelect(leaf) && isJoin(leaf.left))
            {
                // System.out.println("make the change");
                leaf = CSJ(leaf);
            }

            else {
                CJSUse(leaf.left);
                CJSUse(leaf.right);
            }
        }

        return leaf;
    }
    public Node CSJ(Node nd) // e (T1 ⋈ T2) = e (T1) ⋈ T2
    {
        //NB :  the nd is always a selection
        // System.out.println("begin the chinage");
        if (isSelect(nd) && isJoin(nd.left)){
            Node join = nd.left;
            nd.left = join.left;
            join.left = nd;
            return join;
        }
        return nd;
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