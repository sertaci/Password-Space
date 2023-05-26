package kozmikoda.passwordspace;

/**
 * Is thrown if given algorithm is not supported this exception is thrown
 */
public class AlgorithmNotSupportedException extends Exception{

    AlgorithmNotSupportedException(String msg) {
        super(msg);
    }

}
