package erlevator.core;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum Command {
    UP,
    DOWN,
    OPEN,
    OPEN_UP,
    OPEN_DOWN,
    CLOSE,
    NOTHING;

    public String asString() {
        return name();
    }



}
