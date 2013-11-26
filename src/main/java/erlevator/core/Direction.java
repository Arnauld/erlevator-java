package erlevator.core;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum Direction {
    UP,
    DOWN;

    public static Direction from(String to) {
        for(Direction direction : values()) {
            if(direction.name().equalsIgnoreCase(to))
                return direction;
        }
        throw new IllegalArgumentException("Direction '" + to +"' is not mapped");
    }


}
