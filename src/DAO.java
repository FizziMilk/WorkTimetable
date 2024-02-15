//This class handles interactions between the database and the rest of the program

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO {
    private static final String INSERT_ITEM_SQL = "INSERT INTO timeslots" +
            " (course_name,year,week,day,start_time,end_time,room, room_type, lecturer, module, module_reference)" +
            " VALUES (?,?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
    private static final String INSERT_COURSE_SQL = "INSERT INTO courses" +
            " (course_name, course_code)" +
            " VALUES (?,?)";
    private static final String INSERT_ROOM_SQL = "INSERT INTO rooms" +
            " (room_name, room_type, room_capacity)" +
            " VALUES (?,?,?)";
    private static final String INSERT_MODULE_SQL = "INSERT INTO modules" +
            " (module_name, module_code)" +
            " VALUES (?,?)";

    private final DatabaseManager databaseManager;

    public DAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addTimeslot(Timeslot timeslot) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM_SQL)) {
            //Set parameters for the PreparedStatement
            ps.setString(1, timeslot.getCourseName());
            ps.setInt(2, timeslot.getYear());
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

    public void addCourseToDatabase(String courseName, String courseCode) {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_COURSE_SQL)) {
            ps.setString(1, courseName);
            ps.setString(2, courseCode);

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

    public void addRoomToDatabase(String roomName, String roomType, int roomCapacity)
    {
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

    public void addModuleToDatabase(String moduleName, String moduleCode)
    {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MODULE_SQL)) {
            ps.setString(1, moduleName);
            ps.setString(2, moduleCode);

            // executeUpdate returns 1 if successful
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Module added successfully.");
            } else {
                System.err.println("Failed to add module to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Exception: adding module to the database");
            e.printStackTrace();
        }
    }





    /*public void addItemToDatabase(Product product) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ITEM_SQL)) {

            //Set parameters for the PreparedStatement
            preparedStatement.setString(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setDouble(4,product.getPrice());
            preparedStatement.setString(5, product.getType());
            preparedStatement.setString(6, product.getDescription());

            //Execute the update
            preparedStatement.executeUpdate();

            System.out.println("Item added to the database.");
        }catch (SQLException e) {
            System.err.println("Error adding item to the database");
            e.printStackTrace();
        }
        //Execute the update */
}


