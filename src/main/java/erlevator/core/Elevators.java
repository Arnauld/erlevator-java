package erlevator.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Elevators {

    private Cabin[] cabins;
    private List<UserCommand> userCommands;
    private ElevatorStrategy strategy;
    private final TimeService timeService;
    private int lowerFloor;
    private int higherFloor;
    private Long lastTick;

    public Elevators(ElevatorStrategy strategy) {
        this(strategy, new TimeService());
    }

    public Elevators(ElevatorStrategy strategy, TimeService timeService) {
        this.strategy = strategy;
        this.timeService = timeService;
    }

    public Iterable<UserCommand> pendingCommands() {
        return new ArrayList<UserCommand>(userCommands);
    }

    public int lowerFloor() {
        return lowerFloor;
    }

    public int higherFloor() {
        return higherFloor;
    }

    public Cabin cabin(int index) {
        return cabins[index];
    }

    public int nbCabins() {
        return cabins.length;
    }

    public Long lastTick() {
        return lastTick;
    }

    private void tick() {
        UserCommand.tickAll(userCommands);
        this.lastTick = timeService.currentTimeMillis();
    }

    /**
     * API
     */
    public List<Command> nextCommands() {
        tick();
        return strategy.nextCommands(userCommands);
    }

    /**
     * API
     */
    public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        resetUserCommand();
        resetCabins(cabinSize, cabinCount);
        resetStrategy(lowerFloor, higherFloor);
        tick();
    }

    private void resetUserCommand() {
        this.userCommands = new LinkedList<UserCommand>();
    }

    private void resetCabins(int cabinSize, int cabinCount) {
        this.cabins = new Cabin[cabinCount];
        for (int i = 0; i < cabinCount; i++)
            cabins[i] = new Cabin(cabinSize, 0, DoorState.CLOSED);
    }

    private void resetStrategy(int lowerFloor, int higherFloor) {
        this.strategy.reset(lowerFloor, higherFloor, cabins);
    }

    /**
     * API
     */
    public void userExited(int cabin) {
        tick();
        cabins[cabin].userExited();
    }

    /**
     * API
     */
    public void userEntered(int cabin) {
        tick();
        cabins[cabin].userEntered();
    }

    /**
     * API
     */
    public void call(int atFloor, Direction dir) {
        tick();
        userCommands.add(new UserCommand.Call(atFloor, dir));
    }

    /**
     * API
     */
    public void go(int cabin, int floorToGo) {
        tick();
        userCommands.add(new UserCommand.Go(cabin, floorToGo));
    }

}
