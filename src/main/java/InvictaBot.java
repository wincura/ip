// CS2103T Individual Project by William Scott Win A0273291A

// Imports to use collections and handle user input
import java.util.*;

@SuppressWarnings("TextBlockMigration") // Suppress the warning to use text block for logo instead

public class InvictaBot {
    // Declaring data structures to be used by chatbot
    private static ArrayList<String> log = new ArrayList<String>();

    // Method to display logo
    private static void logo() {
        // Logo below generated with the help of an external tool from https://patorjk.com/software/taag/
        String logo =   ".___            .__        __        __________        __\n" +
                "|   | _______  _|__| _____/  |______ \\______   \\ _____/  |_\n" +
                "|   |/    \\  \\/ /  |/ ___\\   __\\__  \\ |    |  _//  _ \\   __\\\n" +
                "|   |   |  \\   /|  \\  \\___|  |  / __ \\|    |   (  <_> )  |\n"  +
                "|___|___|  /\\_/ |__|\\___  >__| (____  /______  /\\____/|__|\n" +
                "         \\/             \\/          \\/       \\/\n";
        System.out.println("Hello from\n" + logo);
    }

    // Method to display greeting message
    private static void greet() {
        System.out.println("_".repeat(70) + "\n" +
                "\t" + "Howdy! I'm InvictaBot!\n" +
                "\t" + "What can I do for ya, pal?\n" +
                "_".repeat(70) + "\n");
    }

    // Method to display goodbye message
    private static void bye() {
        System.out.println("_".repeat(70) + "\n" +
                "\t" + "Bye bye now! You take care!\n" +
                "_".repeat(70) + "\n");
    }

    public static void main(String[] args) {
        // Method calls upon chatbot execution
        logo();
        greet();
        // Uses Scanner to accept user input (planning to use BufferedReader in later releases)
        Scanner s = new Scanner(System.in);
        // Loop to keep chatbot running until bye prompt
        while (true) {
            String userInput = s.nextLine();
            if (userInput.equals("bye")) {
                bye();
                // Exit loop
                break;
            } else if (userInput.equals("list")) {
                int count = 0;
                System.out.println("_".repeat(66));
                for (String msg : log) {
                    count += 1;
                    System.out.println("\t" + count + ". " + msg);
                }
                System.out.println("_".repeat(66) + "\n");
            } else {
                // Display added user input message
                log.add(userInput);
                System.out.println("_".repeat(66) + "\n" +
                        "\t" + "added: " + userInput + "\n" +
                        "_".repeat(66) + "\n");
            }
        }
    }
}
