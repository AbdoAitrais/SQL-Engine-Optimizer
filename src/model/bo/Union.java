package model.bo;

public class Union extends Node{
    public Union(Node left, Node right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "U";
    }

    @Override
    public double NbrLignes() {
        return 0;
    }

    @Override
    public double cost() {
        return 0.0;
    }
}
