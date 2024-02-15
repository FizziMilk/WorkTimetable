import java.util.*;
public class Classroom
{
    String name;
    String type;
    int capacity;

    public Classroom (String name, String type, int capacity)
    {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
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
}

