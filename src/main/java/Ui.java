import java.io.IOException;

public class Ui {
    private String username;

    public Ui() {
        username = "";
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Displays chatbot logo.
     */
    public void logo() {
        // Logo below generated with the help of an external tool from https://patorjk.com/software/taag/
        String logo =   ".___            .__        __        __________        __\n"
                + "|   | _______  _|__| _____/  |______ \\______   \\ _____/  |_\n"
                + "|   |/    \\  \\/ /  |/ ___\\   __\\__  \\ |    |  _//  _ \\   __\\\n"
                + "|   |   |  \\   /|  \\  \\___|  |  / __ \\|    |   (  <_> )  |\n"
                + "|___|___|  /\\_/ |__|\\___  >__| (____  /______  /\\____/|__|\n"
                + "         \\/             \\/          \\/       \\/\n";
        System.out.println("Hello from\n" + logo);
    }

    /**
     * Displays goodbye message with username.
     */
    public void bye() {
        System.out.println("_".repeat(100)
                + "\n\tBye bye now! You take care, " + this.username + "!\n"
                + "_".repeat(100));
    }

    /**
     * Displays message when adding tasks, including count.
     */
    public void added(Task t, TaskList taskList) {
        System.out.println("_".repeat(100)
                + "\n\tOkay! I've added this task: \n\t\t" + t.toString()
                + "\n\tYou've got " + taskList.getSize() + " tasks in your list now.\n"
                + "_".repeat(100));
    }

    /**
     * Displays error message for taskbot specific errors occurring during operations.
     */
    public void showException (InvictaException e) {
        System.out.println(e.getMessage());
    }

    /**
     * Displays error message for error occurring while reading from file.
     */
    public void showLoadingError(IOException e) {
        System.out.print("Error occurred while reading file: " + e.getMessage());
        e.printStackTrace();
    }
}
