import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import javax.swing.*;
public class InputProcessing {
    //Performs a check by using isValidInput to see if the user input was empty, and allows user to enter q to return to the menu at any time
    //if requireNumeric is set to true, will check if the input is a number
    private final Scanner input = new Scanner(System.in);
    DatabaseManager databaseManager = new DatabaseManager();
    DAO dao = new DAO(databaseManager);

    String[] timesArray = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00"};

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
        String course = courseName + "_" + courseYear;
        String moduleName = dao.retrieveOption2(course);
        String lecturerName = dao.retrieveOption("lecturerName");
        String roomName = dao.retrieveOption("roomName");

        String roomType = dao.retrieveRoomType(roomName);

        int week = weekSelection();
        String day = daySelection();



        // create the new timeslot
       // dao.addTimeslot(new Timeslot(courseName, courseYear, week, day,
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

    public int weekSelection(){
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++){
            comboBox.addItem(i);
        }
        JOptionPane.showMessageDialog(null,comboBox,"Please select the week",JOptionPane.QUESTION_MESSAGE);
        int week = (int) comboBox.getSelectedItem();

        JOptionPane.showMessageDialog(null, "The selected week is : " + week);

        return week;
    }
    public String daySelection(){


        JComboBox<String> comboBox2 = new JComboBox<>();
        comboBox2.addItem("Monday");
        comboBox2.addItem("Tuesday");
        comboBox2.addItem("Wednesday");
        comboBox2.addItem("Thursday");
        comboBox2.addItem("Friday");

        JOptionPane.showMessageDialog(null,comboBox2,"Please select the day",JOptionPane.QUESTION_MESSAGE);
        String day = (String) comboBox2.getSelectedItem();

        JOptionPane.showMessageDialog(null,"The selected day is " + day);

        JOptionPane.showMessageDialog(null, "Go back to the console!");

        return day;
    }

    public Duration calculateDuration(String startTimeString, String endTimeString) {
        startTimeString = "09:00";
        endTimeString = "10:30";

        Duration duration = calculateDuration(startTimeString, endTimeString);
        System.out.println("Duration: " + duration);
        // Parse strings to LocalTime objects
        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalTime endTime = LocalTime.parse(endTimeString);

        // Calculate duration between start and end times
        duration = Duration.between(startTime, endTime);

        //Put this into where you'll use the timeslot duration
        // long hours = duration.toHours();
        // long minutes = duration.toMinutes() % 60;
        // System.out.println("Hours: " + hours + ", Minutes: " + minutes);

        return duration;
    }

    public String selectStartTime() {

        String msg = "Please select a start time for this class.";

        JComboBox<String> comboBox = new JComboBox<>();
        //-3 to exclude the 19:30 and 20:00 timeslots
        for(int i = 0; i <= timesArray.length - 3; i++){
            comboBox.addItem(timesArray[i]);
        }
        JOptionPane.showMessageDialog(null,comboBox,msg,JOptionPane.QUESTION_MESSAGE);
        String startTime = (String) comboBox.getSelectedItem();
        return startTime;
    }


    public String selectEndTime(){

        String msg = "Please select an end time for this class.";
        int startTimeIndex = 0;
        String startTime = selectStartTime();

        for(int i = 0; i <= timesArray.length - 1; i++){
            if(Objects.equals(timesArray[i], startTime)){
                startTimeIndex = i;
            }
        }
        JComboBox<String> comboBox = new JComboBox<>();
        for(int i = startTimeIndex; i <= timesArray.length - 1; i++){
            comboBox.addItem(timesArray[i]);
        }
        JOptionPane.showMessageDialog(null,comboBox,msg,JOptionPane.QUESTION_MESSAGE);
        String endTime = (String) comboBox.getSelectedItem();

        return endTime;
    }




}