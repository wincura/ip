package invicta.app;

import invicta.task.Task;
import invicta.task.TaskList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

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
     *
     * @param t Task whose details are to be printed.
     * @param taskList Task list whose count are to be printed.
     */
    public void added(Task t, TaskList taskList) {
        System.out.println("_".repeat(100)
                + "\n\tOkay! I've added this task: \n\t\t" + t.toString()
                + "\n\tYou've got " + taskList.getSize() + " tasks in your list now.\n"
                + "_".repeat(100));
    }

    public void marked(int i, String task) {
        switch (i) {
            case 1: {
                System.out.println("_".repeat(100)
                        + "\n\tGreat! I've marked this as done:  \n\t\t" + task + "\n"
                        + "_".repeat(100));
                break;
            }
            case 2: {
                System.out.println("_".repeat(100)
                        + "\n\tThis task is already marked as done: " + "\n\t\t" + task + "\n"
                        + "_".repeat(100));
                break;
            }
            case 3: {
                System.out.println("_".repeat(100)
                        + "\n\tOh I see! I've marked this as not done: \n\t\t" + task + "\n"
                        + "_".repeat(100));
                break;
            }
            case 4: {
                System.out.println("_".repeat(100)
                        + "\n\tThis task is already marked as not done: \n\t\t" + task + "\n"
                        + "_".repeat(100));
                break;
            }
        }
    }

    /**
     * Displays message indicating an empty task list.
     */
    public void empty() {
        System.out.println("_".repeat(100)
                + "\n\tYour task list is empty! Add a few tasks!\n"
                + "_".repeat(100));
    }

    /**
     * Displays all the tasks in task list, along with their details.
     *
     * @param taskList Provided task list to print out.
     */
    public void printAll(TaskList taskList) {
        System.out.println("_".repeat(100)
                + "\n\tHere is a list of your tasks: ");
        taskList.printTasks();
        System.out.println("_".repeat(100));
    }

    /**
     * Displays the tasks that fall on a specified date, along with their details.
     *
     * @param taskList Provided list of found tasks to be printed.
     * @param dateToSearch Date used to find the tasks to be printed.
     */
    public void printFound(ArrayList<Task> taskList, LocalDate dateToSearch) {
        int number = 0;
        System.out.println("_".repeat(100)
                + "\n\tHere is a list of your tasks that you have on "
                + dateToSearch.format(Parser.dateDisplay) + ": ");
        for (Task t : taskList) {
            number += 1;
            System.out.println("\t" + number + ". " + t.toString());
        }
        System.out.println("_".repeat(100));
    }

    /**
     * Displays the tasks that fall within a specified period, along with their details.
     *
     * @param taskList Provided list of found tasks to be printed.
     * @param periodStartTime Period start time used to find the tasks to be printed.
     * @param periodEndTime Period end time used to find the tasks to be printed.
     */
    public void printFound(ArrayList<Task> taskList, LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        int number = 0;
        if (taskList.isEmpty()) {
            System.out.println("_".repeat(100)
                    + "\n\tThere are no events in that period. Wanna add a task?\n"
                    + "_".repeat(100));
        } else {
            System.out.println("_".repeat(100)
                    + "\n\tHere is a list of your tasks that fall within "
                    + periodStartTime.format(Parser.dateDisplay) + " to " + periodEndTime.format(Parser.dateDisplay) + ": ");
            for (Task t : taskList) {
                number += 1;
                System.out.println("\t" + number + ". " + t.toString());
            }
            System.out.println("_".repeat(100));

        }
    }

    /**
     * Reads user input for username and loop until valid input.
     *
     * @param s Scanner used to read user input for username.
     */
    public void readUsername(Scanner s) {
        System.out.println("_".repeat(100)
                + "\n\tHowdy! I'm InvictaBot!\n\tHow might I address you, pal?\n"
                + "_".repeat(100));
        String username;
        while (true) {
            try {
                // Obtaining user's name, with validation to handle empty names
                username = s.nextLine().trim();
                this.setUsername(username);
                if (username.isEmpty()) {
                    throw new InvictaException("\tSurely you're not a nameless person! Come again?");
                } else {
                    // Exit loop and continue to chatbot program
                    System.out.println("_".repeat(100)
                            + "\n\tIt's a pleasure, " + this.getUsername() + "! What can I do you for?\n"
                            + "_".repeat(100));
                    break;
                }
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Reads user input from scanner into a string array.
     *
     * @param s scanner object with raw user input.
     * @return userInput string array containing user input split by spaces.
     */
    public String[] readCommand(Scanner s) throws InvictaException {
        String raw = s.nextLine().trim();
        String[] userInput = raw.split(" ");
        if (raw.isEmpty()) {
            throw new InvictaException("_".repeat(100)
                    + "\n\tWhat? Did you say something? Type a message!\n"
                    + "_".repeat(100));
        } else {
            return userInput;
        }
    }

    /**
     * Displays error message for taskbot specific errors occurring during operations.
     */
    public void showException (Exception e) {
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
