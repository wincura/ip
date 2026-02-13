package invicta.app;

import invicta.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Message {
    public enum Lang { EN, FR }

    private static Lang currentLang = Lang.EN;

    public static void setLang(Lang lang) {
        currentLang = lang;
    }

    public static Lang getLang() {
        return currentLang;
    }

    public enum MessageKey {
        // FILE / IO
        DIRECTORY_CREATED,
        DIRECTORY_CREATE_FAILED,
        DIRECTORY_FOUND,
        FILE_CREATED,
        FILE_FOUND,
        FILE_LOADING,
        FILE_LOADED,
        FILE_INVALID_TYPE,
        FILE_IO_ERROR,

        // HELP / GENERAL
        TYPE_HELP,
        DISPLAY_HELP,

        // INPUT / COMMAND ERRORS
        INVALID_COMMAND,
        INVALID_DATE_TIME,
        INVALID_INDEX,

        MISSING_USERNAME,
        MISSING_INPUT,
        MISSING_INDEX,
        MISSING_NAME,
        MISSING_DEADLINE,
        MISSING_EVENT_START,
        MISSING_EVENT_END,
        MISSING_STRING,
        MISSING_DAY,
        MISSING_PERIOD_START,
        MISSING_PERIOD_END,

        // USAGE MESSAGES
        TODO_USAGE,
        DEADLINE_USAGE,
        EVENT_USAGE,
        MARK_USAGE,
        UNMARK_USAGE,
        DELETE_USAGE,
        FIND_USAGE,
        PERIOD_USAGE,
        DAY_USAGE,
        LIST_USAGE,

        // CHATBOT RESPONSES
        PROMPT_USERNAME,
        PROMPT_COMMAND,

        BYE,
        ADDED_TASK,
        DELETED_TASK,

        MARKED_DONE,
        MARKED_NOT_DONE,
        MARKED_DONE_ALREADY,
        MARKED_NOT_DONE_ALREADY,

        // TASK DISPLAY
        DISPLAY_TASK_LIST,
        DISPLAY_TASK_LIST_FIND,
        DISPLAY_TASK_LIST_PERIOD,
        DISPLAY_TASK_LIST_DAY,

        EMPTY_TASK_LIST,
        EMPTY_PERIOD,
        EMPTY_DAY,
        EMPTY_FIND
    }

    private static final int SEPARATOR_WIDTH = 100;
    public static final String SEPARATOR = "_".repeat(SEPARATOR_WIDTH);

    private static final Map<Lang, Map<MessageKey, String>> chatbotMessages = new HashMap<>();
    private static final Map<Lang, Map<MessageKey, String>> usageMessages = new HashMap<>();
    private static final Map<Lang, Map<MessageKey, String>> ioMessages = new HashMap<>();

    static {
        ioMessages.put(Lang.EN, new HashMap<>());
        ioMessages.put(Lang.FR, new HashMap<>());
        usageMessages.put(Lang.EN, new HashMap<>());
        usageMessages.put(Lang.FR, new HashMap<>());
        chatbotMessages.put(Lang.EN, new HashMap<>());
        chatbotMessages.put(Lang.FR, new HashMap<>());

        Map<MessageKey, String> ioEn = ioMessages.get(Lang.EN);
        Map<MessageKey, String> ioFr = ioMessages.get(Lang.FR);
        Map<MessageKey, String> usageEn = usageMessages.get(Lang.EN);
        Map<MessageKey, String> usageFr = usageMessages.get(Lang.FR);
        Map<MessageKey, String> chatEn = chatbotMessages.get(Lang.EN);
        Map<MessageKey, String> chatFr = chatbotMessages.get(Lang.FR);

        // IO MESSAGES
        ioEn.put(MessageKey.DIRECTORY_CREATED, "Data directory created at: ");
        ioEn.put(MessageKey.DIRECTORY_CREATE_FAILED, "Failed to create data directory at: ");
        ioEn.put(MessageKey.DIRECTORY_FOUND, "Data directory found at: ");
        ioEn.put(MessageKey.FILE_CREATED, "Task list file created at: ");
        ioEn.put(MessageKey.FILE_FOUND, "Task list file found at: ");
        ioEn.put(MessageKey.FILE_LOADING, "Loading data from file into Invicta...");
        ioEn.put(MessageKey.FILE_LOADED, "Task list file data successfully loaded into Invicta.");
        ioEn.put(MessageKey.FILE_INVALID_TYPE, "Invalid task type found in file!");
        ioEn.put(MessageKey.FILE_IO_ERROR, "Error occurred while reading or writing to file: ");

        // IO MESSAGES (FR)
        ioFr.put(MessageKey.DIRECTORY_CREATED, "Dossier de données créé à : ");
        ioFr.put(MessageKey.DIRECTORY_CREATE_FAILED, "Échec de la création du dossier de données à : ");
        ioFr.put(MessageKey.DIRECTORY_FOUND, "Dossier de données trouvé à : ");
        ioFr.put(MessageKey.FILE_CREATED, "Fichier de tâches créé à : ");
        ioFr.put(MessageKey.FILE_FOUND, "Fichier de tâches trouvé à : ");
        ioFr.put(MessageKey.FILE_LOADING, "Chargement des données du fichier dans Invicta...");
        ioFr.put(MessageKey.FILE_LOADED, "Les données du fichier ont été chargées avec succès dans Invicta.");
        ioFr.put(MessageKey.FILE_INVALID_TYPE, "Type de tâche invalide trouvé dans le fichier !");
        ioFr.put(MessageKey.FILE_IO_ERROR, "Erreur lors de la lecture ou de l’écriture du fichier : ");

        // USAGE MESSAGES
        usageEn.put(MessageKey.TODO_USAGE, "(usage: todo <name>)");
        usageEn.put(MessageKey.DEADLINE_USAGE, "(usage: deadline <name> /by <deadline>)");
        usageEn.put(MessageKey.DELETE_USAGE, "(usage: delete <number>)");
        usageEn.put(MessageKey.DAY_USAGE, "(usage: day <date>)");
        usageEn.put(MessageKey.EVENT_USAGE, "(usage: event <name> /from <start> /to <end>)");
        usageEn.put(MessageKey.FIND_USAGE, "(usage: find <search string>)");
        usageEn.put(MessageKey.MARK_USAGE, "(usage: mark <number>)");
        usageEn.put(MessageKey.PERIOD_USAGE, "(usage: period /from <start> /to <end>)");
        usageEn.put(MessageKey.UNMARK_USAGE, "(usage: unmark <number>)");
        usageEn.put(MessageKey.LIST_USAGE, "(check task list using 'list' command)");

        usageEn.put(MessageKey.TYPE_HELP,
                "Type 'help' for a list of commands, their usages and acceptable date time formats.");

        // USAGE MESSAGES (FR)
        usageFr.put(MessageKey.TODO_USAGE, "(utilisation : todo <nom>)");
        usageFr.put(MessageKey.DEADLINE_USAGE, "(utilisation : deadline <nom> /by <date_limite>)");
        usageFr.put(MessageKey.DELETE_USAGE, "(utilisation : delete <numéro>)");
        usageFr.put(MessageKey.DAY_USAGE, "(utilisation : day <date>)");
        usageFr.put(MessageKey.EVENT_USAGE, "(utilisation : event <nom> /from <début> /to <fin>)");
        usageFr.put(MessageKey.FIND_USAGE, "(utilisation : find <texte_recherché>)");
        usageFr.put(MessageKey.MARK_USAGE, "(utilisation : mark <numéro>)");
        usageFr.put(MessageKey.PERIOD_USAGE, "(utilisation : period /from <début> /to <fin>)");
        usageFr.put(MessageKey.UNMARK_USAGE, "(utilisation : unmark <numéro>)");
        usageFr.put(MessageKey.LIST_USAGE, "(consultez la liste des tâches avec la commande 'list')");

        usageFr.put(MessageKey.TYPE_HELP,
                "Tapez 'help' pour voir la liste des commandes, leurs utilisations et les formats de date et d’heure acceptés.");

        // CHATBOT MESSAGES
        // Help text
        chatEn.put(MessageKey.DISPLAY_HELP, """
        List of commands in InvictaBot:
        \tbye - exit app
        \tlist - display task list
        \tdelete - delete the task
        \tmark <index> - mark task as done
        \tunmark <index> - mark task as not done
        \ttodo <name> - add a to-do task
        \tdeadline <name> /by <deadline> - add a deadline task
        \tevent <name> /from <start> /to <end> - add an event
        \tfind <search string> - display tasks containing search string in task descriptions
        \tday <date> - display tasks on date
        \tperiod /from <start> /to <end> - display tasks within period
        
        \tList of available date time formats:
        \tyyyy-MM-dd
        \tyyyy-MM-dd HH:mm""");

        // Help text (FR)
        chatFr.put(MessageKey.DISPLAY_HELP, """
        Liste des commandes dans InvictaBot :
        \tbye - quitter l’application
        \tlist - afficher la liste des tâches
        \tdelete - supprimer une tâche
        \tmark <index> - marquer une tâche comme terminée
        \tunmark <index> - marquer une tâche comme non terminée
        \ttodo <nom> - ajouter une tâche simple
        \tdeadline <nom> /by <date_limite> - ajouter une tâche avec échéance
        \tevent <nom> /from <début> /to <fin> - ajouter un événement
        \tfind <texte_recherché> - afficher les tâches contenant le texte recherché
        \tday <date> - afficher les tâches à une date donnée
        \tperiod /from <début> /to <fin> - afficher les tâches dans une période donnée
        
        \tFormats de date et d’heure disponibles :
        \tyyyy-MM-dd
        \tyyyy-MM-dd HH:mm""");


        // Errors / missing fields
        chatEn.put(MessageKey.INVALID_COMMAND, "What are you talking about? I do not understand!");
        chatEn.put(MessageKey.INVALID_DATE_TIME, "Invalid date time format!");
        chatEn.put(MessageKey.INVALID_INDEX, "You want me to do what? Put a valid index!");

        chatEn.put(MessageKey.MISSING_USERNAME, "Surely you're not a nameless person! Come again?");
        chatEn.put(MessageKey.MISSING_INPUT, "What? Did you say something? Type a message!");
        chatEn.put(MessageKey.MISSING_INDEX, "Please provide an index for this command.");
        chatEn.put(MessageKey.MISSING_NAME, "Missing task name for task!");
        chatEn.put(MessageKey.MISSING_DEADLINE, "Missing deadline date for task!");
        chatEn.put(MessageKey.MISSING_EVENT_START, "Missing start date for event!");
        chatEn.put(MessageKey.MISSING_EVENT_END, "Missing end date for event!");
        chatEn.put(MessageKey.MISSING_STRING, "Missing string to search for!");
        chatEn.put(MessageKey.MISSING_DAY, "Missing date to search for!");
        chatEn.put(MessageKey.MISSING_PERIOD_START, "Missing start date for period!");
        chatEn.put(MessageKey.MISSING_PERIOD_END, "Missing end date for period!");

        // Errors / missing fields (FR)
        chatFr.put(MessageKey.INVALID_COMMAND, "De quoi parlez-vous ? Je ne comprends pas !");
        chatFr.put(MessageKey.INVALID_DATE_TIME, "Format de date et d’heure invalide !");
        chatFr.put(MessageKey.INVALID_INDEX, "Que voulez-vous faire ? Veuillez entrer un index valide !");

        chatFr.put(MessageKey.MISSING_USERNAME, "Vous n’allez tout de même pas rester sans nom ! Réessayez ?");
        chatFr.put(MessageKey.MISSING_INPUT, "Quoi ? Vous avez dit quelque chose ? Tapez un message !");
        chatFr.put(MessageKey.MISSING_INDEX, "Veuillez fournir un index pour cette commande.");
        chatFr.put(MessageKey.MISSING_NAME, "Nom de tâche manquant !");
        chatFr.put(MessageKey.MISSING_DEADLINE, "Date limite manquante pour la tâche !");
        chatFr.put(MessageKey.MISSING_EVENT_START, "Date de début manquante pour l’événement !");
        chatFr.put(MessageKey.MISSING_EVENT_END, "Date de fin manquante pour l’événement !");
        chatFr.put(MessageKey.MISSING_STRING, "Texte à rechercher manquant !");
        chatFr.put(MessageKey.MISSING_DAY, "Date manquante pour la recherche !");
        chatFr.put(MessageKey.MISSING_PERIOD_START, "Date de début manquante pour la période !");
        chatFr.put(MessageKey.MISSING_PERIOD_END, "Date de fin manquante pour la période !");

        // Empty states
        chatEn.put(MessageKey.EMPTY_TASK_LIST, "Your task list is empty! Add a few tasks!");
        chatEn.put(MessageKey.EMPTY_PERIOD, "There are no tasks in that period.");
        chatEn.put(MessageKey.EMPTY_DAY, "There are no tasks on that date.");
        chatEn.put(MessageKey.EMPTY_FIND, "There are no matching tasks.");

        // Empty states (FR)
        chatFr.put(MessageKey.EMPTY_TASK_LIST, "Votre liste de tâches est vide ! Ajoutez-en quelques-unes !");
        chatFr.put(MessageKey.EMPTY_PERIOD, "Aucune tâche dans cette période.");
        chatFr.put(MessageKey.EMPTY_DAY, "Aucune tâche à cette date.");
        chatFr.put(MessageKey.EMPTY_FIND, "Aucune tâche correspondante trouvée.");

        // Prompts
        chatEn.put(MessageKey.PROMPT_USERNAME, "Howdy! I'm InvictaBot! How might I address you, pal?");
        chatEn.put(MessageKey.PROMPT_COMMAND, "It's a pleasure, %s! What can I do you for?");

        // Prompts (FR)
        chatFr.put(MessageKey.PROMPT_USERNAME, "Salut ! Je suis InvictaBot ! Comment dois-je vous appeler ?");
        chatFr.put(MessageKey.PROMPT_COMMAND, "Ravi de vous rencontrer, %s ! Que puis-je faire pour vous ?");

        // Normal responses
        chatEn.put(MessageKey.BYE, "Bye bye now! You take care, %s!");
        chatEn.put(MessageKey.ADDED_TASK, "Okay! I've added this task:\n\t\t%s\n\tYou've got %s tasks in your list now.");
        chatEn.put(MessageKey.DELETED_TASK, "Into the trash! This task has been deleted:\n\t\t%s\n\tYou've got %s tasks in your list now.");
        chatEn.put(MessageKey.MARKED_DONE, "Great! I've marked this as done:\n\t\t%s");
        chatEn.put(MessageKey.MARKED_DONE_ALREADY, "This task is already marked as done:\n\t\t%s");
        chatEn.put(MessageKey.MARKED_NOT_DONE, "Oh I see! I've marked this as not done:\n\t\t%s");
        chatEn.put(MessageKey.MARKED_NOT_DONE_ALREADY, "This task is already marked as not done:\n\t\t%s");

        // Normal responses (FR)
        chatFr.put(MessageKey.BYE, "Au revoir ! Prenez soin de vous, %s !");
        chatFr.put(MessageKey.ADDED_TASK,
                "D’accord ! J’ai ajouté cette tâche :\n\t\t%s\n\tVous avez maintenant %s tâches dans votre liste.");
        chatFr.put(MessageKey.DELETED_TASK,
                "À la poubelle ! Cette tâche a été supprimée :\n\t\t%s\n\tVous avez maintenant %s tâches dans votre liste.");
        chatFr.put(MessageKey.MARKED_DONE,
                "Parfait ! J’ai marqué cette tâche comme terminée :\n\t\t%s");
        chatFr.put(MessageKey.MARKED_DONE_ALREADY,
                "Cette tâche est déjà marquée comme terminée :\n\t\t%s");
        chatFr.put(MessageKey.MARKED_NOT_DONE,
                "Très bien ! J’ai marqué cette tâche comme non terminée :\n\t\t%s");
        chatFr.put(MessageKey.MARKED_NOT_DONE_ALREADY,
                "Cette tâche est déjà marquée comme non terminée :\n\t\t%s");

        // Display headers
        chatEn.put(MessageKey.DISPLAY_TASK_LIST, "Here is a list of your tasks:\n%s");
        chatEn.put(MessageKey.DISPLAY_TASK_LIST_DAY, "Here is a list of your tasks that you have on %s:\n%s");
        chatEn.put(MessageKey.DISPLAY_TASK_LIST_FIND, "Here is a list of your tasks that contains '%s':\n%s");
        chatEn.put(MessageKey.DISPLAY_TASK_LIST_PERIOD, "Here is a list of your tasks that fall within %s to %s:\n%s");

        // Display headers (FR)
        chatFr.put(MessageKey.DISPLAY_TASK_LIST,
                "Voici la liste de vos tâches :\n%s");
        chatFr.put(MessageKey.DISPLAY_TASK_LIST_DAY,
                "Voici la liste de vos tâches pour le %s :\n%s");
        chatFr.put(MessageKey.DISPLAY_TASK_LIST_FIND,
                "Voici les tâches contenant \"%s\" :\n%s");
        chatFr.put(MessageKey.DISPLAY_TASK_LIST_PERIOD,
                "Voici les tâches comprises entre %s et %s :\n%s");

    }

    private static String lookup(Map<Lang, Map<MessageKey, String>> store, MessageKey key) {
        String s = store.getOrDefault(currentLang, Map.of()).get(key);
        if (s != null) return s;

        s = store.getOrDefault(Lang.EN, Map.of()).get(key);
        if (s != null) return s;

        throw new IllegalArgumentException("Missing message data for key: " + key + " (" + currentLang + ")");
    }


    public static String getIoMessage(MessageKey key) {
        return lookup(ioMessages, key);
    }

    public static String getIoMessage(MessageKey key, String... details) {
        String base = lookup(ioMessages, key);
        return base + " " + String.join(" ", details).trim();
    }

    private static String formatWrap(String msg) {
        return SEPARATOR + "\n\t" + msg + "\n" + SEPARATOR;
    }

    public static String getChatbotMessage(MessageKey key) {
        return formatWrap(lookup(chatbotMessages, key));
    }

    public static String getChatbotMessage(MessageKey key, String details) {
        return formatWrap(lookup(chatbotMessages, key) + " " + details);
    }

    public static String getChatbotMessageFormatted(MessageKey key, Object... details) {
        String template = lookup(chatbotMessages, key);
        return formatWrap(String.format(template, details));
    }

    public static String getChatbotMessage(MessageKey[] keys, String details) {
        StringBuilder toDisplay = new StringBuilder();
        for (MessageKey key : keys) {
            toDisplay.append(lookup(chatbotMessages, key)).append(" ");
        }
        return formatWrap(toDisplay.toString().trim() + " " + details);
    }

    public static String buildListMessage(ArrayList<Task> taskList) {
        int number = 0;
        StringBuilder list = new StringBuilder();
        for (Task t : taskList) {
            number += 1;
            list.append("\t").append(number).append(". ").append(t.toString()).append("\n");
        }
        return list.toString();
    }

    public static String getUsageMessage(MessageKey key) {
        return lookup(usageMessages, key);
    }
}