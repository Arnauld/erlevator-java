package erlevator.core;

import erlevator.util.Iterables;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OmnibusStrategy implements ElevatorStrategy {

    private int lowerFloor;
    private int higherFloor;
    private Cabin[] cabins;

    public OmnibusStrategy() {
    }

    @Override
    public void reset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        this.cabins = cabins;

        // all cabins start with an offset
        int offset = offset(lowerFloor, higherFloor, cabins);
        for (int i = 0; i < cabins.length; i++)
            cabins[i].setStrategyData(i * offset);
    }

    private int offset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        return -2 * (higherFloor - lowerFloor) / (cabins.length);
    }

    @Override
    public List<Command> nextCommands(Iterable<UserCommand> userCommands) {
        // prevent leaks...
        Iterables.removeAll(userCommands);

        int nbCabins = cabins.length;
        List<Command> commands = new ArrayList<Command>(nbCabins);
        for (int i = 0; i < cabins.length; i++) {
            Cabin cabin = cabins[i];
            Command command = cycle(i, cabin);
            commands.add(command);
        }
        return commands;
    }

    private Command cycle(int index, Cabin cabin) {
        Integer offset = cabin.getStrategyData();
        try {
            if (offset < 0)
                return Command.NOTHING;
            else if (offset == 0) {
                cabin.direction(Direction.values()[index % 2]);
                cabin.openDoor();
                return cabin.lastCommand();
            }
        } finally {
            cabin.setStrategyData(++offset);
        }

        Command command = cabin.lastCommand();
        int floor = cabin.floor();

        if (cabin.doorState() == DoorState.OPENED) {
            cabin.closeDoor();
        } else if (command == Command.UP || command == Command.DOWN) {
            if (floor == higherFloor)
                cabin.direction(Direction.DOWN);
            else if (floor == lowerFloor)
                cabin.direction(Direction.UP);
            cabin.openDoor();
        } else {
            switch (cabin.direction()) {
                case UP:
                    cabin.moveUp();
                    break;
                case DOWN:
                    cabin.moveDown();
                    break;
            }
        }
        return cabin.lastCommand();
    }
}
