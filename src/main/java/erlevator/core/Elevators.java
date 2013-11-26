package erlevator.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Elevators {

    private Cabin[] cabins;
    private LinkedList<UserCommand> userCommands;
    private ElevatorStrategy strategy;
    private int lowerFloor;
    private int higherFloor;

    public Elevators(ElevatorStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Command> nextCommands() {
        return strategy.nextCommands(userCommands);
    }

    public int lowerFloor() {
        return lowerFloor;
    }

    public int higherFloor() {
        return higherFloor;
    }

    public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        resetUserCommand();
        resetCabins(cabinSize, cabinCount);
        resetStrategy(lowerFloor, higherFloor);
    }

    private void resetUserCommand() {
        this.userCommands = new LinkedList<UserCommand>();
    }

    private void resetCabins(int cabinSize, int cabinCount) {
        this.cabins = new Cabin[cabinCount];
        for(int i=0;i <cabinCount; i++)
            cabins[i] = new Cabin(cabinSize, 0, DoorState.CLOSED);
    }

    private void resetStrategy(int lowerFloor, int higherFloor) {
        this.strategy.reset(lowerFloor, higherFloor, cabins);
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

    public int nbCabins() {
        return cabins.length;
    }
}
