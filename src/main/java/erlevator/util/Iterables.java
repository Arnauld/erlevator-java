package erlevator.util;

import erlevator.core.UserCommand;

import java.util.Iterator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Iterables {
    public static void removeAll(Iterable<UserCommand> userCommands) {
        Iterator<UserCommand> it = userCommands.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public static String join(Iterable<?> iterable, String separator) {
        StringBuilder builder = new StringBuilder();

        Iterator<?> it = iterable.iterator();
        String sep = "";
        while (it.hasNext()) {
            builder.append(sep).append(it.next());
            sep = separator;
        }
        return builder.toString();
    }
}
