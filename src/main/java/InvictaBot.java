// CS2103T Individual Project by William Scott Win A0273291A

// Imports to use collections, handle files and user input
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;


public class InvictaBot {
    // Declaring data structures and file paths to be used by chatbot
    private static ArrayList<Task> taskList = new ArrayList<>();
    private static final String DATA_DIR_PATH = "./data";
    private static final String TASK_LIST_FILE_PATH = "./data/tasklist.txt";

    /**
     * Initializes task list from provided file and display greeting message.
     * If the file does not exist, it is created.
     *
     * @param taskListFile File on which Task List is stored.
     */
    private static void invictaBotInit(File taskListFile) {
        try {
            if (taskListFile.createNewFile()) {
                System.out.println("Task list file created at: " + taskListFile.getAbsolutePath());
            } else {
                System.out.println("Task list file found at: " + taskListFile.getAbsolutePath()
                        + "\nLoading data from file into Invicta...");
                Scanner s = new Scanner(taskListFile);
                while (s.hasNext()) {
                    String[] input = s.nextLine().split(";");
                    try {
                        Type type = Type.fromString(input[0]);
                        switch (type) {
                            case TODO: {
                                boolean isDone = input[1].equals("1");
                                String name = input[2];
                                Todo toAdd = new Todo(name);
                                if (isDone) {
                                    toAdd.setDone(true);
                                }
                                taskList.add(toAdd);
                                break;
                            }
                            case DEADLINE: {
                                boolean isDone = input[1].equals("1");
                                String name = input[2];
                                String deadline = input[3];
                                Deadline toAdd = new Deadline(name, deadline);
                                if (isDone) {
                                    toAdd.setDone(true);
                                }
                                taskList.add(toAdd);
                                break;
                            }
                            case EVENT: {
                                boolean isDone = input[1].equals("1");
                                String name = input[2];
                                String start = input[3];
                                String end = input[4];
                                Event toAdd = new Event(name, start, end);
                                if (isDone) {
                                    toAdd.setDone(true);
                                }
                                taskList.add(toAdd);
                                break;
                            }
                        }
                    } catch (InvictaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println("Task list file data successfully loaded into Invicta.\n\n\n");
            }
        } catch (IOException e) {
            System.out.print("Error occurred while reading file: " + e.getMessage());
            e.printStackTrace();
        }

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
     * Updates task list file upon changes to task list.
     */
    private static void updateTaskListFile() {
        try {
            FileWriter fw = new FileWriter(TASK_LIST_FILE_PATH);
            String toAdd;
            for (Task t : taskList) {
                if (t instanceof Todo) {
                    String[] values = {Type.TODO.getCode(), (t.getDone()) ? "1" : "0", t.getDescription()};
                    toAdd = String.join(";", values);
                    fw.write(System.lineSeparator() + toAdd);
                } else if (t instanceof Deadline) {
                    String[] values = {Type.DEADLINE.getCode(), (t.getDone()) ? "1" : "0", t.getDescription(),
                            ((Deadline) t).getDeadline()};
                    toAdd = String.join(";", values);
                    fw.write(System.lineSeparator() + toAdd);
                } else if (t instanceof Event) {
                    String[] values = {Type.DEADLINE.getCode(), (t.getDone()) ? "1" : "0", t.getDescription(),
                            ((Event) t).getStart(), ((Event) t).getEnd()};
                    toAdd = String.join(";", values);
                    fw.write(System.lineSeparator() + toAdd);
                }
            }
            fw.close();
        } catch (IOException e) {
            System.out.print("Error occurred while writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays goodbye message with username.
     */
    private static void bye(String username) {
        System.out.println("_".repeat(100)
                + "\n\tBye bye now! You take care, " + username + "!\n"
                + "_".repeat(100));
    }

    /**
     * Displays message when adding tasks, including count.
     */
    private static void added(Task t) {
        System.out.println("_".repeat(100)
                + "\n\tOkay! I've added this task: \n\t\t" + t.toString()
                + "\n\tYou've got " + taskList.size() + " tasks in your list now.\n"
                + "_".repeat(100));
    }

    public static void main(String[] args) {
        // Method to initialize chatbot upon execution
        File dataDir = new File(DATA_DIR_PATH);
        if (!dataDir.exists()) {
            if (dataDir.mkdir()) {
                System.out.println("Data directory created at: " + dataDir.getAbsolutePath());
            } else {
                System.out.println("Failed to create new directory.");
            }
        } else {
            System.out.println("Data directory found at: " + dataDir.getAbsolutePath());
        }
        File taskListFile = new File(TASK_LIST_FILE_PATH);

        invictaBotInit(taskListFile);

        // Loop to prompt user for username input until valid
        System.out.println("_".repeat(100)
                + "\n\tHowdy! I'm InvictaBot!\n\tHow might I address you, pal?\n"
                + "_".repeat(100));
        String username;
        Scanner s = new Scanner(System.in);
        while (true) {
            try {
                // Obtaining user's name, with validation to handle empty names
                username = s.nextLine().trim();
                if (username.isEmpty()) {
                    throw new InvictaException("Surely you're not a nameless person! Come again?");
                } else {
                    // Exit loop and continue to chatbot program
                    System.out.println("_".repeat(100)
                            + "\n\tIt's a pleasure, " + username + "! What can I do you for?\n"
                            + "_".repeat(100));
                    break;
                }
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }

        // Loop to keep chatbot running until bye prompt
        appLoop:
        while (true) {
            try {
                String raw = s.nextLine().trim();
                String[] userInput = raw.split(" ");
                if (raw.isEmpty()) {
                    throw new InvictaException("_".repeat(100)
                            + "\n\tWhat? Did you say something? Type a message!\n"
                            + "_".repeat(100));
                } else {
                    Command c = Command.fromString(userInput[0]);
                    switch (c) {
                        case BYE: {
                            bye(username);
                            // Exit loop
                            break appLoop;
                        }
                        case HELP: {
                            System.out.println("_".repeat(100)
                                    + "\n\tList of commands in InvictaBot:\n"
                                    + "\tbye - exit app\n"
                                    + "\tlist - display task list\n"
                                    + "\tdelete - delete the task\n"
                                    + "\tmark <index> - mark task as done\n"
                                    + "\tunmark <index> - mask task as not done\n"
                                    + "\ttodo <name> - add a to-do task\n"
                                    + "\tdeadline <name> /by <deadline> - add a deadline task\n"
                                    + "\tevent <name> /from <start> /to <end> - add an event\n"
                                    + "_".repeat(100));
                            break;
                        }
                        case LIST: {
                            int number = 0;
                            if (taskList.isEmpty()) {
                                System.out.println("_".repeat(100)
                                        + "\n\tYour task list is empty! Add a few tasks!\n"
                                        + "_".repeat(100));
                            } else {
                                System.out.println("_".repeat(100)
                                        + "\n\tHere is a list of your tasks: ");
                                for (Task t : taskList) {
                                    number += 1;
                                    System.out.println("\t" + number + ". " + t.toString());
                                }
                                System.out.println("_".repeat(100));
                            }
                            break;
                        }
                        case DELETE: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tPlease provide an index for this command. (usage: delete <number>)\n"
                                        + "_".repeat(100));
                            } else {
                                int index = Integer.parseInt(userInput[1]) - 1;
                                if (index < 0 | index > taskList.size() - 1) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tYou want me to do what? "
                                            + "Put a valid index! (check task list using 'list' command)\n"
                                            + "_".repeat(100));
                                } else {
                                    Task temp = taskList.get(index);
                                    String deleteTask = temp.toString();
                                    taskList.remove(index);
                                    updateTaskListFile();
                                    System.out.println("_".repeat(100)
                                            + "\n\tInto the trash! This task has been deleted: \n"
                                            + "\t\t" + deleteTask + "\n\tYou've got " + taskList.size()
                                            + " tasks in your list now.\n"
                                            + "_".repeat(100));
                                }
                            }
                            break;
                        }
                        case MARK: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tPlease provide an index for this command. (usage: mark <number>)\n"
                                        + "_".repeat(100));
                            } else {
                                int index = Integer.parseInt(userInput[1]) - 1;
                                if (index < 0 | index > taskList.size() - 1) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                                            + "_".repeat(100));
                                } else {
                                    Task t = taskList.get(index);
                                    if (t.getDone()) {
                                        System.out.println("_".repeat(100)
                                                + "\n\tThis task is already marked as done: " + "\n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    } else {
                                        t.setDone(true);
                                        updateTaskListFile();
                                        System.out.println("_".repeat(100)
                                                + "\n\tGreat! I've marked this as done:  \n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    }
                                }
                            }
                            break;
                        }
                        case UNMARK: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tPlease provide an index for this command. (usage: mark <number>)\n"
                                        + "_".repeat(100));
                            } else {
                                int index = Integer.parseInt(userInput[1]) - 1;
                                if (index < 0 | index > taskList.size() - 1) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                                            + "_".repeat(100));
                                } else {
                                    Task t = taskList.get(index);
                                    if (!t.getDone()) {
                                        System.out.println("_".repeat(100)
                                                + "\n\tThis task is already marked as not done: \n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    } else {
                                        t.setDone(false);
                                        updateTaskListFile();
                                        System.out.println("_".repeat(100)
                                                + "\n\tOh I see! I've marked this as not done: \n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    }
                                }
                            }
                            break;
                        }
                        case EVENT: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing task name, start time and end time! (usage: event <name> /from <start> /to <end>)\n"
                                        + "_".repeat(100));
                            } else {
                                StringBuilder taskName = new StringBuilder();
                                StringBuilder eventStartTime = new StringBuilder();
                                StringBuilder eventEndTime = new StringBuilder();
                                // Flags to mark where one argument ends and another begins, and when to disregard unnecessary arguments
                                boolean taskNameDone = false;
                                boolean eventStartDone = false;
                                int argsDoneFlag = 2;
                                // Start counting from index 1 to ignore event command
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    if (word.equals("/from")) {
                                        taskNameDone = true;
                                        argsDoneFlag -= 1;
                                        if (argsDoneFlag < 0) {
                                            break;
                                        }
                                    } else if (word.equals("/to")) {
                                        eventStartDone = true;
                                        argsDoneFlag -= 1;
                                        if (argsDoneFlag < 0) {
                                            break;
                                        }
                                    } else if (taskNameDone && !eventStartDone) {
                                        eventStartTime.append(word).append(" ");
                                    } else if (eventStartDone) {
                                        eventEndTime.append(word).append(" ");
                                    } else {
                                        taskName.append(word).append(" ");
                                    }
                                }
                                if (eventStartTime.isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing start time and end time! (usage: event <name> /from <start> /to <end>)\n"
                                            + "_".repeat(100));
                                } else if (eventEndTime.isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing end time! (usage: event <name> /from <start> /to <end>)" + "\n"
                                            + "_".repeat(100));
                                } else {
                                    Event ev = new Event(taskName.toString().trim(),
                                            eventStartTime.toString().trim(),
                                            eventEndTime.toString().trim());
                                    taskList.add(ev);
                                    updateTaskListFile();
                                    added(ev);
                                }
                            }
                            break;
                        }
                        case DEADLINE: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing task name and deadline! (usage: deadline <name> /by <deadline>)\n"
                                        + "_".repeat(100));
                            } else {
                                StringBuilder taskName = new StringBuilder();
                                StringBuilder deadlineTime = new StringBuilder();
                                // Flags to mark where one argument ends and another begins, and when to disregard unnecessary arguments
                                boolean taskNameDone = false;
                                int argsDoneFlag = 1;
                                // Start counting from index 1 to ignore deadline command
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    if (word.equals("/by")) {
                                        taskNameDone = true;
                                        argsDoneFlag -= 1;
                                        if (argsDoneFlag < 0) {
                                            break;
                                        }
                                    } else if (taskNameDone) {
                                        deadlineTime.append(word).append(" ");
                                    } else {
                                        taskName.append(word).append(" ");
                                    }
                                }
                                if (deadlineTime.isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing deadline! (usage: deadline <name> /by <deadline>)\n"
                                            + "_".repeat(100));
                                } else {
                                    Deadline dl = new Deadline(taskName.toString().trim(),
                                            deadlineTime.toString().trim());
                                    taskList.add(dl);
                                    updateTaskListFile();
                                    added(dl);
                                }
                            }
                            break;
                        }
                        case TODO: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing task name! (usage: todo <name>)\n"
                                        + "_".repeat(100));
                            } else {
                                StringBuilder taskName = new StringBuilder();
                                // Start counting from index 1 to ignore todo command
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    taskName.append(word).append(" ");
                                }
                                Todo td = new Todo(taskName.toString().trim());
                                taskList.add(td);
                                updateTaskListFile();
                                added(td);
                            }
                            break;
                        }
                        default: {
                            bye(username);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("_".repeat(100)
                        + "\n\tAn index is a number, so go put one! (usage: mark/unmark <int as index>)\n"
                        + "_".repeat(100));
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
