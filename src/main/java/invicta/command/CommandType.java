package invicta.command;

// Import to handle chatbot-specific exceptions
import invicta.app.InvictaException;

/**
 * Represents possible types of commands.
 */
public enum CommandType {
    BYE("bye"),
    LIST("list"),
    DELETE("delete"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DAY("day"),
    PERIOD("period"),
    HELP("help");

    private final String word;

    CommandType(String word) {
        this.word = word;
    }

    /**
     * Returns the respective command type based on string.
     *
     * @param word user input string for command
     * @return command type
     * @throws InvictaException if string is not a command type defined in enum.
     */
    public static CommandType fromString(String word) throws InvictaException {
        for (CommandType c : CommandType.values()) {
            if (c.word.equals(word)) {
                return c;
            }
        }
        throw new InvictaException("_".repeat(100)
                + "\n\tWhat are you talking about? I do not understand: "
                + word + "\n\tType 'help' for a list of commands and their usage.\n"
                + "_".repeat(100));
    }
}

