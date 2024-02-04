import java.util.*;
public class Week
{
    private List<Day> days;
    private int weekNumber;

    // constructor which creates a new ArrayList of day objects
    public Week(int weekNumber)
    {
        this.weekNumber = weekNumber;
        this.days = new ArrayList<>();
        for (int i = 1; i <= 7; i++)
        {
            String dayName = "Null";
            switch (i)
            {
                case 1:
                    dayName = "Monday";
                    break;
                case 2:
                    dayName = "Tuesday";
                    break;
                case 3:
                    dayName = "Wednesday";
                    break;
                case 4:
                    dayName = "Thursday";
                    break;
                case 5:
                    dayName = "Friday";
                    break;
                case 6:
                    dayName = "Saturday";
                    break;
                case 7:
                    dayName = "Sunday";
                    break;
                default:
                    break;
            }
            days.add(new Day(dayName));
        }
    }

    // add a day object to the existing List of days
    public void addDay(Day day)
    {
        days.add(day);
    }

    // Show List of days in Week object
    public List<Day> getDays()
    {
        return days;
    }

    public int getWeekNumber()
    {
        return weekNumber;
    }
}