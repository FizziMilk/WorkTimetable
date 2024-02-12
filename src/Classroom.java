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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public List<Week> getWeeks() {
        return weeks;
    }
}

