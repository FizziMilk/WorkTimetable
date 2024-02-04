
public class Timeslot
{

    private String lecturer;
    private String module;
    private String reference;
    private String type;
    private String startTime;
    private String endTime;

    public Timeslot(String lecturer, String module, String reference, String type, String startTime, String endTime)
    {
        this.lecturer = lecturer;
        this.module = module;
        this.reference = reference;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getModule() {
        return module;
    }

}