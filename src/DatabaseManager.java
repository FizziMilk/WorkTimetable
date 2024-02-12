//This class handles the connection to the database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:sqlite:identifier.sqlite";

    // Load the SQLite JDBC driver when the class is initialized
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load SQLite JDBC driver");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }
}