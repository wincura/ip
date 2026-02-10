package invicta.command;

// Import to handle chatbot-specific exceptions
import invicta.app.InvictaException;

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

    // Declared a static method to create a Command from a String, throwing an exception if it does not match
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

