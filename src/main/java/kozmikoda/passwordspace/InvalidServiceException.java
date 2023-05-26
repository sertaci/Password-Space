package kozmikoda.passwordspace;

/**
 * If requested service is invalid this exception is thrown
 */
public class InvalidServiceException extends RuntimeException{
    InvalidServiceException(String service) {
        super(service + " does not exist!");
    }
}
