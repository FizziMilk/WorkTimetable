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

    // Print the timetable for a given classroom
    public void printTimetable(Classroom classroom)
    {
        // Ask for which week for that classroom to be printed
        System.out.println("Which week would you like to be printed?");
        Scanner scan = new Scanner(System.in);
        int weekNumber = scan.nextInt();
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
            } else
            {
                System.out.println("Unable to find weekNumber linkage");
                return;
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
        Scanner scan = new Scanner(System.in);
        int weekListIndex = 0;

        System.out.println("Which week would you like to add the timeslot to?");
        int weekNumber = 0;
        try {
            weekNumber = Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        System.out.println("Which day would you like to add the timeslot to? E.G. Monday or Tuesday...");
        String dayChosen = scan.nextLine();


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
            } else
            {
                System.out.println("Unable to find weekNumber linkage");
                return;
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

                System.out.println("Give the start time like so: xx:xx [Minimum 09:00, maximum 18:00]");
                String startTime = scan.nextLine();
                System.out.println("Give the end time like so: xx:xx [Minimum 10:00, maximum 20:00]");
                String endTime = scan.nextLine();
                System.out.println("Who is the lecturer for this timeslot?");
                String lecturer = scan.nextLine();
                System.out.println("What module is this timeslot for?");
                String module = scan.nextLine();
                System.out.println("What is the reference for this module?");
                String reference = scan.nextLine();
                System.out.println("Is this timeslot a lab, workshop or lecture?");
                String type = scan.nextLine();

                // create the new timeslot
                day.addTimeslot(new Timeslot(lecturer, module, reference, type, startTime, endTime));
                break;
            } else
            {
                System.out.println("Unable to find the day you requested... Unusual? This message should not appear.");
            }
        }
    }
}