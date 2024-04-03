public class Timeslot {


    private String courseName;
    private String year;
    private int week;
    private String day;
    private String startTime;
    private String endTime;
    private String room;
    private String roomType;
    private String roomCapacity;
    private String lecturer;
    private String module;
    private String moduleReference;

    public Timeslot(String courseName, String year, int week, String day, String startTime,
                    String endTime, String room, String roomType,String lecturer, String module, String moduleReference,String roomCapacity) {
        this.courseName = courseName;
        this.year = year;
        this.week = week;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.roomType = roomType;
        this.roomCapacity = roomCapacity;
        this.lecturer = lecturer;
        this.module = module;
        this.moduleReference = moduleReference;
    }


    public String getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleReference() {
        return moduleReference;
    }

    public void setModuleReference(String moduleReference) {
        this.moduleReference = moduleReference;
    }





}


