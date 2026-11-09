import java.util.Scanner;
import java.util.List;

public class SafeInput {
    /**
     * Lists referenced and used by the getYNConfirm method.
     * Class-level so instantiation is done only once and not on every call of the getYNConfirm method.
     */
    private static final List<String> yesResponses = List.of("yes", "y");
    private static final List<String> noResponses = List.of("no", "n");

    /**
     * A method to validate user input for a string that needs to be a non-zero length.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @return a String response that is not zero length.
     */
    public static String getNonZeroLenString(Scanner pipe, String prompt) {
        while(true) {
            System.out.printf("%s>> ", prompt);
            String retString = pipe.nextLine().strip();
            if(retString.isBlank()) {
                System.out.println("Nothing was entered! You must enter something!");
            } else {
                return retString;
            }
        }
    }

    /**
     * A method to validate user input for an integer.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @return an integer response.
     */
    public static int getInt(Scanner pipe, String prompt) {
        while(true) {
            System.out.printf("%s>> ", prompt);
            if(pipe.hasNextInt()) {
                int retInt = pipe.nextInt();
                pipe.nextLine(); // Clears newline from buffer.
                return retInt;
            } else {
                String trash = pipe.nextLine();
                System.out.printf("'%s' is not a valid input (must be a number (data type integer))! Please try again!%n", trash);
            }
        }
    }

    /**
     * A method to validate user input for an integer that needs to be within a range.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @param low the lower boundary of the range.
     * @param high the upper boundary of the range.
     * @return an integer response.
     */
    public static int getRangedInt(Scanner pipe, String prompt, int low, int high) {
        while(true) {
            int retInt = getInt(pipe, "%s (%d - %d)".formatted(prompt, low, high));
            if(retInt >= low && retInt <= high) {
                return retInt;
            } else {
                System.out.printf("'%d' is outside of the valid range (%d to %d, inclusive)! Please try again!%n", retInt, low, high);
            }
        }
    }

    /**
     * A method to validate user input for a double.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @return a double response.
     */
    public static double getDouble(Scanner pipe, String prompt) {
        while(true) {
            System.out.printf("%s>> ", prompt);
            if(pipe.hasNextDouble()) {
                double retDouble = pipe.nextDouble();
                pipe.nextLine(); // Clears newline from buffer.
                return retDouble;
            } else {
                String trash = pipe.nextLine();
                System.out.printf("'%s' is not a valid input (must be a decimal value (data type double))! Please try again!%n", trash);
            }
        }
    }

    /**
     * A method to validate user input for a double that needs to be within a range.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @param low the lower boundary of the range.
     * @param high the upper boundary of the range.
     * @return a double response.
     */
    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high) {
        while(true) {
            double retDouble = getDouble(pipe, "%s (%s - %s)".formatted(prompt, low, high));
            if(retDouble >= low && retDouble <= high) {
                return retDouble;
            } else {
                System.out.printf("'%s' is outside of the valid range (%s to %s, inclusive)! Please try again!%n", retDouble, low, high);
            }
        }
    }

    /**
     * A method to validate a yes or no user input.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @return a boolean response.
     */
    public static boolean getYNConfirm(Scanner pipe, String prompt) {
        while(true) {
            System.out.printf("%s (Y/N)>> ", prompt);
            String userInput = pipe.nextLine().strip();
            if(yesResponses.contains(userInput.toLowerCase())) {
                return true;
            } else if(noResponses.contains(userInput.toLowerCase())) {
                return false;
            } else {
                System.out.printf("'%s' is not a valid input (must be yes or no)! Please try again!%n", userInput);
            }
        }
    }

    /**
     * A method to validate a user's string via RegEx.
     * @param pipe a Scanner opened to read from System.in.
     * @param prompt prompt for the user.
     * @param regEx the pattern that the user's string is compared against.
     * @return a String response matching a RegEx pattern.
     */
    public static String getRegExString(Scanner pipe, String prompt, String regEx) {
        while(true) {
            System.out.printf("%s>> ", prompt);
            String retString = pipe.nextLine();
            if(retString.matches(regEx)) {
                return retString;
            } else {
                System.out.printf("%s must match the pattern (the pattern is: %s)! Please try again!%n", retString, regEx);
            }
        }
    }

    /**
     * Method to print a header with asterisk formatting, centered text, 60 characters wide.
     * @param msg message at the center of the header.
     */
    public static void prettyHeader(String msg) {
        int padding = (60 - (3*2) - msg.length()) / 2; // Total set width of 60, minus 6 asterisks on each end of the second row, minus message length

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 60; j++) {
                if(i == 0 || i == 2 || j < 3 || j > 56) {
                    System.out.print("*");
                } else if(j >= 3 + padding && j < 3 + padding + msg.length()) {
                    /*
                    * If the current character in the row is past the initial asterisks plus the padding space,
                    * and is still typing out the message in the row (hasn't passed padding plus initial asterisks plus full message)
                    */
                    System.out.print(msg.charAt(j - 3 - padding)); // Print the letter that should be in this spot in the row
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}