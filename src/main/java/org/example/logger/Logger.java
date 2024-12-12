package org.example.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Logger class for recording messages to both the console and a log file
public class Logger {
    private static final String LOG_FILE = "src/main/resources/ticketing_log.txt";

    // Logs a message with a timestamp to the console and a log file.
    // This method is synchronized to ensure thread-safe writing to the log file.
    public static synchronized void log(String message) {
        // Generate a timestamp and format the log message
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = timestamp + " - " + message;

        // Print the log message to the console
        System.out.println(logMessage);

        // Write the log message to the log file
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            // Display an error message if writing to the log file fails
            System.err.println("Error logging message: " + e.getMessage());
        }
    }
}
