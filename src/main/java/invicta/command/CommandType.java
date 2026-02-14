package invicta.command;

// Import to handle chatbot-specific exceptions
import invicta.app.InvictaException;
import invicta.app.Message;
import invicta.app.MessageKey;

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
    FIND("find"),
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
        throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_COMMAND,
                Message.getUsageMessage(MessageKey.TYPE_HELP)));
    }
}

