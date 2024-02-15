import java.util.*;

public class Timetable
{
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final DAO dao = new DAO(databaseManager);

    public void addTimeSlotClassroom()
    {
        // Ask for which week for that classroom to be printed
        InputProcessing scan = new InputProcessing();

        String courseName = scan.processUserInput(("Give the name of the course"),false);
        int courseYear = Integer.parseInt(scan.processUserInput("Give the year of the course",true));
        String roomName = scan.processUserInput("What is the name of the room?",false);
        String roomType = scan.processUserInput("What is the type of the room?",false);
        int roomCapacity = Integer.parseInt(scan.processUserInput("How many students are in this class?", true));
        int week = Integer.parseInt(scan.processUserInput("What is the week you wish to select?", true));
        String day = scan.processUserInput("What is the day you wish to select?", false);
        String startTime = scan.processUserInput("Give the start time like so: xx:xx [Minimum 09:00, maximum 18:00]",false);
        String endTime = scan.processUserInput("Give the end time like so: xx:xx [Minimum 10:00, maximum 20:00]",false);
        String lecturer = scan.processUserInput("Who is the lecturer for this timeslot?",false);
        String module = scan.processUserInput("What module is this timeslot for?",false);
        String reference = scan.processUserInput("What is the reference for this module?",false);

        // create the new timeslot
        dao.addTimeslot(new Timeslot(courseName, courseYear, week, day,
                startTime, endTime, roomName, roomType, roomCapacity, lecturer, module, reference));
    }
}

        /*
        System.out.println(
                        "   Timeslot " + (j + 1) +
                                ": " + timeslot.getStartTime() + " - " + timeslot.getEndTime() +
                                " | Lecturer: " + timeslot.getLecturer() +
                                " | Module: " + timeslot.getModule()
        */