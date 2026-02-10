package invicta.app;

/**
 * Represents possible types of tasks.
 */
public enum Type {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String code;

    Type(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /**
     * Returns the respective task type based on string.
     *
     * @param code String (char) within file representing a task
     * @return task type
     * @throws InvictaException if string is not a task type defined in enum.
     */
    // Declared a static method to create a Command from a String, throwing an exception if it does not match
    public static Type fromString(String code) throws InvictaException {
        for (Type c : Type.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        throw new InvictaException("Invalid type found in file.");
    }
}

