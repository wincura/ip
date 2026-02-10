package invicta.app;

public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String code;

    TaskType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    // Declared a static method to create a Command from a String, throwing an exception if it does not match
    public static TaskType fromString(String code) throws InvictaException {
        for (TaskType c : TaskType.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        throw new InvictaException("Invalid type found in file.");
    }
}

