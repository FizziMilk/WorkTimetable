import java.util.*;
public class Timetable
{

    private List<Classroom> classrooms;
    public Timetable()
    {
        this.classrooms = new ArrayList<>();
    }

    public void addClassroom(Classroom classroom)
    {
        classrooms.add(classroom);
    }

    public List<Classroom> getClassrooms() { return classrooms; }

    private final DatabaseManager databaseManager = new DatabaseManager();
    private final DAO dao = new DAO(databaseManager);
    // Print the timetable for a given classroom
    public void printTimetable(Classroom classroom)
    {
        // Ask for which week for that classroom to be printed
        InputProcessing scan = new InputProcessing();
        int weekNumber = Integer.parseInt(scan.processUserInput("Enter the week number",true));
        int weekListIndex = 0;

        List<Week> weeks = classroom.getWeeks();
        int weekSize = weeks.size();

        // loop through the Week ArrayList and find matching weekNumber
        for (int i = 0; i < weekSize; i++ )
        {
            // Obtain object Week from the List
            Week selectedWeek = weeks.get(i);
            if (weekNumber == selectedWeek.getWeekNumber())
            {
                // This week is chosen: break loop and print
                System.out.println("Week successfully found");
                weekListIndex = i;
                break;
            }
        }

        // If reaching this point, weekNumber was found
        Week selectedWeek = weeks.get(weekListIndex);

        // Obtain days List in the selected week
        List<Day> days = selectedWeek.getDays();

        for (int i = 0; i < days.size(); i++)
        {
            // Loop through each day in the list
            Day day = days.get(i);
            System.out.println("\nDay " + (i + 1) + ":");

            List<Timeslot> timeslots = day.getTimeslots();

            for (int j = 0; j < timeslots.size(); j++)
            {
                Timeslot timeslot = timeslots.get(j);
                System.out.println(
                        "   Timeslot " + (j + 1) +
                                ": " + timeslot.getStartTime() + " - " + timeslot.getEndTime() +
                                " | Lecturer: " + timeslot.getLecturer() +
                                " | Module: " + timeslot.getModule()
                );
            }
        }
    }


    public void addTimeSlotClassroom(Classroom classroom)
    {
        // Ask for which week for that classroom to be printed
        InputProcessing scan = new InputProcessing();
        int weekListIndex = 0;
        int weekNumber = 0;
        try {
            weekNumber = Integer.parseInt(scan.processUserInput("Which week would you like to add the timeslot to?",true));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String dayChosen = scan.processUserInput("Which day would you like to add the timeslot to? E.G. Monday or Tuesday...",false);


        List<Week> weeks = classroom.getWeeks();
        int weekSize = weeks.size();

        // loop through the Week ArrayList and find matching weekNumber
        for (int i = 0; i < weekSize; i++)
        {
            // Obtain object Week from the List
            Week selectedWeek = weeks.get(i);
            if (weekNumber == selectedWeek.getWeekNumber())
            {
                // This week is chosen: break loop and print
                System.out.println("Week successfully found");
                weekListIndex = i;
                break;
            }
        }

        // If reaching this point, weekNumber was found
        Week selectedWeek = weeks.get(weekListIndex);

        // Obtain days List in the selected week
        List<Day> days = selectedWeek.getDays();

        for (Day day : days) {
            // Loop through each day in the list
            if (day.getDay().equals(dayChosen)) {
                // if true, the day has been selected, add the timeslot.
                System.out.println("Day successfully found");

                String courseName = scan.processUserInput(("Give the name of the course"),false);
                int courseYear = Integer.parseInt(scan.processUserInput("Give the year of the course",true));

                String roomName = scan.processUserInput("What is the name of the room?",false);



                String startTime = scan.processUserInput("Give the start time like so: xx:xx [Minimum 09:00, maximum 18:00]",false);
                String endTime = scan.processUserInput("Give the end time like so: xx:xx [Minimum 10:00, maximum 20:00]",false);
                String lecturer = scan.processUserInput("Who is the lecturer for this timeslot?",false);
                String module = scan.processUserInput("What module is this timeslot for?",false);
                String reference = scan.processUserInput("What is the reference for this module?",false);
                String type = scan.processUserInput("Is this timeslot a lab, workshop or lecture?",false);
                int capacity = Integer.parseInt(scan.processUserInput("How many students are in this class?", true));

                // create the new timeslot
               // day.addTimeslot(new Timeslot(lecturer, module, reference, type, startTime, endTime));
                dao.addTimeslot(new Timeslot(lecturer, module, reference, type, startTime, endTime),new Course(courseName,courseYear),new Classroom(roomName,type, capacity),selectedWeek,dayChosen);
                break;
            } else
            {
                System.out.println("Unable to find the day you requested... Unusual? This message should not appear.");
            }
        }
    }
}