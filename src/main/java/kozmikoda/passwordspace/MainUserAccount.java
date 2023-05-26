package kozmikoda.passwordspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*

    SMS API ile entegrasyon bu class üzerinden olacak
    sms yollama olayı için belki tekil bir method oluşturulabilir


 */


public class MainUserAccount {

    // username of the main account
    private final String userName;

    // same as username
    private final String userDatabase;

    // real life name of the user
    private String realName;

    private String eMail;

    private String phoneNumber;

    // HashMap for storing the service credentials
    private ServiceMap services;

    PSQLConnection dbConnection;

    Statement s;


    /**
     * call to connect to an existing user
     */
    MainUserAccount(PSQLConnection dbConnection, String userName) throws SQLException{
        this.userDatabase = userName;
        this.userName = userName;
        this.dbConnection = dbConnection;


        dbConnection.connectToDatabase(userDatabase);


        fetchRealName();
        fetchEMail();
        fetchPhoneNumber();
        fetchServices();

    }

    /**
     * call to create a new user and connect to it
     */

    MainUserAccount(PSQLConnection dbConnection, String userName, String password, String realName,
                    String eMail, String phoneNumber) throws SQLException
    {
        this.dbConnection = dbConnection;
        this.userName = userName;
        this.userDatabase = userName;
        this.realName = realName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        services = new ServiceMap();

        createNewUser(userName, password, realName, eMail, phoneNumber);
    }

    private void createNewUser(String userName, String password, String realName,
                               String eMail, String phoneNumber) throws SQLException
    {

        s = dbConnection.getCommandExecutor();

        String databaseCreatorStatement = String.format("CREATE DATABASE %s;", userName);
        String user_credentialsCreatorStatement = "CREATE TABLE user_credentials (" +
                "user_name varchar(32)," +
                "password_hash varchar(64)," +
                "real_name varchar(64)," +
                "e_mail varchar(64)," +
                "phone_number char(10) );";

        String servicesCreatorStatement = "CREATE TABLE services (" +
                "service_name varchar(64)," +
                "user_identifier varchar(64)," +
                "encrypted_password varchar(64));";


        // create and change into the new user database
        s.execute(databaseCreatorStatement);

        dbConnection.connectToDatabase(userName);



        // get the new command executor
        s = dbConnection.getCommandExecutor();

        // create the user_credentials table
        // user_credentials stores the main user account credentials
        s.execute(user_credentialsCreatorStatement);

        // add main account credentials into the user_credentials table
        dbConnection.insertIntoTable(PSQLConnection.getAccountInfoTable(), userName,
                HashedPassword.calculateDigest(password, "sha-256"),
                realName,
                eMail,
                phoneNumber);


        // create the services table and
        // stores the necessary service credentials
        s.execute(servicesCreatorStatement);


    }

    public void addNewService(String service,
                              String serviceUser,
                              String servicePassword) throws SQLException
    {

        dbConnection.insertIntoTable("services", service, serviceUser, servicePassword);

        services.addService(service, serviceUser, servicePassword);

    }

    public void removeService(String service) throws SQLException{
        dbConnection.removeFromTable("services", "service_name", service);
        services.removeService(service);
    }

    public void updatePassword(String password) throws SQLException {
        dbConnection.updateColumn(userName, password);
    }

    private void fetchRealName() throws SQLException{

        s = dbConnection.getCommandExecutor();

        ResultSet rs = s.executeQuery("SELECT real_name FROM user_credentials WHERE user_name = '"+ userName +"';");
        rs.next();
        realName = rs.getString("real_name");

    }
    private void fetchEMail() throws SQLException{

        s = dbConnection.getCommandExecutor();

        ResultSet rs = s.executeQuery("SELECT e_mail FROM user_credentials WHERE user_name = '"+ userName +"';");

        rs.next();
        eMail = rs.getString("e_mail");

    }
    private void fetchPhoneNumber() throws SQLException{

        s = dbConnection.getCommandExecutor();

        ResultSet rs = s.executeQuery("SELECT phone_number FROM user_credentials WHERE user_name = '"+ userName +"';");

        rs.next();

        phoneNumber = rs.getString("phone_number");

    }

    private void fetchServices() throws SQLException{

        services = new ServiceMap(dbConnection, userDatabase);

    }

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
        return realName;
    }

    public ServiceMap getServices() {
        return services;
    }

    public String getEMail() {
        return eMail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserDatabase() {
        return userDatabase;
    }


}
