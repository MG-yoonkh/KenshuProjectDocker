package mg.recipe.user;

public class EmailAlreadyExistsException extends RuntimeException  {
    public EmailAlreadyExistsException(String message){
        super(message);
    }

}
