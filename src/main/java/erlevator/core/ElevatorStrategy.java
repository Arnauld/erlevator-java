package erlevator.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface ElevatorStrategy {
    void reset(int lowerFloor, int higherFloor, Cabin[] cabins);

    List<Command> nextCommands(LinkedList<UserCommand> userCommands);
}
