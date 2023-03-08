package exceptions;

public class TableNotExistException extends Exception{
    public TableNotExistException() {
        super("A table that is mentioned in the query doesn't exist");
    }
}
