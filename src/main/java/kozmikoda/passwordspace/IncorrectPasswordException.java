package kozmikoda.passwordspace;

/**
 * If prompted password is incorrect this exception is thrown
 */
public class IncorrectPasswordException extends RuntimeException{
    IncorrectPasswordException() {
        super("Password is incorrect.");
    }
}
