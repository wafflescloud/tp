package seedu.address.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.commons.core.LogsCenter;

/**
 * Loads command help information from a JSON configuration file.
 */
public class HelpCommandLoader {
    private static final String HELP_FILE = "/help/commands.json";
    private static final Logger logger = LogsCenter.getLogger(HelpCommandLoader.class);
    private static Map<String, CommandHelp> commandHelpMap;

    /**
     * Represents help information for a single command.
     */
    public static class CommandHelp {
        private String description;
        private List<String> formats;
        private List<String> examples;

        /**
         * Returns the description of the command.
         * @return the command description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the description of the command.
         * @param description the command description
         */
        public void setDescription(String description) {
            assert description != null : "Description should not be null";
            this.description = description;
        }

        /**
         * Returns the list of formats for the command.
         * @return the list of command formats
         */
        public List<String> getFormats() {
            return formats;
        }

        /**
         * Sets the list of formats for the command.
         * @param formats the list of command formats
         */
        public void setFormats(List<String> formats) {
            assert formats != null : "Formats list should not be null";
            this.formats = formats;
        }

        /**
         * Returns the list of examples for the command.
         * @return the list of command examples
         */
        public List<String> getExamples() {
            return examples;
        }

        /**
         * Sets the list of examples for the command.
         * @param examples the list of command examples
         */
        public void setExamples(List<String> examples) {
            assert examples != null : "Examples list should not be null";
            this.examples = examples;
        }
    }

    /**
     * Loads and returns the command help map from the JSON file.
     * Returns a cached version on subsequent calls.
     *
     * @return Map of command names to their help information
     */
    public static Map<String, CommandHelp> loadCommandHelp() {
        if (commandHelpMap == null) {
            try (InputStream is = HelpCommandLoader.class.getResourceAsStream(HELP_FILE)) {
                if (is == null) {
                    logger.warning("Command help file not found: " + HELP_FILE);
                    commandHelpMap = new HashMap<>();
                    return commandHelpMap;
                }
                ObjectMapper mapper = new ObjectMapper();
                commandHelpMap = mapper.readValue(is,
                        mapper.getTypeFactory().constructMapType(HashMap.class, String.class, CommandHelp.class));
                logger.info("Successfully loaded command help information");
                assert commandHelpMap != null : "Command help map should not be null after loading";
                assert !commandHelpMap.isEmpty() : "Command help map should not be empty after successful load";
            } catch (IOException e) {
                logger.warning("Failed to load command help: " + e.getMessage());
                commandHelpMap = new HashMap<>();
            }
        }
        assert commandHelpMap != null : "Command help map should never be null";
        return commandHelpMap;
    }

    /**
     * Gets help information for a specific command.
     *
     * @param commandName The name of the command
     * @return CommandHelp object or null if not found
     */
    public static CommandHelp getCommandHelp(String commandName) {
        assert commandName != null : "Command name should not be null";
        assert !commandName.trim().isEmpty() : "Command name should not be empty";
        return loadCommandHelp().get(commandName);
    }
}
