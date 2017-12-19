package mytunes.dal;

// handles errors
// author @Dether
public class DAException extends Exception {


    //creates a new error
    public DAException(String message) {
        super(message);
    }

    //creates an error message
    @Override
    public String getMessage() {
        return super.getMessage(); //To change body of generated methods, choose Tools | Templates.
    }
}
