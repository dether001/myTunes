package mytunes.gui.Model;


//for errors
public class ModelException extends Exception
{

    // creates the error message
    public ModelException(String message)
    {
        super(message);
    }

    // creates the error
    public ModelException(Exception ex)
    {
        super(ex.getMessage());
    }
    
    // returns error message
    @Override
    public String getMessage()
    {
        return super.getMessage(); 
    }
       
}
