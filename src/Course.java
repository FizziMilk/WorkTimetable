public class Course {

    private String name;
    private String code;
    private String year;

    public Course(String name, String code, String year){
        this.name = name;
        this.code = code;
        this.year = year;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
