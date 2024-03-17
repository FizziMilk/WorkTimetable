import java.sql.SQLException;
import java.util.Scanner;

public class TestDatabaseDriver {
    public static void main(String args[]) throws SQLException {
        DatabaseManager databaseManager = new DatabaseManager();
        DAO dao = new DAO(databaseManager);
        UserInterface UI = new UserInterface();

        try {
            UI.menu();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

