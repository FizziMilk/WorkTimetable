//This class handles interactions between the database and the rest of the program

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAO {
    private static final String INSERT_ITEM_SQL = "INSERT INTO timeslots" +
            " (course_name,year,week,day,start_time,end_time,room, room_type, lecturer, module, module_reference)" +
            " VALUES (?,?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
    private static final String INSERT_COURSE_SQL = "INSERT INTO courses" +
            " (course_name, course_code, course_year)" +
            " VALUES (?,?,?)";
    private static final String INSERT_ROOM_SQL = "INSERT INTO rooms" +
            " (room_name, room_type, room_capacity)" +
            " VALUES (?,?,?)";
    private static final String INSERT_MODULE_SQL = "INSERT INTO modules" +
            " (module_name, module_code)" +
            " VALUES (?,?)";
    private static final String INSERT_LECTURER_SQL = "INSERT INTO lecturers" +
            " (lecturer_name)" +
            " VALUES (?)";

    private static final String INSERT_MODULE_TO_COURSE = "INSERT INTO modules_to_courses" +
            "(courses, modules) " +
            "VALUES (?,?)";

    private static final String RETRIEVE_COURSES_SQL = "SELECT * FROM courses";
    private static final String RETRIEVE_ROOMS_SQL = "SELECT * FROM rooms";
    private static final String RETRIEVE_MODULES_SQL = "SELECT * FROM modules";
    private static final String RETRIEVE_LECTURERS_SQL = "SELECT * FROM lecturers";

    private static final String QUERY_MODULES_FROM_COURSES = "SELECT * FROM modules_to_courses WHERE courses = ?";
    private static final String QUERY_COURSES = "SELECT * FROM courses WHERE course_name = ? AND course_year = ?";
    private static final String QUERY_MODULES = "SELECT * FROM modules WHERE module_name = ?";
    private static final String QUERY_ROOMS = "SELECT * FROM rooms WHERE room_name = ?";


    private final DatabaseManager databaseManager;

    public DAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addTimeslot(Timeslot timeslot) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM_SQL)) {
            //Set parameters for the PreparedStatement
            ps.setString(1, timeslot.getCourseName());
            ps.setString(2, timeslot.getYear());
            ps.setInt(3, timeslot.getWeek());
            ps.setString(4, timeslot.getDay());
            ps.setString(5, timeslot.getStartTime());
            ps.setString(6, timeslot.getEndTime());
            ps.setString(7, timeslot.getRoom());
            ps.setString(8, timeslot.getRoomType());
            ps.setString(9, timeslot.getLecturer());
            ps.setString(10, timeslot.getModule());
            ps.setString(11, timeslot.getModuleReference());

            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Timeslot added successfully.");
            } else {
                System.err.println("Failed to add timeslot to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: adding timeslot to the database");
            e.printStackTrace();
        }
    }

    public void addCourseToDatabase(String courseName, String courseCode, int courseYear) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_COURSE_SQL)) {
            ps.setString(1, courseName);
            ps.setString(2, courseCode);
            ps.setInt(3, courseYear);

            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course added successfully.");
            } else {
                System.err.println("Failed to add course to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: adding course to the database");
            e.printStackTrace();
        }
    }

    public void addRoomToDatabase(String roomName, String roomType, int roomCapacity) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ROOM_SQL)) {
            ps.setString(1, roomName);
            ps.setString(2, roomType);
            ps.setInt(3, roomCapacity);

            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Room added successfully.");
            } else {
                System.err.println("Failed to add room to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: adding room to the database");
            e.printStackTrace();
        }
    }

    public void addModuleToDatabase(String moduleName, String moduleCode, String courseName) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MODULE_SQL);
             PreparedStatement ps2 = conn.prepareStatement(INSERT_MODULE_TO_COURSE)) {

            ps.setString(1, moduleName);
            ps.setString(2, moduleCode);

            //Adds the module to modules_to_courses database
            ps2.setString(1, courseName);
            ps2.setString(2,moduleName);
            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();
            int rowsAffected2 = ps2.executeUpdate();
            if (rowsAffected > 0 || rowsAffected2 > 0 ) {
                System.out.println("Module added successfully.");
            } else {
                System.err.println("Failed to add module to the database.");
            }


        } catch (SQLException e) {
            System.err.println("Exception: adding module to the database");
            e.printStackTrace();
        }
    }

    public void addLecturerToDatabase(String lecturerName) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_LECTURER_SQL)) {
            ps.setString(1, lecturerName);

            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Lecturer added successfully.");
            } else {
                System.err.println("Failed to add lecturer to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: adding lecturer to the database");
            e.printStackTrace();
        }
    }

    public void addModuleToCourse(String course, String module){
            try(Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MODULE_TO_COURSE)){
                ps.setString(1,course);
                ps.setString(2,module);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Module successfully added to course.");
                } else {
                    System.err.println("Failed to add module to course.");
                }
            }  catch (SQLException e) {
                    System.err.println("Exception: adding module to course");
                    e.printStackTrace();
            }
    }

    public String retrieveOption(String entry) {
        System.out.println("A dialog box has appeared, look around for it.");
        String x = null;
        String msg = "";
        int selectedColumn = 1;
        switch (entry) {
            case "courseName":
                msg = "Select the course";
                x = RETRIEVE_COURSES_SQL;
                break;
            case "courseYear":
                msg = "Select the year of the course";
                x = RETRIEVE_COURSES_SQL;
                selectedColumn = 3;
                break;
            case "moduleName":
                msg = "Select the module for this timeslot";
                x = RETRIEVE_MODULES_SQL;
                break;
            case "lecturerName":
                msg = "Select the lecturer for this timeslot";
                x = RETRIEVE_LECTURERS_SQL;
                break;
            case "roomName":
                msg = "Select the room for this timeslot";
                x = RETRIEVE_ROOMS_SQL;
                break;
            default: System.out.println("Error in Retrieve() method");
        }

        Set<String> options = new HashSet<>();
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(x)) {
            try (ResultSet rs = ps.executeQuery()) {
                JComboBox<String> comboBox = new JComboBox<>();
                while (rs.next()) {
                    String option = rs.getString(selectedColumn);
                    options.add(option);
                }
                for (String optionInSet : options) {
                    comboBox.addItem(optionInSet);
                }
                JOptionPane.showMessageDialog(null, comboBox, msg, JOptionPane.QUESTION_MESSAGE);
                String selectedOption = (String) comboBox.getSelectedItem();
                return selectedOption;

            }
        } catch (SQLException e) {
            System.err.println("Exception: retrieving resources from database");
            e.printStackTrace();
        }
        return null;
    }

    // retrieve method, make checks for: what course was selected
    // private static final String QUERY_MODULES_FROM_COURSES = "SELECT * FROM modules_to_courses WHERE courses = ?";
    public String retrieveOption2(String course) {

        String msg = "Which module from this course and year?";
        System.out.println(course);
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_MODULES_FROM_COURSES)) {
            ps.setString(1, course);
            try (ResultSet rs = ps.executeQuery()) {
                JComboBox<String> comboBox = new JComboBox<>();
                while (rs.next()) {
                    String option = rs.getString(2);
                    System.out.println(rs.getString(2));
                    comboBox.addItem(option);
                }
                JOptionPane.showMessageDialog(null, comboBox, msg, JOptionPane.QUESTION_MESSAGE);
                String selectedOption = (String) comboBox.getSelectedItem();
                return selectedOption;

            }
        } catch (SQLException e) {
            System.err.println("Exception: retrieving method from courses, from database");
            e.printStackTrace();
        }
        return null;
    }
}


