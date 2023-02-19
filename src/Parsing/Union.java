package Parsing;

public class Union extends Node{
    public Union(Node left, Node right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "U";
    }

    @Override
    public double estimate() {
        return 0;
    }
}
