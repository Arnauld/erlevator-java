package erlevator.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RawCommandPriorityStrategy implements ElevatorStrategy {

    private Cabin[] cabins;

    @Override
    public void reset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        this.cabins = cabins;
    }

    @Override
    public List<Command> nextCommands(LinkedList<UserCommand> userCommands) {
        List<Command> commands = new ArrayList<Command>();
        for (int cabinIndex = 0; cabinIndex < cabins.length; cabinIndex++) {
            Cabin cabin = cabins[cabinIndex];

            UserCommand userCommand = findMostSuitableUserCommand(userCommands, cabinIndex, cabin);
            if (userCommand != null) {
                int floorImplied = userCommand.floorImplied();
                Command command = commandForTarget(cabin, floorImplied);

                // discard any command belonging to the floor one left :)
                if (cabin.floor() == floorImplied && command == Command.CLOSE) {
                    removeAllCommandsInvolvingFloor(userCommands, floorImplied);
                }
                commands.add(command);
            } else
                commands.add(Command.NOTHING);
        }
        return commands;
    }

    private static void removeAllCommandsInvolvingFloor(LinkedList<UserCommand> userCommands, int floor) {
        Iterator<UserCommand> commands = userCommands.iterator();
        while (commands.hasNext()) {
            UserCommand command = commands.next();
            if (command.implyFloor(floor)) {
                System.out.println("RawCommandPriorityStrategy.removeAllCommandsInvolvingFloor(floor: " + floor + ", command: " + command + ")");
                commands.remove();
            }
        }
    }

    private static Command commandForTarget(Cabin cabin, int targetFloor) {
        int cabinFloor = cabin.floor();

        if (cabin.doorState() == DoorState.OPENED) {
            cabin.closeDoor();
        } else if (cabinFloor < targetFloor) {
            cabin.moveUp();
        } else if (cabinFloor > targetFloor) {
            cabin.moveDown();
        } else {
            cabin.openDoorNoDirection();
        }
        return cabin.lastCommand();
    }

    private static UserCommand findMostSuitableUserCommand(LinkedList<UserCommand> userCommands, int cabinIndex, Cabin cabin) {
        if (cabin.isFull()) {
            // attempt to empty it by going to the closest queried floor
            UserCommand.Go go = findClosestGoForCabin(cabinIndex, cabin, userCommands);
            if (go != null) {
                return go;
            }
        }
        return findClosestCommandForCabin(cabinIndex, cabin, userCommands);
    }

    private static UserCommand.Go findClosestGoForCabin(int cabinIndex, Cabin cabin, LinkedList<UserCommand> userCommands) {
        int foundDist = Integer.MAX_VALUE;
        UserCommand.Go found = null;
        for (UserCommand command : userCommands) {
            if (!command.isGo())
                continue;

            UserCommand.Go go = (UserCommand.Go) command;
            if (go.cabin != cabinIndex)
                continue;

            if (found == null) {
                found = go;
                foundDist = Math.abs(cabin.floor() - found.floorToGo);
                continue;
            }

            int dist = Math.abs(cabin.floor() - go.floorToGo);
            if (dist < foundDist) {
                found = go;
                foundDist = dist;
            }
        }
        return found;
    }

    private static UserCommand findClosestCommandForCabin(int cabinIndex, Cabin cabin, LinkedList<UserCommand> userCommands) {
        int foundDist = Integer.MAX_VALUE;
        UserCommand found = null;
        for (UserCommand command : userCommands) {

            if (command.isGo()) {
                UserCommand.Go go = (UserCommand.Go) command;
                if (go.cabin != cabinIndex)
                    continue;
            }

            if (found == null) {
                found = command;
                foundDist = Math.abs(cabin.floor() - found.floorImplied());
            }

            int dist = Math.abs(cabin.floor() - command.floorImplied());
            if (dist < foundDist) {
                found = command;
                foundDist = dist;
            }
        }
        return found;
    }
}
