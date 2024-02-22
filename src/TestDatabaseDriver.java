import java.util.Scanner;

public class TestDatabaseDriver {
    public static void main(String args[])
    {
        DatabaseManager databaseManager = new DatabaseManager();
        DAO dao = new DAO(databaseManager);
        UserInterface UI = new UserInterface();

        UI.menu();
    }
}

