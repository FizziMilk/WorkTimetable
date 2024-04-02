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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        while (loop)
        {

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
            switch(week)
            {
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

        TableView<Timeslot> timetable = new TableView<>();
        timetable.setPrefWidth(1000); // Set preferred width for the table

        // Create column for the time slots
        TableColumn<Timeslot, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        timetable.getColumns().add(timeColumn);

        // Create columns for each day
        for (String day : days) {
            TableColumn<Timeslot, String> dayColumn = new TableColumn<>(day);
            dayColumn.setCellValueFactory(cellData -> {
                Timeslot timeslot = cellData.getValue();
                return new SimpleStringProperty(timeslot.getLecture(day));
            });
            timetable.getColumns().add(dayColumn);
        }

        // retrieve arrayList that contains two ArrayLists-> [0] is startTimes, [1] is endTimes, [2] is values
        ArrayList<String>[] arrayTimes = dao.retrieveTimeslots(courseName, courseYear, week);

        List<Timeslot> timeslots = new ArrayList<>();


        // Iterate over the hours and minutes to create empty timeslots
        int min = 0;
        int index = 0;
        int indexB = 0;
        boolean isFinished;

        for (int hour = 9; hour <= 20; hour++) {
            if (index >= arrayTimes[2].size()) {
                break; // Exit the loop if we have processed all timeslots
            }

            String dayChosen = arrayTimes[2].get(index); // get the day

            min = 0;
            isFinished = addTimeSlot(hour, min, arrayTimes, timeslots, index, indexB, dayChosen);

            min = 30;
            isFinished = addTimeSlot(hour, min, arrayTimes, timeslots, index, indexB, dayChosen);

            if (isFinished) {
                index += 3;
                indexB += 1;
            }


        }


        // Add all timeslots to the timetable
        timetable.getItems().addAll(timeslots);

        root.setCenter(timetable);

        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.setTitle("Timetable App");
        primaryStage.show();
    }

    public boolean addTimeSlot(int hour, int min, ArrayList<String>[] arrayTimes, List<Timeslot> timeslots, int index, int indexB, String dayChosen) {

        if ( dayChosen == "Monday") {

        }

        // call recursive function after checking 1 timeslot, and update the indexes to choose the next timeslot for checking with days

        String time = String.format("%02d:%02d", hour, min);
        Timeslot timeslot = new Timeslot(time);



        //String dayChosen = arrayTimes[2].get(index); // indexes 0, 3, 6..
        String roomName = arrayTimes[2].get(index+1);  // indexes 1, 4, 7..
        String moduleName = arrayTimes[2].get(index+2); // indexes 2, 5, 8..
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

    public static class Timeslot {
        private final String time;
        private final StringProperty monday = new SimpleStringProperty("");
        private final StringProperty tuesday = new SimpleStringProperty("");
        private final StringProperty wednesday = new SimpleStringProperty("");
        private final StringProperty thursday = new SimpleStringProperty("");
        private final StringProperty friday = new SimpleStringProperty("");

        public Timeslot(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public String getLecture(String day) {
            switch (day) {
                case "Monday":
                    return monday.get();
                case "Tuesday":
                    return tuesday.get();
                case "Wednesday":
                    return wednesday.get();
                case "Thursday":
                    return thursday.get();
                case "Friday":
                    return friday.get();
                default:
                    return "";
            }
        }

        public StringProperty mondayProperty() {
            return monday;
        }

        public StringProperty tuesdayProperty() {
            return tuesday;
        }

        public StringProperty wednesdayProperty() {
            return wednesday;
        }

        public StringProperty thursdayProperty() {
            return thursday;
        }

        public StringProperty fridayProperty() {
            return friday;
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

/*


   */

