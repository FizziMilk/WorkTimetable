// To start, a timetable object is created, which has a list of classrooms.
// The user can create a classroom object in this driver file for testing, and add it to the timetable classroom list via a method.
// Upon creating a classroom object, 12 week objects are automatically created
// Every one of these week objects have a number associated with them (1, 2, 3..) for each week in the semester, up until 12
// With every week object created, there are 7 day objects created (Monday, Tuesday... until Sunday)
// Each day object has a String dayName like "Monday" used to identify that day in the week.


import java.util.*;

public class Driver
{
    public static void main (String[] args)
    {

        // MAIN MENU
        System.out.println("Welcome to the Timetable Generation System!");
        InputProcessing input = new InputProcessing();
        // Test unit creation of timetable object
        Timetable semester1 = new Timetable();
        // Test unit creation of classroom object
        Classroom T719 = new Classroom("T-719", "Lab", 40);
        // Test addition to the timetable classroom list
        semester1.addClassroom(T719);

        semester1.addTimeSlotClassroom(T719);
        semester1.printTimetable(T719);

        /*
        while (true)
        {
            System.out.println("<< MAIN MENU >>");
            System.out.println("<< 1 >> - Select Timetable to observe");
            System.out.println("<< 2 >> - Add a classroom to a timetable");
            System.out.println("<< 3 >> - Add a timeslot to a classroom");
            System.out.println("<< 4 >> - Debug mode");

            System.out.println("Enter Input >> ");
            int userInput = input.nextInt();

            switch (userInput)
            {
                case 1:
                    // Test unit

                    break;
                case 2:
                    // run function
                    break;
                case 3:
                    // run function
                    break;
                case 4:
                    // run function
                    break;
                default:
                    System.out.println("Choose a correct input.");
                    break;
            }
        }
*/

    }
}