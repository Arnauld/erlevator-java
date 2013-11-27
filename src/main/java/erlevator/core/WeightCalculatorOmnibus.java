package erlevator.core;

import java.util.concurrent.Callable;

/**
 *
 */
public class WeightCalculatorOmnibus implements WeightCalculator {
    private int lowerFloor;
    private int higherFloor;

    @Override
    public Weight weight(final int index, final Cabin cabin, UserCommand userCommand) {
        return new Weight(1000.0f, new Callable<Command>() {
            @Override
            public Command call() throws Exception {
                return nextCommand(index, cabin);
            }
        }, true);
    }

    private Command nextCommand(int index, Cabin cabin) {
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

    @Override
    public void reset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        // all cabins start with an offset
        int offset = offset(lowerFloor, higherFloor, cabins);
        for (int i = 0; i < cabins.length; i++)
            cabins[i].setStrategyData(i * offset);
    }

    private int offset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        return -2 * (higherFloor - lowerFloor) / (cabins.length);
    }
}
