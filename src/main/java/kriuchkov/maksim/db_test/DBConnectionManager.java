package kriuchkov.maksim.db_test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnectionManager {

    private static final String PROPERTY_FILE_NAME = "db.properties";

    private static String url;
    private static String user;
    private static String password;

    private static Connection connection = null;

    private DBConnectionManager() {

    }

    public static Connection getConnection() throws Exception {
        if (connection == null) {
            readDBPropertiesFile();
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    private static void readDBPropertiesFile() throws Exception {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTY_FILE_NAME)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new IOException(String.format("Unable to read properties file '%s'", PROPERTY_FILE_NAME), e);
        }

        String driverClass = properties.getProperty("jdbc.driver");
        if (driverClass != null) {
            Class.forName(driverClass);
        }

        url = properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
    }
}
