package kozmikoda.passwordspace;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * PostgreSQL connection class. Lets you open a connection to an existant or not existant database on the system.
 */
public class PSQLConnection{


    /**
     * database connection handler
     */
    private Connection database = null;

    /**
     * SQL command execution handler
      */
    private Statement commandExecutor = null;


    // ************************************
    static private String accountInfoTable = "user_credentials";
    static private String serviceInfoTable = "services";
    static private String serviceNameColumn = "service_name";
    static private String userIdentifierColumn = "user_identifier";
    static private String passwordColumn = "encrypted_password";
    // ************************************

    /**
     * Connects to the database named after the user of the OS
     * @throws SQLException throws when an SQL error occurs
     */
    PSQLConnection() throws SQLException {

        if (isWindows()) {
            connectToDatabase("postgres", "postgres", "postgres");
        } else {
            connectToDatabase("template1");
        }



    }


    /**
     * connects to an arbitrary named database
     * @param dbName a database name existing on the system
     * @throws SQLException throws when an SQL error occurs
     */
    PSQLConnection(String dbName) throws SQLException {

        if (isWindows()) {
            connectToDatabase(dbName, "postgres", "postgres");
        } else {
            connectToDatabase(dbName);
        }

    }

    /**
     * connects to the database dbName
     */
    public void connectToDatabase(String dbName) throws SQLException{

        if (isWindows()) {
            connectToDatabase(dbName, "postgres", "postgres");
            return;
        }

        if (database != null) {
            commandExecutor.close();
            database.close();

        }

        database = DriverManager.getConnection("jdbc:postgresql:" + dbName);
        commandExecutor = database.createStatement();

    }

    /**
     * Connects to dbName by using username and password
     * @param dbName existing database name
     * @param username user of the dbName
     * @param password password of the username user
     * @throws SQLException throws when an SQL error occurs
     */
    public void connectToDatabase(String dbName, String username, String password) throws SQLException{
        if (database != null) {
            commandExecutor.close();
            database.close();

        }

        database = DriverManager.getConnection("jdbc:postgresql:" + dbName, username, password);
        commandExecutor = database.createStatement();

    }


    /**
     * Creates database if it does not exist
     * @param dbName name of an existing database
     * @throws SQLException throws when an SQL error occurs
     */
    public void createDatabase(String dbName) throws SQLException{
        String command = String.format("CREATE DATABASE %S;", dbName);

        commandExecutor.execute(command);

    }

    /*


     */

    /**
     * Creates the requested table on the current database
     * @param tableName name of the table to be added
     * @param tableElement columns of the table, this variable is an ellipsis it can take infinite amount of objects>
     * @throws SQLException throws when an SQL error occurs
     */
    @SafeVarargs
    public final void createTable(String tableName, Pair<String, String>... tableElement) throws SQLException {
        StringBuilder command = new StringBuilder("CREATE TABLE " + tableName + " (");

        for (int a = 0; a < tableElement.length; a++) {

            Pair<String, String> temp = tableElement[a];

            command.append(temp.getKey());
            command.append(" ");
            command.append(temp.getValue());
            if (a != tableElement.length -1) {
                command.append(",");
            }

        }

        command.append(");");

        commandExecutor.execute(command.toString());

    }

    /**
     * Creates a table that stores the user credentials in the database
     * @param userName user which table is created for
     * @throws SQLException throws when an SQL error occurs
     */
    public void createUserTable(String userName) throws SQLException{
        createTable(userName,
                new Pair<>(PSQLConnection.getServiceNameColumn(), "varchar(64)"),
                new Pair<>(PSQLConnection.getUserIdentifierColumn(), "varchar(64)"),
                new Pair<>(PSQLConnection.getPasswordColumn(), "varchar(200)")
                );
    }

    /**
     * Inserts data into the table
     * @param tableName table to import the data in
     * @param values infinite amount values that is going into the table
     * @throws SQLException throws when an SQL error occurs
     */
    public final void insertIntoTable(String tableName, Object ... values) throws SQLException{

        StringBuilder command = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

        for (int i = 0; i < values.length; i++) {
            command.append("\'" + values[i].toString() + "\'" );
            if (i != values.length - 1) {
                command.append(", ");
            }
        }

        command.append(");");

        commandExecutor.execute(command.toString());

    }

    /**
     * Removes the requested data from the table
     * @param tableName table to delete the data from
     * @param columnName where the data exists in the column
     * @param toBeRemoved which data to remove
     * @throws SQLException throws when an SQL error occurs
     */
    public final void removeFromTable(String tableName, String columnName, String toBeRemoved) throws SQLException{

        String removeCommand = "DELETE FROM " + tableName + " WHERE " + columnName + " = " + "'" + toBeRemoved + "'" + ";";

        commandExecutor.execute(removeCommand);

    }



    /**
     *  Updates the requested column
     * @param targetUser which user to update the data on
     * @param newPassword new password
     * @throws SQLException throws when an SQL error occurs
     */
    public final void updateColumn(String targetUser, String newPassword) throws SQLException{
        String updateCommand = "UPDATE " + accountInfoTable + " SET password_hash"  +
                " = '" + HashedPassword.calculateDigest(newPassword, "sha-256") + "' WHERE user_name = '" + targetUser + "';";

        commandExecutor.execute(updateCommand);
    }


    public static String getServiceInfoTable() {
        return serviceInfoTable;
    }

    public static void setServiceInfoTable(String serviceInfoTable) {
        PSQLConnection.serviceInfoTable = serviceInfoTable;
    }

    public static String getServiceNameColumn() {
        return serviceNameColumn;
    }

    public static void setServiceNameColumn(String serviceNameColumn) {
        PSQLConnection.serviceNameColumn = serviceNameColumn;
    }

    public static String getUserIdentifierColumn() {
        return userIdentifierColumn;
    }

    public static void setUserIdentifierColumn(String userIdentifierColumn) {
        PSQLConnection.userIdentifierColumn = userIdentifierColumn;
    }

    public static String getPasswordColumn() {
        return passwordColumn;
    }

    public static void setPasswordColumn(String passwordColumn) {
        PSQLConnection.passwordColumn = passwordColumn;
    }

    public Statement getCommandExecutor() {
        return commandExecutor;
    }

    public static String getAccountInfoTable() {
        return accountInfoTable;
    }

    public static void setAccountInfoTable(String accountInfoTable) {
        PSQLConnection.accountInfoTable = accountInfoTable;
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
