//This class creates a new course
public class Course {

    private String name;
    private int year;



    public Course(String name, int year){
        setName(name);
        setYear(year);
    }

    //GET SET METHODS
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }


}
