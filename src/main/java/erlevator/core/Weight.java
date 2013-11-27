package erlevator.core;

import java.util.concurrent.Callable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Weight {

    private final float weight;
    private final Callable<Command> command;
    private final boolean exclusive;

    public Weight(float weight, Callable<Command> command) {
        this(weight, command, false);
    }
    public Weight(float weight, Callable<Command> command, boolean exclusive) {
        this.weight = weight;
        this.command = command;
        this.exclusive = exclusive;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public Command applyAndGetCommand() {
        try {
            return command.call();
        } catch (Exception e) {
            // never happen...
            throw new IllegalStateException(e);
        }
    }

    public boolean isHeavierThan(Weight other) {
        if(other == null)
            return true;
        return weight > other.weight;
    }

    @Override
    public String toString() {
        return "Weight{" +
                "weight=" + weight +
                ", command=" + command +
                '}';
    }
}
