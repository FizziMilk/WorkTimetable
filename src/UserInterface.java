import java.sql.SQLException;
import java.util.*;
public class UserInterface {

    InputProcessing input = new InputProcessing();

    public void menu() throws SQLException {
        System.out.println("**********************************************" +
                "\nWelcome to the Timetable Generation System.\n" +
                "**********************************************\n");

        System.out.println("This program lets you add or remove timeslots and more features.");


        boolean loop = true;
        while (loop)
        {

            System.out.println("""
                    1. Create a new timeslot
                    2. View timeslots
                    3. Database - Add new Course
                    4. Database - Add new Module
                    5. Database - Add new Room
                    6. Database - Add new Lecturer
                    7. Database - Link a module to the course
                    8. Exit
                    """);
            int week = Integer.parseInt(input.processUserInput("Put your numeric input below:", true));
            switch(week)
            {
                case 1 -> input.addTimeslot();
                // case "2" ->
                case 3 -> input.addCourse();
                case 4 -> input.addModule();
                case 5 -> input.addRoom();
                case 6 -> input.addLecturer();
                case 7 -> input.addModuleToCourse();
                case 8 -> loop = false;
                default -> System.out.println("Enter a number --> (not 2 for now) ");
            }
        }
    }


}
