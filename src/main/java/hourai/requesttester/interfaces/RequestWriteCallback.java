/*
 *  Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.interfaces;

/**
 * Describes a class which processes a readere from the writer
 */
public interface RequestWriteCallback {
   public void recievedLine(String line);
   public void recievedChar(char c);
}


