import java.sql.*;
import java.util.*;
import javax.swing.*;

public class InputProcessing {
    //Performs a check by using isValidInput to see if the user input was empty, and allows user to enter q to return to the menu at any time
    //if requireNumeric is set to true, will check if the input is a number

    private final Scanner input = new Scanner(System.in);
    DatabaseManager databaseManager = new DatabaseManager();
    DAO dao = new DAO(databaseManager);

    public String processUserInput(String message, boolean requireNumeric) {
        try {
            // Get user input
            System.out.println(message);
            String userInput = input.nextLine();

            // Check if the user wants to quit
            if ("q".equalsIgnoreCase(userInput)) {
                System.out.println("Leaving to menu");


                // replace menu
            }

            // Validate user input
            if (isValidInput(userInput,requireNumeric)) {
                // Process or store the valid input
                return capitalizeFirstLetter(userInput.toLowerCase());
            } else {
                // Handle invalid input
                System.out.println("Invalid input. Please try again. Or enter q to quit.\n");
                return processUserInput(message,requireNumeric);
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            System.out.println("An error occurred. Please try again. Or enter q to quit.\n");
            return processUserInput(message,requireNumeric);
        }
    }

    private boolean isValidInput(String input, boolean requireNumeric) {
        // non-empty string
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Capitalize the first letter
        input = capitalizeFirstLetter(input);

        if (requireNumeric) {
            try {
                Double.parseDouble(input);
                return true; // input is a valid double
            } catch (NumberFormatException e) {
                return false; // input is not a valid double
            }
        }

        return true; // Input is a non-empty string
    }
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    // add new course intermediary
    public void addCourse()
    {
        String courseName = processUserInput(("Give the name of the course"),false);
        String courseCode = processUserInput(("Give the code of the course"),false);
        int courseYear = Integer.parseInt(processUserInput(("Give the year of the course"),false));


        dao.addCourseToDatabase(courseName, courseCode, courseYear);
    }

    public void addModule()
    {
        String moduleName = processUserInput(("Give the name of the module"),false);
        String moduleCode = processUserInput(("Give the code of the module"),false);
        String courseName = dao.retrieveOption("courseName");
        dao.addModuleToDatabase(moduleName, moduleCode, courseName);
    }

    public void addRoom()
    {
        String roomName = processUserInput(("Give the name of the room"),false);
        String roomType = processUserInput(("Give the type of the room (e.g. Lab)"),false);
        int roomCapacity = Integer.parseInt(processUserInput(("Give the capacity of the room"),false));

        dao.addRoomToDatabase(roomName, roomType, roomCapacity);
    }

    public void addLecturer()
    {
        String lecturerName = processUserInput(("Give the name of the room"),false);

        dao.addLecturerToDatabase(lecturerName);
    }

    public void addTimeslot() {


        System.out.println("A dialogue box has opened. Look around for it!");
        String courseName = dao.retrieveOption("courseName");
        String courseYear = dao.retrieveOption("courseYear");

        String moduleName = dao.retrieveOption("moduleName");
        String lecturerName = dao.retrieveOption("lecturerName");
        String roomName = dao.retrieveOption("roomName");
        JOptionPane.showMessageDialog(null, "Go back to the console!");



        // create the new timeslot
        //dao.addTimeslot(new Timeslot(courseName, courseYear, week, day,
                //startTime, endTime, roomName, roomType, lecturerName, moduleName, reference));

    }

    public void addModuleToCourse(){
        System.out.println("Linking modules to courses, dialog box created.");
        String courseName = dao.retrieveOption("courseName");
        int courseYear = Integer.parseInt(dao.retrieveOption("courseYear"));
        String moduleName = dao.retrieveOption("moduleName");

        courseName = courseName + "_" + courseYear;

        dao.addModuleToCourse(courseName,moduleName);
    }
}
