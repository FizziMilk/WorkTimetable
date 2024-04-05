//This class handles interactions between the database and the rest of the program

import javax.swing.*;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DAO {
    private static final String INSERT_ITEM_SQL = "INSERT INTO timeslots" +
            " (course_name,year,week,day,start_time,end_time,room, room_type, lecturer, module, module_reference,room_capacity)" +
            " VALUES (?,?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?)";
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
    private static final String QUERY_MODULECODE_SQL = "SELECT * FROM modules WHERE module_name = ?";
    private static final String QUERY_TIMESLOTS = "SELECT * FROM timeslots WHERE course_name = ? AND year = ? AND week = ?";

    String TIMESLOT_DAY_WEEK_CHECK = "SELECT * FROM timeslots WHERE Week = ? AND Day = ?";

    private final DatabaseManager databaseManager;

    public DAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addTimeslot(Timeslot timeslot) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM_SQL)) {
            //Set parameters for the PreparedStatement
            ps.setString(1, timeslot.getCourseName()+"_"+timeslot.getYear());
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
            ps.setString(12, timeslot.getRoomCapacity());

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
    public void removeTimeslot(String courseName, int week, String day, String startTime) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM timeslots WHERE course_name = ? AND week = ? AND day = ? AND start_time = ?")) {
            ps.setString(1, courseName);
            ps.setInt(2, week);
            ps.setString(3, day);
            ps.setString(4, startTime);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Timeslot removed successfully.");
            } else {
                System.err.println("Failed to remove timeslot.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: removing timeslot from the database");
            e.printStackTrace();
        }
    }

    public boolean clashCheck(int week, String day, String lecturerName, String newStartTime, String newEndTime, String moduleName, String courseName, String roomName) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(TIMESLOT_DAY_WEEK_CHECK)) {

            // Set the week, day, and module parameters in the prepared statement
            pstmt.setInt(1, week);
            pstmt.setString(2, day);
            InputProcessing input = new InputProcessing();
            // Execute the query
            try (ResultSet rs = pstmt.executeQuery();) {

                // Check if the timeslot already exists for the provided course
                while (rs.next()) {
                    if (rs.getString("course_name").equals(courseName) &&
                            day.equals(rs.getString("day")) &&
                            newStartTime.equals(rs.getString("start_time")) &&
                            newEndTime.equals(rs.getString("end_time"))) {
                        // Timeslot already exists for this course
                        input.processUserInput("Timeslot already exists for this course.\nPress any character and enter to proceed.", false);
                        return true;
                    }
                }

                // Fetch courses associated with the given module
                Set<String> coursesForModule = new HashSet<>();
                String fetchCoursesQuery = "SELECT courses FROM modules_to_courses WHERE modules = ?";
                try (PreparedStatement fetchCoursesStmt = conn.prepareStatement(fetchCoursesQuery)) {
                    fetchCoursesStmt.setString(1, moduleName);
                    try (ResultSet coursesRs = fetchCoursesStmt.executeQuery()) {
                        while (coursesRs.next()) {
                            coursesForModule.add(coursesRs.getString("courses"));
                        }
                    }
                }
                // Check if the module is shared by the provided course
                boolean moduleShared = coursesForModule.contains(courseName);

                // Iterate over the existing timeslots
                while (rs.next()) {
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");
                    String existingLecturerName = rs.getString("lecturer");
                    String existingCourseName = rs.getString("course_name");
                    String existingModuleName = rs.getString("module");
                    String existingRoomName = rs.getString("room");

                    // Check if the module is shared by the provided course
                    if (moduleShared && existingModuleName.equals(moduleName)) {
                        // No clash detection needed if the module is shared by the provided course
                        return false;
                    }

                    // Check if the lecturer is busy at the specified time
                    if (lecturerName.equals(existingLecturerName) &&
                            day.equals(rs.getString("day")) &&
                            newStartTime.compareTo(endTime) < 0 && newEndTime.compareTo(startTime) > 0) {
                        // Clash detected
                        input.processUserInput("Clash detected! Lecturer is busy at that time.\nPress any character and enter to proceed.", false);
                        return true;
                    }

                    // Check if the room is busy at the specified time
                    if (roomName.equals(existingRoomName) &&
                            day.equals(rs.getString("day")) &&
                            newStartTime.compareTo(endTime) < 0 && newEndTime.compareTo(startTime) > 0) {
                        // Clash detected
                        input.processUserInput("Clash detected! Room is busy at that time.\nPress any character and enter to proceed.", false);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false; // No clash detected
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
            ps2.setString(2, moduleName);
            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();
            int rowsAffected2 = ps2.executeUpdate();
            if (rowsAffected > 0 || rowsAffected2 > 0) {
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

    public void addModuleToCourse(String course, String module) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MODULE_TO_COURSE)) {
            ps.setString(1, course);
            ps.setString(2, module);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Module successfully added to course.");
            } else {
                System.err.println("Failed to add module to course.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: adding module to course");
            e.printStackTrace();
        }
    }

    public String retrieveOption(String entry) {
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
            default:
                System.out.println("Error in Retrieve() method");
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
            System.err.println("Exception: retrieving module from courses, from database");
            e.printStackTrace();
        }
        return null;
    }

    public String[] retrieveRoomType(String roomName) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_ROOMS)) {
            ps.setString(1, roomName);
            try (ResultSet rs = ps.executeQuery()) {
                String[] roomInfo = new String[2];
                while (rs.next()) {
                    roomInfo[0] = rs.getString(2);
                    roomInfo[1] = rs.getString(3);
                }
                return roomInfo;
            }
        } catch (SQLException e) {
            System.err.println("Exception: retrieving room_type from room");
            e.printStackTrace();
        }
        return null;
    }

    public String retrieveModuleReference(String module) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_MODULECODE_SQL)) {
            ps.setString(1, module);
            try (ResultSet rs = ps.executeQuery()) {
                String moduleCode = null;
                while (rs.next()) {
                    moduleCode = rs.getString(2);
                }
                return moduleCode;
            }
        } catch (SQLException e) {
            System.err.println("Exception: retrieving moduleCode from modules");
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<TimeSlotRow> findAllTimeSlots(String courseName, int courseYear, int week) {
        ArrayList<TimeSlotRow> rows = new ArrayList<>();

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(QUERY_TIMESLOTS)) {
            statement.setString(1, courseName);
            statement.setInt(2, courseYear);
            statement.setInt(3, week);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TimeSlotRow row = new TimeSlotRow(
                        resultSet.getString(1), // course_name
                        resultSet.getString(2), // year
                        resultSet.getInt(3),    // week
                        resultSet.getString(4), // day
                        LocalTime.parse(resultSet.getString(5)), // start_time
                        LocalTime.parse(resultSet.getString(6)),  // end_time
                        resultSet.getString(7),
                        resultSet.getString(10)
                    );

                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Exception: retrieving room_type from room: " + e.getMessage());
        }

        return rows;
    }


    public record TimeSlotRow(String courseName, String year, int week, String day, LocalTime startTime,
                              LocalTime endTime, String lecture, String room) {

    }
}


