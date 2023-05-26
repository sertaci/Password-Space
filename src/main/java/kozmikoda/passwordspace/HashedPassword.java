package kozmikoda.passwordspace;

import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Stores all functionality of the hashed password generation
 */
public class HashedPassword {

    public static final String[] supportedAlgorithms = {"sha-256", "md5", "sha-512"};

    public String getAlgorithm() {
        return algorithm;
    }

    public String getText() {
        return text;
    }

    // hashing algorithm used to create this password text
    private String algorithm;

    // hashed password text
    private String text;

    // hashes the plain password using sha-256 and stores the hash
    HashedPassword(String plainPasswordText) {

        this.text = calculateDigest(plainPasswordText, "sha-256");
        this.algorithm = "sha-256";

    }

    HashedPassword(String hashedPasswordText, String algorithm)
            throws AlgorithmNotSupportedException {

        if (isAlgorithmSupported(algorithm)) {
            this.algorithm = algorithm;
        }

        this.text = hashedPasswordText;
    }

    HashedPassword(String hashedPasswordText, String algorithm, String plainPasswordText)
            throws AlgorithmNotSupportedException, DigestsNotMatchException {

        if (isAlgorithmSupported(algorithm)) {
            String newDigest = calculateDigest(plainPasswordText, algorithm);

            if (newDigest.equals(hashedPasswordText)) {
                this.algorithm = algorithm;
                this.text = hashedPasswordText;
            } else {
                throw new DigestsNotMatchException("Provided hashedPasswordText does not match true hash!");
            }

        } else {
            throw new AlgorithmNotSupportedException("Provided algorithm is not supported!");
        }

    }

    // checks if algorithm is supported
    public static boolean isAlgorithmSupported(String algorithm)
            throws AlgorithmNotSupportedException {

        for (String supportedAlgorithm : supportedAlgorithms) {
            if (supportedAlgorithm.equals(algorithm)) {
                return true;
            }
        }

        throw new AlgorithmNotSupportedException("Provided hash algorithm is not supported for HashedPassword");
    }

    public static String calculateDigest(String msg, String algorithm) {
        try {
            MessageDigest digestGenerator = MessageDigest.getInstance(algorithm);

            byte[] passwordDigest = digestGenerator.digest(msg.getBytes());

            return HexFormat.of().formatHex(passwordDigest).toString();

        } catch (Exception e) {
            System.err.println("Something went very very wrong about HashedPassword constructor." +
                    "Possible problem at SHA-256 algorithm");

            return null;
        }
    }

}
