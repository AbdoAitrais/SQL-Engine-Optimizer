package BusinessObject;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

public class Transformer {
    public List<Node> trees;
    public Node mainRoot;
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
        this.trees = new ArrayList<>();
        this.mainRoot = root;
    }
    private boolean isProject(Node nd){
        return nd.toString().charAt(0) == 'π';
    }
    private boolean isSelect(Node nd){
        return nd.toString().charAt(0) == 'σ';
    }
    private boolean isJoin(Node nd){
        return nd.toString().charAt(0) == '⋈';
    }


    public boolean sameTree(Node t1,Node t2)
    {
        if (!t1.equals(t2))      
            return false;
        sameTree(t1.left,t2.left);
        sameTree(t1.right,t2.right);
        return true;
    }

    public boolean existInListOfTrees(Node nd)
    {
        for (Node node : trees) {
            if (sameTree(node, nd)) {
                return true;
            }   
        }
        return false;
    }
    public void createEquivalence(){
        trees.add(mainRoot);
        //trees.add(eclatement(SCUse(mainRoot)));
        trees.add(eclatement(clone(mainRoot)));               
        trees.add(JCUse(clone(mainRoot)));
        trees.add(CPJUse(clone(mainRoot)));
        trees.add(SCUse(clone(mainRoot)));
        trees.add(CPSUse(clone(mainRoot)));
        trees.add(CJSUse(clone(mainRoot)));
        

        System.out.println("the size is " +trees.size());
    }


    public Node eclatement(Node leaf)
    {
        if (leaf != null){
            if (isSelect(leaf) && isSelect(leaf.left)){
                leaf = eclatementSelection(leaf);
                eclatement(leaf);
            }
            eclatement(leaf.left);
        }
        return leaf;
    }
    public Node eclatementSelection(Node leaf){
       Selection temp = (Selection) leaf;
       temp.condition += " and " + ((Selection) temp.left).condition;
       temp.left = temp.left.left;
       return temp;
    }

    public Node JC(Node nd)
    {
        Node temp = nd.left;
        nd.left = nd.right;
        nd.right = temp;
        return nd;
    }
    public Node JCUse(Node leaf)
    {
        if (leaf != null){
            if (isJoin(leaf)){
                leaf = JC(leaf);
            }
            else  JCUse(leaf.left);
        }
        return leaf;
    }
    

    public Node SCUse(Node leaf)// does not work very well miss something I don't fucking know
    {
        if (leaf != null && leaf.left != null) {
            
            if (isSelect(leaf) && isSelect(leaf.left)){
                leaf = SC(leaf); 
                return leaf;           
            }
            else SCUse(leaf.left);
        }
        return leaf;
    } 
    public Node SC(Node nd)
    {
        Node temp = nd.left;
        nd.left = temp.left;
        temp.left = nd;
        return temp;
    }

    public Node JA(Node nd) // T1 join (T2 join T3) ==>  (T1 join T2) join T3
    {
        
        Node rightJoin  = nd.right; // save the right join (T2 join T3)
        nd.right = rightJoin.right; // mainJoin took the T3 as right child
        // form the subJoin  now
        rightJoin.right = rightJoin.left;//subJoin took the T2 as right child 
        rightJoin.left = nd.left;// //subJoin took the T1 as left child 
        nd.left = rightJoin;// the mainJoin took the subJoin as left child 
        return nd;// return the mainJoin
    }
    public Node JAUse(Node leaf)
    {
        if (isJoin(leaf) && isJoin(leaf.right)) {
            leaf = JA(leaf);
        }else  
            return JAUse(leaf.left);
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
        }
        }
        
        return leaf;
    }
    public Node CSJ(Node nd) // e (T1 ⋈ T2) = e (T1) ⋈ T2
    {  
       //NB :  the nd is always a selection 
        // System.out.println("begin the chinage"); 
        Node join = nd.left;
        nd.left = join.left;
        join.left = nd;
        return join;
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
           else  CPJUse(leaf.left);
        }
        // System.out.println("that's fucking null");
        return leaf;
    }
    public Node CPJ(Projection nd)
    {
        
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


    public Node CPSUse(Node leaf)
    {
        if (leaf != null) {
            if (isProject(leaf) && isSelect(leaf.left)) {
                leaf = CPS(leaf);
            }
            else CPSUse(leaf.left);
        }
        return leaf;
    }

    public Node CPS(Node nd) // we can use the SC function it's the same thing
    {

        Node temp = nd.left;
        nd.left = temp.left;
        temp.left = nd;        
        return temp;
    }
}
