package invicta.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import invicta.task.Task;

/**
 * Represents the messages displayed by the chsatbot.
 * Handles the formatting of strings orchestrated by Ui class.
 */
public class Message {
    private static final Map<Lang, Map<MessageKey, String>> ioMessages = new HashMap<>();
    private static final Map<Lang, Map<MessageKey, String>> chatbotMessages = new HashMap<>();
    private static final Map<Lang, Map<MessageKey, String>> usageMessages = new HashMap<>();

    private static final int SEPARATOR_WIDTH = 100;
    public static final String SEPARATOR = "_".repeat(SEPARATOR_WIDTH);

    /**
     * Represents the current chosen language. Used to determine which .properties file to obtain
     * message string to be displayed.
     */
    public enum Lang { EN, FR, ES }

    private static Lang currentLang = Lang.EN;

    public static void setLang(Lang lang) {
        currentLang = lang;
    }

    public static Lang getLang() {
        return currentLang;
    }

    static {
        ioMessages.put(Lang.EN, new HashMap<>());
        ioMessages.put(Lang.ES, new HashMap<>());
        ioMessages.put(Lang.FR, new HashMap<>());
        chatbotMessages.put(Lang.EN, new HashMap<>());
        chatbotMessages.put(Lang.FR, new HashMap<>());
        chatbotMessages.put(Lang.ES, new HashMap<>());
        usageMessages.put(Lang.EN, new HashMap<>());
        usageMessages.put(Lang.FR, new HashMap<>());
        usageMessages.put(Lang.ES, new HashMap<>());

        loadInto(ioMessages.get(Lang.EN), "io_en.properties");
        loadInto(ioMessages.get(Lang.FR), "io_fr.properties");
        loadInto(ioMessages.get(Lang.ES), "io_es.properties");

        loadInto(chatbotMessages.get(Lang.EN), "chat_en.properties");
        loadInto(chatbotMessages.get(Lang.FR), "chat_fr.properties");
        loadInto(chatbotMessages.get(Lang.ES), "chat_es.properties");

        loadInto(usageMessages.get(Lang.EN), "usage_en.properties");
        loadInto(usageMessages.get(Lang.FR), "usage_fr.properties");
        loadInto(usageMessages.get(Lang.ES), "usage_es.properties");
    }

    /**
     * Loads message key-string pairings stored in .properties files into the Map data structures
     * of respective languages. Used as a helper function during initialization.
     *
     * @param target Map data structure to load message key-string pairings into.
     * @param resourcePath Path where .properties files are located.
     */
    private static void loadInto(Map<MessageKey, String> target, String resourcePath) {
        Properties props = new Properties();

        try (InputStream in = Message.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Missing resource file: " + resourcePath);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                props.load(br);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load resource file: " + resourcePath, e);
        }

        for (String k : props.stringPropertyNames()) {
            MessageKey key = MessageKey.valueOf(k);
            String v = props.getProperty(k);
            target.put(key, v);
        }
    }

    /**
     * Returns a string containing the corresponding message according to the current set language.
     * Looks up the Map data structures containing message keys mapped with message strings.
     */
    private static String lookup(Map<Lang, Map<MessageKey, String>> store, MessageKey key) {
        String s = store.getOrDefault(currentLang, Map.of()).get(key);
        if (s != null) {
            return s;
        }

        s = store.getOrDefault(Lang.EN, Map.of()).get(key);
        if (s != null) {
            return s;
        }

        throw new IllegalArgumentException("Missing message data for key: " + key + " (" + currentLang + ")");
    }


    /**
     * Returns a string with the corresponding IO message.
     */
    public static String getIoMessage(MessageKey key) {
        return lookup(ioMessages, key);
    }

    /**
     * Returns a string with the corresponding IO message, along with details.
     */
    public static String getIoMessage(MessageKey key, String details) {
        String base = lookup(ioMessages, key);
        return base + " " + String.join(" ", details).trim();
    }

    /**
     * Returns a string wrapped in the chatbot line separator and respective indents.
     */
    private static String formatWrap(String msg) {
        return SEPARATOR + "\n\t" + msg + "\n" + SEPARATOR;
    }

    /**
     * Returns a string with the corresponding chatbot message.
     */
    public static String getChatbotMessage(MessageKey key) {
        return formatWrap(lookup(chatbotMessages, key));
    }

    /**
     * Returns a string with the corresponding chatbot message, along with additional details.
     */
    public static String getChatbotMessage(MessageKey key, String details) {
        return formatWrap(lookup(chatbotMessages, key) + " " + details);
    }

    /**
     * Returns a string with multiple corresponding chatbot messages, and details.
     */
    public static String getChatbotMessage(MessageKey[] keys, String details) {
        StringBuilder toDisplay = new StringBuilder();
        for (MessageKey key : keys) {
            toDisplay.append(lookup(chatbotMessages, key)).append(" ");
        }
        return formatWrap(toDisplay.toString().trim() + " " + details);
    }

    /**
     * Returns a formatted string with the corresponding chatbot message containing details in a
     * specified format (e.g. task details, task list count displayed when adding tasks).
     */
    public static String getChatbotMessageFormatted(MessageKey key, Object... details) {
        String template = lookup(chatbotMessages, key);
        return formatWrap(String.format(template, details));
    }

    /**
     * Returns a string containing labelled items in a list.
     * Used as a helper to handle messages displaying task list or search results.
     */
    public static String buildListMessage(ArrayList<Task> taskList) {
        int number = 0;
        StringBuilder list = new StringBuilder();
        for (Task t : taskList) {
            number += 1;
            list.append("\t").append(number).append(". ").append(t.toString()).append("\n");
        }
        return list.toString();
    }

    /**
     * Returns a string with the corresponding usage message.
     */
    public static String getUsageMessage(MessageKey key) {
        return lookup(usageMessages, key);
    }
}
