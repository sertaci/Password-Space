package kozmikoda.passwordspace;

/**
 * If password digests are not matching this exception is thrown
 */
public class DigestsNotMatchException extends Exception{

    DigestsNotMatchException(String msg) {
        super(msg);
    }

}
