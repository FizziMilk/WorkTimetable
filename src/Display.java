import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class Display extends Application {

    private final ObservableList<String> days = FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
    static InputProcessing input = new InputProcessing();
    DatabaseManager databaseManager = new DatabaseManager();
    DAO dao = new DAO(databaseManager);

    public static void main(String[] args) throws SQLException {

        try {
            menu();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void menu() throws SQLException {
        System.out.println("**********************************************" +
                "\nWelcome to the Timetable Generation System.\n" +
                "**********************************************\n");

        System.out.println("This program lets you add or remove timeslots and more features.");


        boolean loop = true;
        while (loop) {

            System.out.println("""
                    1. Create a new timeslot
                    2. View timeslots
                    3. Database - Add new Course
                    4. Database - Add new Module
                    5. Database - Add new Room
                    6. Database - Add new Lecturer
                    7. Database - Link a module to the course
                    8. Exit
                    """);
            int week = Integer.parseInt(input.processUserInput("Put your numeric input below:", true));
            switch (week) {
                case 1 -> input.addTimeslot();
                case 2 -> launch();
                case 3 -> input.addCourse();
                case 4 -> input.addModule();
                case 5 -> input.addRoom();
                case 6 -> input.addLecturer();
                case 7 -> input.addModuleToCourse();
                case 8 -> loop = false;
                default -> System.out.println("Enter a number --> (not 2 for now) ");
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        System.out.println("Look for dialogue box opening");
        String courseName = dao.retrieveOption("courseName");
        int courseYear = Integer.parseInt(dao.retrieveOption("courseYear"));
        int week = input.weekSelection();

        TableView<TimeSlot> timetable = new TableView<>();
        timetable.setPrefWidth(1000); // Set preferred width for the table

        // Create column for the time slots
        TableColumn<TimeSlot, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        timetable.getColumns().add(timeColumn);

        // Create columns for each day
        for (String day : days) {
            TableColumn<TimeSlot, String> dayColumn = new TableColumn<>(day);
            dayColumn.setCellValueFactory(cellData -> {
                TimeSlot timeslot = cellData.getValue();
                return new SimpleStringProperty(timeslot.getLecture(day));
            });
            timetable.getColumns().add(dayColumn);
        }

        var timeSlotsByTime = new TreeMap<String, TimeSlot>();
        var timeSlotRows = dao.findAllTimeSlots(courseName, courseYear, week);

        for (int hour = 8; hour <= 20; hour++) {
            var hourAsString = formatHour(hour);
            var timeSlot = new TimeSlot(hourAsString);

            timeSlotsByTime.put(hourAsString, timeSlot);
        }

        for (var row : timeSlotRows) {
            var duration = Duration.between(row.getStartTime(), row.getEndTime());

            var hours = duration.toHoursPart();
            for (var hour = 0; hour <= hours; hour++) {
                var hourAsString = formatHour(row.getStartTime().getHour() + hour);
                var timeSlot = timeSlotsByTime.get(hourAsString);

                timeSlot.setLecture(row.getDay(), row.getLecture());
            }
        }

        timetable.getItems().addAll(timeSlotsByTime.values());

        root.setCenter(timetable);

        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.setTitle("Timetable App");
        primaryStage.show();
    }

    private static String formatHour(int hour) {
        return String.format("%02d:00", hour);
    }

    public boolean addTimeSlot(int hour, int min, ArrayList<String>[] arrayTimes, List<TimeSlot> timeslots, int index, int indexB, String dayChosen) {


        String time = String.format("%02d:%02d", hour, min);
        TimeSlot timeslot = new TimeSlot(time);

        //String dayChosen = arrayTimes[2].get(index); // indexes 0, 3, 6..
        String roomName = arrayTimes[2].get(index + 1);  // indexes 1, 4, 7..
        String moduleName = arrayTimes[2].get(index + 2); // indexes 2, 5, 8..
        LocalTime start = LocalTime.parse(arrayTimes[0].get(indexB));
        LocalTime end = LocalTime.parse(arrayTimes[1].get(indexB));
        System.out.println(start);


        if ((hour > start.getHour() || (hour == start.getHour() && min >= start.getMinute())) &&
                (hour < end.getHour() || (hour == end.getHour() && min <= end.getMinute()))) {
            timeslot.setLecture(dayChosen, moduleName + ", " + roomName);
            timeslots.add(timeslot);
        } else {
            timeslots.add(timeslot);
        }
        return hour == end.getHour() || min == end.getMinute() % 60;

    }

    public static class TimeSlot {
        private final String time;
        private final StringProperty monday = new SimpleStringProperty("");
        private final StringProperty tuesday = new SimpleStringProperty("");
        private final StringProperty wednesday = new SimpleStringProperty("");
        private final StringProperty thursday = new SimpleStringProperty("");
        private final StringProperty friday = new SimpleStringProperty("");

        public TimeSlot(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public String getLecture(String day) {
            return switch (day) {
                case "Monday" -> monday.get();
                case "Tuesday" -> tuesday.get();
                case "Wednesday" -> wednesday.get();
                case "Thursday" -> thursday.get();
                case "Friday" -> friday.get();
                default -> "";
            };
        }

        public void setLecture(String day, String lecture) {
            switch (day) {
                case "Monday":
                    monday.set(lecture);
                    break;
                case "Tuesday":
                    tuesday.set(lecture);
                    break;
                case "Wednesday":
                    wednesday.set(lecture);
                    break;
                case "Thursday":
                    thursday.set(lecture);
                    break;
                case "Friday":
                    friday.set(lecture);
                    break;
            }
        }
    }
}

