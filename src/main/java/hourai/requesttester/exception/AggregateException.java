/*
 * Copywrite Olivier Faucheux
 * Obtained: https://stackoverflow.com/a/13142813/464755
 */
package hourai.requesttester.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * This abstract class is to be used for Exception generating by a collection of
 * causes.
 * <p />
 * Typically: several tries take place to do something in different ways and
 * each one fails. We therefore have to collect the exceptions to document why
 * it was not possible at all to do the thing.
 */
public class AggregateException extends Exception {

    /**
     * A generator of random numbers
     */
    private final static Random rand = new Random();

    /**
     * The causes of the exception
     */
    private final List<Throwable> causes;

    /**
     * A (reasonably unique) id for this exception. Used for a better output of
     * the stacktraces
     */
    private final long id = rand.nextLong();

    /**
     * @param causes
     * @see Exception#Exception(String)
     * @param message reason for the exception
     */
    public AggregateException(String message, Collection<? extends Throwable> causes) {
        super(message);
        this.causes = new ArrayList<Throwable>(causes);
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s <code>PrintStream</code> to use for output
     */
    @Override
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            s.println(this);
            StackTraceElement[] trace = getStackTrace();
            for (StackTraceElement trace1 : trace) {
                s.println("\tat " + trace1);
            }

            final Throwable ourCause = getCause();
            if (ourCause != null) {
                throw new AssertionError("The cause of an AggregateException should not be null");
            }

            for (int i = 0; i < causes.size(); i++) {
                final Throwable cause = causes.get(i);
                s.println(String.format(
                        "Cause number %s for AggregateException %s: %s ",
                        i,
                        getId(),
                        cause.toString()
                ));

                final ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                final PrintStream ps = new PrintStream(byteArrayOS);
                cause.printStackTrace(ps);
                ps.close();
                final String causeStackTrace = byteArrayOS.toString();
                int firstCR = causeStackTrace.indexOf("\n");

                s.append(causeStackTrace.substring(firstCR == -1 ? 0 : firstCR + 1));
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s. AggregateException %s with %s causes.", super.toString(), getId(), causes.size());
    }

    @Override
    public Throwable initCause(Throwable cause) {
        if (cause != null) {
            throw new AssertionError("The cause of an AggregateException must not be null");
        }

        return null;
    }

    /**
     *
     * @return {@link #id}
     */
    private String getId() {
        return String.format("%xs", id);
    }

}


