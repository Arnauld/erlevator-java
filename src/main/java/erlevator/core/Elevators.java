package erlevator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Elevators {

    private Cabin[] cabins;
    private int lowerFloor;
    private int higherFloor;
    private List<UserCommand> userCommands;

    public List<Command> nextCommands() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
        this.userCommands = new ArrayList<UserCommand>();
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        this.cabins = new Cabin[cabinCount];

        for(int i=0;i <cabinCount; i++)
            cabins[i] = new Cabin(cabinSize, 0, DoorState.CLOSED);
    }

    public void userExited(int cabin) {
        cabins[cabin].userExited();
    }

    public void userEntered(int cabin) {
        cabins[cabin].userEntered();
    }

    public void call(int atFloor, Direction dir) {
        userCommands.add(new UserCommand.Call(atFloor, dir));
    }

    public void go(int cabin, int floorToGo) {
        userCommands.add(new UserCommand.Go(cabin, floorToGo));
    }

    Cabin cabin(int index) {
        return cabins[index];
    }
}
