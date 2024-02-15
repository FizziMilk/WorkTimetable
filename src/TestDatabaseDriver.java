import java.util.Scanner;

public class TestDatabaseDriver {
    public static void main(String args[])
    {
        // this class and driver method used only to test if database methods are functioning
        // aka this is just for debugging
        DatabaseManager databaseManager = new DatabaseManager();
        DAO dao = new DAO(databaseManager);
        Timetable timetable = new Timetable();

        //dao.addCourseToDatabase(courseName, courseCode);
        dao.addModuleToDatabase("Analogue Electronics", "EEE_5_AEL_2324");
        //timetable.addTimeSlotClassroom();
    }
}
