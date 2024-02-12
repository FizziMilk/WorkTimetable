//This class handles interactions between the database and the rest of the program

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO {
    private static final String INSERT_ITEM_SQL = "INSERT INTO timeslots" +
            " (course_name,year,week,day,start_time,end_time,room, room_type, lecturer, module, module_reference)" +
            " VALUES (?,?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
    private final DatabaseManager databaseManager;
    public DAO(DatabaseManager databaseManager) {this.databaseManager = databaseManager;}

    public void addTimeslot(Timeslot timeslot,Course course,Classroom classroom,Week week, String day){
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ITEM_SQL)){
                 //Set parameters for the PreparedStatement
            preparedStatement.setString(1,course.getName());
            preparedStatement.setInt(2,course.getYear());
            preparedStatement.setInt(3,week.getWeekNumber());
            preparedStatement.setString(4,day);
            preparedStatement.setString(5,timeslot.getStartTime());
            preparedStatement.setString(6,timeslot.getEndTime());
            preparedStatement.setString(7,classroom.getName());
            preparedStatement.setString(8,classroom.getType());
            preparedStatement.setString(9,timeslot.getLecturer());
            preparedStatement.setString(10, timeslot.getModule());
            preparedStatement.setString(11,timeslot.getReference());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Timeslot added successfully.");
            } else {
                System.err.println("Failed to add timeslot to the database.");
            }


        }catch (SQLException e){
                 System.err.println("Error adding item to the database");
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

