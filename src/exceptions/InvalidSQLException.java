package exceptions;

public class InvalidSQLException extends Exception{
    public InvalidSQLException() {
        super("Invalid SQL query");
    }
}
