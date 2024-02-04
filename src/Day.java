import java.util.*;
public class Day {

    String day;
    private List<Timeslot> timeslots;
    public Day (String day)
    {
        this.day = day;
        this.timeslots = new ArrayList<>();
    }

    public void addTimeslot(Timeslot timeslot)
    {
        timeslots.add(timeslot);
    }

    public List<Timeslot> getTimeslots()
    {
        return timeslots;
    }

    public String getDay() { return day; };
}
