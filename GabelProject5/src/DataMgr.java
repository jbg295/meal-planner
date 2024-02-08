
import java.sql.*;

/**
 * Project 5: MealPlanning Application
 *
 * @author Joshua Gabel 
 * CSC-6302
 *
 * This Database Manager class provides a method to connect to MealPlanning SQL
 * database.
 */
public class DataMgr {

    /**
     * Method that takes SQL username and password as input and returns
     * Connection object to MealPlanning SQL database.
     *
     * @param user, SQL username
     * @param password, SQL password
     * @return, Connection object to MealPlanning SQL database.
     */
    public static Connection getMealPlanningConnection(String user, String password) {
        String databaseToConnect = "MealPlanning";
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseToConnect, user, password);

        } catch (SQLException exception) {
            System.out.println("Failed to connect to the database" + exception.getMessage());
            return null;
        }
    }

}
