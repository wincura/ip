// CS2103T Individual Project by William Scott Win A0273291A

// Imports to use collections and handle user input
import java.util.*;

@SuppressWarnings("TextBlockMigration") // Suppress the warning to use text block for logo instead

public class InvictaBot {
    // Declaring data structures to be used by chatbot
    private static ArrayList<Task> taskList = new ArrayList<Task>();

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

    // Method to display message when adding, including count
    private static void added(Task t) {
        System.out.println("_".repeat(66) + "\n" +
                "\t" + "Okay! I've added this task: " + "\n" +
                "\t\t" + t.toString() + "\n" +
                "\tYou've got " + taskList.size() + " tasks in your list now.\n" +
                "_".repeat(66));
    }

    public static void main(String[] args) {
        // Method calls upon chatbot execution
        logo();
        greet();
        // Uses Scanner to accept user input (planning to use BufferedReader in later releases)
        Scanner s = new Scanner(System.in);
        // Loop to keep chatbot running until bye prompt
        label:
        while (true) {
            String raw = s.nextLine();
            String[] userInput = raw.split(" ");
            switch (userInput[0]) {
                case "bye":
                    bye();
                    // Exit loop
                    break label;
                case "list": {
                    int number = 0;
                    System.out.println("_".repeat(66) + "\n" +
                            "\t" + "Here is a list of your tasks: ");
                    for (Task t : taskList) {
                        number += 1;
                        System.out.println("\t" + number + ". " + t.toString());
                    }
                    System.out.println("_".repeat(66) + "\n");
                    break;
                }
                case "mark": {
                    Task t = taskList.get(Integer.parseInt(userInput[1]) - 1);
                    if (t.getStatus()) {
                        System.out.println("_".repeat(66) + "\n" +
                                "\t" + "This task is already marked as done: " + "\n" +
                                "\t\t" + t.toString() + "\n" +
                                "_".repeat(66) + "\n");
                    } else {
                        t.setStatus(true);
                        System.out.println("_".repeat(66) + "\n" +
                                "\t" + "Great! I've marked this as done: " + "\n" +
                                "\t\t" + t.toString() + "\n" +
                                "_".repeat(66) + "\n");
                    }
                    break;
                }
                case "unmark": {
                    Task t = taskList.get(Integer.parseInt(userInput[1]) - 1);
                    if (!t.getStatus()) {
                        System.out.println("_".repeat(66) + "\n" +
                                "\t" + "This task is already marked as not done: " + "\n" +
                                "\t\t" + t.toString() + "\n" +
                                "_".repeat(66) + "\n");
                    } else {
                        t.setStatus(false);
                        System.out.println("_".repeat(66) + "\n" +
                                "\t" + "Great! I've marked this as not done: " + "\n" +
                                "\t\t" + t.toString() + "\n" +
                                "_".repeat(66) + "\n");
                    }
                    break;
                }
                case "event": {
                    StringBuilder taskName = new StringBuilder();
                    StringBuilder eventStartTime = new StringBuilder();
                    StringBuilder eventEndTime = new StringBuilder();
                    boolean taskNameDone = false;
                    boolean eventStartDone = false;
                    // Start counting from index 1 to ignore event command
                    for (int i = 1 ; i < userInput.length ; i++) {
                        String word = userInput[i];
                        if (word.equals("/from")) {
                            taskNameDone = true;
                        } else if (word.equals("/to")) {
                            eventStartDone = true;
                        } else if (taskNameDone && !eventStartDone) {
                            eventStartTime.append(word).append(" ");
                        } else if (eventStartDone) {
                            eventEndTime.append(word).append(" ");
                        } else {
                            taskName.append(word).append(" ");
                        }
                    }
                    Event ev = new Event(taskName.toString().trim(),
                            eventStartTime.toString().trim(),
                            eventEndTime.toString().trim());
                    taskList.add(ev);
                    added(ev);
                    break;
                }
                case "deadline": {
                    StringBuilder taskName = new StringBuilder();
                    StringBuilder deadlineTime = new StringBuilder();
                    boolean taskNameDone = false;
                    // Start counting from index 1 to ignore deadline command
                    for (int i = 1 ; i < userInput.length ; i++) {
                        String word = userInput[i];
                        if (word.equals("/by")) {
                            taskNameDone = true;
                        } else if (taskNameDone) {
                            deadlineTime.append(word).append(" ");
                        } else {
                            taskName.append(word).append(" ");
                        }
                    }
                    Deadline dl = new Deadline(taskName.toString().trim(),
                            deadlineTime.toString().trim());
                    taskList.add(dl);
                    added(dl);
                    break;
                }
                case "todo": {
                    StringBuilder taskName = new StringBuilder();
                    // Start counting from index 1 to ignore todo command
                    for (int i = 1 ; i < userInput.length ; i++) {
                        String word = userInput[i];
                        taskName.append(word).append(" ");
                    }
                    Todo td = new Todo(taskName.toString().trim());
                    taskList.add(td);
                    added(td);
                    break;
                }
                default: {
                    // Display added user input message
                    Task t = new Task(raw);
                    taskList.add(t);
                    added(t);
                    break;
                }
            }
        }
    }
}
