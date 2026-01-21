// CS2103T Individual Project by William Scott Win A0273291A
@SuppressWarnings("TextBlockMigration") // Suppress the warning to use text block for logo instead

public class InvictaBot {
    // Method to display logo
    public static void logo() {
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
    public static void greet() {
        System.out.println("_".repeat(70) + "\n" +
                "Hello! I'm InvictaBot!\n" +
                "What can I do for you?\n" +
                "_".repeat(70) + "\n" +
                "Bye. Hope to see you again soon!\n" +
                "_".repeat(70) + "\n");
    }

    public static void main(String[] args) {
        logo();
        greet();
    }
}
