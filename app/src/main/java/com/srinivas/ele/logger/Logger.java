package com.srinivas.ele.logger;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

public class Logger {
    public static boolean IS_DEBUG = true;
    public static final int INPUT_STREAM_BUFFER_SIZE = 16 * 1024;
    private static boolean sdCardPresent;
    private SimpleDateFormat fmt = null;
    private BufferedWriter buf = null;

    private static String logFilePath = null;
    private static File logFile = null;
    private static final long FILE_SIZE = 100 * 1024 * 1024; // 1 MB = 1024*1024
    // byte

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;

    private static int label = DEBUG;
    private static Logger instance = null;

    private Logger(String logFilePath) {
        this.logFilePath = logFilePath;
        init(logFilePath);
    }

    public static Logger getInstance(String logFilePath){
        if (instance == null){
            instance = new Logger(logFilePath);
        }
        return instance;
    }
    /**
     * This is static method, for initializing for variable of ELogger. This
     * method takes file name as argument. If file name is passed then this
     * class saves the log message in given file name on device SDCARD. If file
     * name not provided then this class print the log only in logcat.
     *
     * @param logFilePath
     * @return return's true if ELogger successfully initialized otherwise false
     */
    synchronized private static boolean init(String logFilePath) {

        if (logFilePath == null) {
            return false;
        }
        String status = Environment.getExternalStorageState();
        if (!status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            sdCardPresent = false;
            return sdCardPresent;
        }
        sdCardPresent = true;
        Logger.logFilePath = logFilePath;
        logFile = new File(logFilePath);
        if (logFile == null) {
            return false;
        }
        if (logFile.exists()) {
            if (logFile.length() >= FILE_SIZE) {
                logFile.delete();
            } else {
                return true;
            }
        }
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            logFile = null;
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static public void close() {
        logFilePath = null;
        logFile = null;
    }

    public static void info(String tag, String msg) {
        if (logFile != null && label <= INFO && sdCardPresent) {
            if (instance != null) {
                msg = "[" + "INFO" + "] " + msg;
                instance.saveLogToFile(tag, msg);
            }
        }
    }

    public static void debug(String tag, String msg) {
        if (logFile != null && label <= DEBUG && sdCardPresent) {
            if (instance != null) {
                msg = "[" + "DEBUG" + "] " + msg;
                instance.saveLogToFile(tag, msg);
            }
        }
    }

    public static void fatal(String tag, String msg) {
        if (logFile != null && sdCardPresent) {
            if (instance != null) {
                msg = "[" + "FATAL" + "] " + msg;
                instance.saveLogToFile(tag, msg);
            }
        }
    }

    public static void error(String tag, String msg) {
        if (logFile != null && label <= ERROR && sdCardPresent) {
            if (instance != null) {
                msg = "[" + "ERROR" + "] " + msg;
                instance.saveLogToFile(tag, msg);
            }
        }
    }

    public static void warn(String tag, String msg) {
        if (logFile != null && label <= WARNING && sdCardPresent) {
            if (instance != null) {
                msg = "[" + "WARNING" + "] " + msg;
                instance.saveLogToFile(tag, msg);
            }
        }
    }

    public static void verbose(String tag, String msg) {
        if (logFile != null && label <= VERBOSE && sdCardPresent) {
            if (instance != null) {
                msg = "[" + "VERBOSE" + "] " + msg;
                instance.saveLogToFile(tag, msg);
            }
        }
    }

    /**
     * This method takes the backup of log file if the log file size is greater
     * than 100MB
     *
     * @return true if backup taken otherwise false
     */
    @SuppressWarnings("unused")
    synchronized private boolean backUpLog() {
        String backupFileName = logFilePath + "_backup";
        File backUpFile = new File(backupFileName);
        if (backUpFile == null) {
            return false;
        }
        if (backUpFile.exists()) {
            backUpFile.delete();
        }

        logFile.renameTo(backUpFile);
        logFile = new File(logFilePath);
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method saves the log message into specified file name. It takes
     * message as parameter
     *
     * @param msg to be saved
     */
    synchronized private void saveLogToFile(String tag, String msg) {
        if (!IS_DEBUG) {
            return;
        }
        if (logFile.length() >= FILE_SIZE) {
            backUpLog();
        }
        if (fmt == null) {
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        String timeStr = fmt.format(System.currentTimeMillis());
        long threadId = Thread.currentThread().getId();
        String logStr = "[" + timeStr + "]" + " " + "[" + threadId + "]" + "  "
                + tag + "  " + msg;
        try {
            if (buf == null) {
                buf = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(logFile, true)),
                        INPUT_STREAM_BUFFER_SIZE);
            }
            if (buf != null) {
                buf.append(logStr);
                buf.newLine();
                buf.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set the log levels.
     *
     * @param logLevel
     */
    public static void setLogLevel(int logLevel) {
        label = logLevel;
    }
}
