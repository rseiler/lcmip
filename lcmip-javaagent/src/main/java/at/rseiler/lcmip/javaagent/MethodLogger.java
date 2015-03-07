package at.rseiler.lcmip.javaagent;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Logs all method calls.
 * <p>
 * A BufferedWriter is used because of performance reasons. Every 60s a flush is triggered, if necessary.
 * </p>
 *
 * @author Reinhard Seiler {@literal <rseiler.developer@gmail.com>}
 */
public class MethodLogger {

    /**
     * Used to synchronize the write and flush calls.
     */
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Set<String> METHODS = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static long lastFlushed = System.currentTimeMillis();
    private static boolean hasToFlush;
    public static BufferedWriter writer;

    /**
     * Logs the specific method call.
     *
     * @param method the method
     */
    public static void log(String method) {
        if (writer != null) {
            if (!METHODS.contains(method)) {
                METHODS.add(method);
                write(DATE_TIME_FORMATTER.format(LocalDateTime.now()) + " " + method + "\n");
            }
        }

        flushIfNecessary();
    }

    /**
     * Writes the value to the BufferedWriter.
     *
     * @param value the value to be written
     */
    private static void write(String value) {
        try {
            LOCK.lock();
            hasToFlush = true;
            writer.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Flushes the content of the buffer to the disk, if necessary.
     */
    private static void flushIfNecessary() {
        if (System.currentTimeMillis() > lastFlushed + 60_000 && hasToFlush) {
            try {
                lastFlushed = System.currentTimeMillis();
                LOCK.lock();
                hasToFlush = false;
                writer.flush();
                LOCK.unlock();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
