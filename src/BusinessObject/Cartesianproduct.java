package BusinessObject;

public class Cartesianproduct extends Node{

    public Cartesianproduct(Node jointure1, Node jointure2) {
        super(jointure1, jointure2);
    }

    @Override
    public String toString() {
        return "X";
    }

    @Override
    public double estimate() {
        return 0;
    }
}
