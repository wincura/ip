public enum Command {
    BYE("bye"),
    LIST("list"),
    DELETE("delete"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MONTH("month"),
    DAY("day"),
    HELP("help");

    private final String word;

    Command(String word) {
        this.word = word;
    }

    // Declared a static method to create a Command from a String, throwing an exception if it does not match
    public static Command fromString(String word) throws InvictaException {
        for (Command c : Command.values()) {
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

