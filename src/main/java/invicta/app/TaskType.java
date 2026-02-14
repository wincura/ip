package invicta.app;

import invicta.app.Message.MessageKey;

/**
 * Represents possible types of tasks.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String code;

    private TaskType(String code) {
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
    public static TaskType fromString(String code) throws InvictaException {
        for (TaskType c : TaskType.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        throw new InvictaException(Message.getIoMessage(MessageKey.FILE_INVALID_TYPE));
    }
}

