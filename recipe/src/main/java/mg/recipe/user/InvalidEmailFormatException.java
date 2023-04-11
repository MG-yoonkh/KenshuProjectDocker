package mg.recipe.user;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(String message){
        super(message);
    }
}
