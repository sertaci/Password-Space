package kozmikoda.passwordspace;

import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Container class to store bunch of service
 */
public class ServiceMap {

    // key of the HashMap ==> Service Name
    // 1.st value in the pair ==> User Identifier
    // 2.nd value in the pair ==> Password
    private HashMap<String, Pair<String, String>> services = new HashMap<>();


    ServiceMap() {}

    ServiceMap(PSQLConnection dbConnection, String userDatabase) throws SQLException {
        dbConnection.connectToDatabase(userDatabase);


        Statement s = dbConnection.getCommandExecutor();

        ResultSet rs = s.executeQuery("SELECT * FROM services ;");

        while(rs.next()) {
            services.put(
                    rs.getString(
                            PSQLConnection.getServiceNameColumn()),
                    new Pair<>(rs.getString(PSQLConnection.getUserIdentifierColumn()),
                            rs.getString(PSQLConnection.getPasswordColumn()) ) );
        }


    }

    public String[] getService(String serviceName) throws InvalidServiceException{

        // extract requested service from hashmap
        Pair<String, String> promptedService = services.get(serviceName);

        // if requested service doesn't exist
        if (promptedService == null) {
            throw new InvalidServiceException(serviceName);
        }

        // put service info into String array
        // [0] ==> userIdentifier
        // [1] ==> password
        return new String[]{promptedService.getKey(), promptedService.getValue()};
    }

    public void addService(String service, String serviceUser, String servicePassword) {
        services.put(service, new Pair<>(serviceUser, servicePassword));
    }

    public void removeService(String service) {
        services.remove(service);
    }

    public int getSize() {
        return services.size();
    }

    public HashMap<String, Pair<String, String>> getHashMap() {
        return  this.services;
    }

}
