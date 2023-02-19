package Parsing;

public class Selection extends Node{
    String condition;

    @Override
    public String toString() {
        return "Sigma" + " " + condition;
    }

    @Override
    public double estimate() {
        return 0;
    }
}
