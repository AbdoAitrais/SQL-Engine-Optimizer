package BusinessObject;

public class Selection extends Node{
    String condition;

    public Selection(String condition) {
        this.condition = condition;
    }
    public Selection(String condition, Node table) {
        super(table);
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "σ" + " " + condition;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
