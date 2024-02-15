public class Course {

    private String name;
    private String code;

    public Course(String name, String code){
        this.name = name;
        this.code = code;
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
}
