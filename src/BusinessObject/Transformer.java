package BusinessObject;

import java.util.ArrayList;
import java.util.List;

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
        return nd.toString().charAt(0) == '∞';
    }
    public void createEquivalence(){
        trees.add(eclatement(JCUse(clone(mainRoot))));
        trees.add(eclatement(clone(mainRoot)));
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


    public Node JCUse(Node leaf)
    {
        if (leaf != null){
            if (isJoin(leaf)){
                leaf = JC(leaf);
            }
            JCUse(leaf.left);
        }
        return leaf;
    }
    public Node JC(Node nd)
    {
        Node temp = nd.left;
        nd.left = nd.right;
        nd.right = temp;
        return nd;
    }
}
