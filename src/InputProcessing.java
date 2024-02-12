import java.util.Scanner;
public class InputProcessing {
    //Performs a check by using isValidInput to see if the user input was empty, and allows user to enter q to return to the menu at any time
    //if requireNumeric is set to true, will check if the input is a number

    private final Scanner input = new Scanner(System.in);
    public String processUserInput(String message, boolean requireNumeric) {
        try {
            // Get user input
            System.out.print(message);
            String userInput = input.nextLine();

            // Check if the user wants to quit
            if ("q".equalsIgnoreCase(userInput)) {
                System.out.println("Leaving to menu");

                // MISSING METHOD QUIT TO MENU

            }

            // Validate user input
            if (isValidInput(userInput,requireNumeric)) {
                // Process or store the valid input
                return capitalizeFirstLetter(userInput.toLowerCase());
            } else {
                // Handle invalid input
                System.out.println("Invalid input. Please try again. Or enter q to quit.\n");
                return processUserInput(message,requireNumeric);
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            System.out.println("An error occurred. Please try again. Or enter q to quit.\n");
            return processUserInput(message,requireNumeric);
        }
    }

    private boolean isValidInput(String input, boolean requireNumeric) {
        // non-empty string
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Capitalize the first letter
        input = capitalizeFirstLetter(input);

        if (requireNumeric) {
            try {
                Double.parseDouble(input);
                return true; // input is a valid double
            } catch (NumberFormatException e) {
                return false; // input is not a valid double
            }
        }

        return true; // Input is a non-empty string
    }
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
