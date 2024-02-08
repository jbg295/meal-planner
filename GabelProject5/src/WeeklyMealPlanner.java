
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Project 5: MealPlanning Application
 *
 * @author Joshua Gabel 
 * CSC-6302
 *
 * This class contains the main function for the MealPlanning Application,
 * looping through a program which allows the user to select cookbooks, recipes
 * within cookbooks, view ingredients in recipes, and add recipes and view a
 * seven day meal plan.
 */
public class WeeklyMealPlanner {

    public static void main(String[] args) {
        // init ShoppingList and MealPlan
        ArrayList<String> shoppingList = new ArrayList<>();
        Hashtable<String, String> mealplan = initMealplan();

        //select user and password from args
        String user = args[0];
        String password = args[1];

        //init scanner and variable for user input
        Scanner in = new Scanner(System.in);
        int choice;

        //program loop.
        do {
            //print menu
            printMenu();

            //user input
            choice = in.nextInt();
            switch (choice) {

                default -> //check for invalid input
                    System.out.println("Invalid choice.");

                case 0 -> {
                }

                case 1 -> // print meal plan
                    printMealplan(mealplan);

                case 2 -> //add a recipe to meal plan
                    addNewMeal(mealplan, shoppingList, user, password);

                case 3 -> //remove a meal
                    removeAMeal(mealplan, shoppingList, user, password);

                case 4 -> //display shopping list
                    printShoppingList(shoppingList);

            }
            // end program
            //end switch

        } while (choice != 0); //end program loop

    }

    /**
     * Method that initializes and returns the new mealplan.
     *
     * @return, new mealplan hashtable with blank values for for value (meal)
     */
    public static Hashtable<String, String> initMealplan() {
        Hashtable<String, String> mealplan = new Hashtable<>();
        mealplan.put("Day 1", "-");
        mealplan.put("Day 2", "-");
        mealplan.put("Day 3", "-");
        mealplan.put("Day 4", "-");
        mealplan.put("Day 5", "-");
        mealplan.put("Day 6", "-");
        mealplan.put("Day 7", "-");
        return mealplan;

    }

    /**
     * Method that adds a meal to the mealplan.
     *
     * @param mealplan, hashtable
     * @param shoppingList, String ArrayList
     * @param user, DB credential
     * @param password, DB credintial
     */
    public static void addNewMeal(Hashtable<String, String> mealplan, ArrayList<String> shoppingList, String user, String password) {
        //init scanner and variable for input
        Scanner in = new Scanner(System.in);
        int choice;

        // Retrieving and displaying cookbooks.
        System.out.println("Cookbooks:");
        Hashtable cookbooks = MealPlanningDBProvider.getCookbooks(user, password);
        Set keys = cookbooks.keySet();
        for (int i = 1; i <= keys.size(); i++) {
            System.out.println(i + ". " + cookbooks.get(i));
        }

        // User input to select cookbook
        System.out.println("Select:");
        choice = in.nextInt();

        //isolate chosen cookbook and cast as a string 
        String cookbookChoice = (String) cookbooks.get(choice);

        // Retrieving and displaying Recipes in chosen cookbook
        System.out.println(cookbookChoice + " Recipes:");
        Hashtable<Integer, String> recipesInCookbook = MealPlanningDBProvider.getRecipesInCookbook(cookbookChoice, user, password);
        Set cookbookKeys = recipesInCookbook.keySet();
        for (int i = 1; i <= cookbookKeys.size(); i++) {
            System.out.println(i + ". " + recipesInCookbook.get(i));
        }

        // user input to select recipe
        System.out.println("Select Recipe/View Ingredients:");
        choice = in.nextInt();

        //isolate chosen recipe string
        String recipeChoice = (String) recipesInCookbook.get(choice);

        //use callable procedure to retrieve and display all ingredients in chosen recipe
        System.out.println(recipeChoice + " Ingredients:");
        ArrayList<String> ingredients = MealPlanningDBProvider.getAllIngredients(recipeChoice, user, password);
        System.out.println(ingredients);

        // user input to add recipe to meal plan or not
        System.out.println("Add to Mealplan? (1 = No, 2 = Yes)");
        choice = in.nextInt();
        switch (choice) {

            default: //check for invalid input
                System.out.println("Invalid choice.");
                break;
            case 2: // YES add to meal plan

                //user input to select day of the week
                System.out.println("Select Day (1-7):");
                choice = in.nextInt();

                // convert day choice to String for hashtable
                String choiceString = Integer.toString(choice);
                String day = "Day " + choiceString;

                // add key value mealplan pair to hashtable
                String dayValue = mealplan.get(day);
                if (!dayValue.equals("-")) {
                    System.out.println("Meal already planned for " + day);

                } else {
                    mealplan.put(day, recipeChoice);
                    System.out.println(recipeChoice + " added to mealplan on day: " + choiceString);
                    //iterate ingredients and add to shopping list.
                    Iterator it = ingredients.iterator();
                    while (it.hasNext()) {
                        String ingredientToAdd = (String) it.next();
                        shoppingList.add(ingredientToAdd);
                    }

                }
                break;

            case 1: // don't add to meal plan
                break;
        }
    }

