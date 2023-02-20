package Parsing;

public abstract class Node {

    Node left;
    Node right;
    public Node(){}
    public Node(Node left) {
        this.left = left;
    }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public abstract double estimate();

}
