import java.util.*;
public class Classroom
{
    String name;
    String type;
    int capacity;
    private List<Week> weeks;

    public Classroom (String name, String type, int capacity)
    {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.weeks = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
        {
            weeks.add(new Week(i));
        }
    }

    public List<Week> getWeeks() {
        return weeks;
    }
}