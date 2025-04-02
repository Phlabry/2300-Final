// logs errors
package Utilities;

import java.io.*;
import java.text.*;
import java.util.*;

public class Logger {
    private static final String LOG_FILE = "game_log.txt"; 
    private static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        writer.println("[" + timestamp + "] " + message);
        writer.flush();
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
