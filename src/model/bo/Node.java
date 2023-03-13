package model.bo;

public abstract class Node {
    Node left;
    Node right;
    public Node(){}
    public Node(Node left) {
        this.left = left;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public abstract double estimate();

}
