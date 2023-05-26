package kozmikoda.passwordspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class to validate user accounts
 */
public class UserValidator {

    /**
     * Validates a user account according to given information
     * @param dbConnection where to fetch the user
     * @param username which user to fetch
     * @param password prompted user
     * @throws SQLException is thrown if user doesn't exist in the database
     * @throws IncorrectPasswordException is thrown if password of the user is not correct
     */
    public static void validateUser(PSQLConnection dbConnection, String username, String password) throws SQLException, IncorrectPasswordException{

        dbConnection.connectToDatabase(username);

        Statement s = dbConnection.getCommandExecutor();

        ResultSet rs = s.executeQuery("SELECT password_hash FROM user_credentials WHERE user_name = '" + username + "';");

        rs.next();

        String promptPassword = HashedPassword.calculateDigest(password, "sha-256");
        String hashedPassword = rs.getString("password_hash");

        if (!promptPassword.equals(hashedPassword)) {
            throw new IncorrectPasswordException();
        }

    }


}