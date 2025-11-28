import java.io.IOException;
import java.util.logging.*;

public class SystemLogger {

    private static final Logger logger = Logger.getLogger("TeamMateLogger");

    static {
        try {
            // Ensure logs folder exists
            java.io.File dir = new java.io.File("logs");
            if (!dir.exists()) dir.mkdir();

            // Use full qualified FileHandler to avoid conflict with your FileHandler class
            java.util.logging.FileHandler fileHandler =
                    new java.util.logging.FileHandler("logs/system.log", true);

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.addHandler(new java.util.logging.ConsoleHandler());

            logger.setUseParentHandlers(false);

        } catch (IOException e) {
            System.out.println("Logger setup failed: " + e.getMessage());
        }
    }

    // short wrapper for INFO
    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    // short wrapper for ERROR
    public static void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }
}
