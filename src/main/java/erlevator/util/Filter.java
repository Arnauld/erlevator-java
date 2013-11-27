package erlevator.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Filter<T> {
    boolean accepts(T value);
}
