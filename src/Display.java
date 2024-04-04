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
        Iterable<DAO.TimeSlotRow> timeSlotRows = dao.findAllTimeSlots(courseName, courseYear, week);

        for (int hour = 9; hour <= 20; hour++) {
            for (int minutes = 0; minutes <= 30; minutes += 30) {

                String timeAsString = formatTime(hour,minutes);
                TimeSlot timeSlot = new TimeSlot(timeAsString);

                timeSlotsByTime.put(timeAsString, timeSlot);
            }
        }
        for (DAO.TimeSlotRow row : timeSlotRows) {
            Duration duration = Duration.between(row.getStartTime(), row.getEndTime());

            int totalMinutes = (int) duration.toMinutes();
            for (int minute = 0; minute <= totalMinutes; minute += 30) {
                int currentHour = row.getStartTime().getHour() + minute / 60;
                int currentMinute = row.getStartTime().getMinute() + minute % 60;

                // Handle case when minutes exceed 59
                if (currentMinute >= 60) {
                    currentHour++;
                    currentMinute -= 60;
                }

                String timeAsString = formatTime(currentHour, currentMinute);

                TimeSlot timeSlot = timeSlotsByTime.get(timeAsString);
                if (timeSlot == null) {
                    timeSlot = new TimeSlot(timeAsString);
                    timeSlotsByTime.put(timeAsString, timeSlot);
                }

                timeSlot.setLecture(row.getDay(), row.getLecture(),row.getRoom());
            }
        }
        timetable.getItems().addAll(timeSlotsByTime.values());

        root.setCenter(timetable);

        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.setTitle("Timetable App");
        primaryStage.show();
    }

    private static String formatTime(int hour,int minutes) {
        return String.format("%02d:%02d", hour, minutes);
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

        public void setLecture(String day, String lecture,String room) {
            switch (day) {
                case "Monday":
                    monday.set(lecture +" " + room);
                    break;
                case "Tuesday":
                    tuesday.set(lecture +"  " + room);
                    break;
                case "Wednesday":
                    wednesday.set(lecture +" " + room);
                    break;
                case "Thursday":
                    thursday.set(lecture +" " + room);
                    break;
                case "Friday":
                    friday.set(lecture +" " + room);
                    break;
            }
        }
    }
}

