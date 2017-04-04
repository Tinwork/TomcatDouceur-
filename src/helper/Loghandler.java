package helper;

import java.io.IOException;
import java.security.Security;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

/**
 * Created by lookitsmarc on 04/04/2017.
 */
public class Loghandler {

    private static Logger logger = Logger.getLogger("log");
    private static FileHandler file;
    private static SimpleFormatter formatter = new SimpleFormatter();

    /**
     * Log
     *      Log the data in a file
     */
    public static void log(String data, String level){
        try {
            file = new FileHandler("/opt/tomcat9/logs/apps.log");
            logger.addHandler(file);
            file.setFormatter(formatter);

            logger.log(getLevel(level), data);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Get Level
     * @param level
     * @return Level levelToReturn
     */
    public static Level getLevel(String level){
        Level levelToReturn;

        switch (level) {
            case "fatal":
                levelToReturn = SEVERE;
                break;
            case "warn":
                levelToReturn = WARNING;
                break;
            case "info":
                levelToReturn = INFO;
                break;
            default:
                levelToReturn = WARNING;
        }

        return levelToReturn;
    }
}
