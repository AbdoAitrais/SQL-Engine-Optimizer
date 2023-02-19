package Parsing;

public class Jointure extends Node{
    String condition;

    public Jointure(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Join" + " " + condition;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
