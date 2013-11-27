package erlevator.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Filters {
    public static <T> Filter<T> all() {
        return new Filter<T>() {
            @Override
            public boolean accepts(T value) {
                return true;
            }
        };
    }
}
