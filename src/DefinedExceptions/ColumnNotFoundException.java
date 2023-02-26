package DefinedExceptions;

public class ColumnNotFoundException extends Exception{
    public ColumnNotFoundException() {
        super("Couldn't Find Column !");
    }
}