    /**
     * Method that removes a meal from the mealplan
     *
     * @param mealplan, hashtable
     * @param shoppingList, String ArrayList
     * @param user, DB credential
     * @param password, DB credintial
     */
    public static void removeAMeal(Hashtable<String, String> mealplan, ArrayList<String> shoppingList, String user, String password) {
        //init scanner and variable for input
        Scanner in = new Scanner(System.in);
        int choice;

        //print meal plean
        printMealplan(mealplan);

        //user input for day
        System.out.println("Which day?");
        choice = in.nextInt();

        //cast choice as String and concat for mealplan
        String dayToRemoveChoice = Integer.toString(choice);
        String dayToRemove = "Day " + dayToRemoveChoice;

        //Select recipe
        String recipeToRemove = mealplan.get(dayToRemove);

        //remove meal
        mealplan.put(dayToRemove, "-");
        System.out.println(recipeToRemove + " successfully removed from " + dayToRemove);

        //remove ingredients from shopping list
        ArrayList<String> ingredientsToRemove = MealPlanningDBProvider.getAllIngredients(recipeToRemove, user, password);
        Iterator it = ingredientsToRemove.iterator();
        while (it.hasNext()) {
            String removeThisIngredient = (String) it.next();
            shoppingList.remove(removeThisIngredient);

        }

    }

    /**
     * Method that takes Hashtable mealplan as input and prints to console.
     *
     * @param mealplan, Hashtable of mealplan
     */
    public static void printMealplan(Hashtable<String, String> mealplan) {
        System.out.println(".....................");
        System.out.println("   Day        Meal");
        System.out.println("----------|----------");
        System.out.println("   Day 1  |   " + mealplan.get("Day 1"));
        System.out.println("   Day 2  |   " + mealplan.get("Day 2"));
        System.out.println("   Day 3  |   " + mealplan.get("Day 3"));
        System.out.println("   Day 4  |   " + mealplan.get("Day 4"));
        System.out.println("   Day 5  |   " + mealplan.get("Day 5"));
        System.out.println("   Day 6  |   " + mealplan.get("Day 6"));
        System.out.println("   Day 7  |   " + mealplan.get("Day 7"));
        System.out.println("----------|----------");
        System.out.println(".....................");

    }

    /**
     * Method that prints application's main menu.
     */
    public static void printMenu() {
        System.out.println("\nWeekly Meal Planner:");
        System.out.println("1. View Meal Plan");
        System.out.println("2. Add a Meal");
        System.out.println("3. Remove a Meal");
        System.out.println("4. Generate Shopping List");
        System.out.println("0. End");
        System.out.println("What would you like to do?");

    }

    /**
     * Method that prints shopping list
     *
     * @param shoppingList, ArrayList of Strings
     */
    public static void printShoppingList(ArrayList<String> shoppingList) {
        System.out.println("Shopping List:");
        System.out.println(shoppingList);

    }

}// end all
