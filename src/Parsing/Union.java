package Parsing;

public class Union extends Node{
    @Override
    public String toString() {
        return "U";
    }

    @Override
    public double estimate() {
        return 0;
    }
}
