
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Project 5: MealPlanning Application
 *
 * @author Joshua Gabel 
 * CSC-6302
 *
 * This Database Provider class provides methods which ask the DataMgr class for
 * a connection to the database and provide access to data within the meal
 * planning database.
 */
public class MealPlanningDBProvider {

    /**
     * This method establishes a connection to the database, and executes a
     * callable statement to return a ResultSet of CookbookName in the database;
     * the method then maps this data to a Hashtable using unique integer
     * identifiers as the key and CookbookName as the value.
     *
     * @param user, DB credentials
     * @param password, DB credentials
     * @return, Hashtable of CookbookName in DB with unique int as Key.
     */
    public static Hashtable getCookbooks(String user, String password) {
        //init connection, Hashtable for results, and query to find cookbooks.
        Connection myConnection = DataMgr.getMealPlanningConnection(user, password);
        Hashtable<Integer, String> cookbooks = new Hashtable<>();
        int i = 1;

        try {
            //init and execute callable statement
            CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call GetCookbooks()}");
            ResultSet myRelation = myStoredProcedureCall.executeQuery();

            // mapping result set to Hashtable
            while (myRelation.next()) {
                String addToCookbooks = myRelation.getString("CookbookName");
                cookbooks.put(i, addToCookbooks);
                i++;

            }
            return cookbooks;

        } catch (SQLException exception) {
            System.out.println("Statement failed.");

        }
        return null;
    }

    /**
     * This method establishes a connection to the MealPlanning database, and
     * uses a callable statement to execute SQL stored procedure
     * RecipesInCookbook to produce a ResultSet of all recipes in a cookbook;
     * the method then maps this data to a Hashtable using unique integer
     * identifiers as the key and RecipeName as the value.
     *
     * @param cookbookName, the cookbook for which we want to find recipes
     * @param user, DB credentials
     * @param password, DB credentials
     * @return Hashtable of Recipes in a Cookbook with unique int identifies as
     * Key
     */
    public static Hashtable getRecipesInCookbook(String cookbookName, String user, String password) {
        // init connection and Hashtable for results
        Connection myConnection = DataMgr.getMealPlanningConnection(user, password);
        Hashtable<Integer, String> recipes = new Hashtable<>();
        int i = 1;

        try {
            //init and execute callable statement
            CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call RecipesInCookbook(?)}");
            myStoredProcedureCall.setString(1, cookbookName);
            ResultSet myResults = myStoredProcedureCall.executeQuery();

            //mapping results to Hashtable
            while (myResults.next()) {
                String recipeToAdd = myResults.getString("RecipeName");
                recipes.put(i, recipeToAdd);
                i++;

            }
            return recipes;
        } catch (SQLException exception) {
            System.out.println("getRecipesInCookbook failed.");
        }
        return null;
    }

    /**
     * This method establishes a connection to the MealPlanning database, and
     * uses a callable statement to execute SQL stored procedure
     * RecipeIngredients to produce a ResultSet of all ingredients in a recipe;
     * the method then maps this data to an ArrayList and returns the ArrayList.
     *
     * @param recipeName, the recipe for which we want to find ingredients
     * @param user, DB credentials
     * @param password, DB credentials
     * @return String ArrayList of all ingredients
     */
    public static ArrayList<String> getAllIngredients(String recipeName, String user, String password) {
        //init connection and ArrayList for results
        Connection myConnection = DataMgr.getMealPlanningConnection(user, password);
        ArrayList<String> resultArray = new ArrayList<>();

        try {
            //init and execute callable statement
            CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call RecipeIngredients(?)}");
            myStoredProcedureCall.setString(1, recipeName);
            ResultSet myResults = myStoredProcedureCall.executeQuery();

            //mapping results to ArrayList
            while (myResults.next()) {
                String addToResult = myResults.getString("IngredientName");
                resultArray.add(addToResult);
            }
            return resultArray;
        } catch (SQLException exception) {
            System.out.println("getAllIngredients failed.");

        }
        return null;
    }

} //end all
