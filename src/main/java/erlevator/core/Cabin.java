package erlevator.core;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Cabin {
    private final int cabinSize;

    private int floor;
    private int nbUsers;
    private Direction direction = Direction.UP;
    private DoorState doorState;
    private Object strategyData;
    private Command lastCommand;

    public Cabin(int cabinSize, int initialFloor, DoorState initialDoorState) {
        this.cabinSize = cabinSize;
        this.floor = initialFloor;
        this.doorState = initialDoorState;
    }

    public Command lastCommand() {
        return lastCommand;
    }

    public void moveUp() {
        if(doorState == DoorState.CLOSED) {
            lastCommand = Command.UP;
            direction = Direction.UP;
            floor++;
        }
        else
            throw new IllegalStateException("Doors open");
    }

    public void moveDown() {
        if(doorState == DoorState.CLOSED) {
            lastCommand = Command.DOWN;
            direction = Direction.DOWN;
            floor--;
        }
        else
            throw new IllegalStateException("Doors open");
    }

    public void openDoorNoDirection() {
        lastCommand = Command.OPEN;
        doorState = DoorState.OPENED;
    }


    public void openDoor() {
        lastCommand = (direction==Direction.UP)?Command.OPEN_UP:Command.OPEN_DOWN;
        doorState = DoorState.OPENED;
    }

    public void closeDoor() {
        lastCommand = Command.CLOSE;
        doorState = DoorState.CLOSED;
    }

    public int nbUsers() {
        return nbUsers;
    }

    public void userExited() {
        nbUsers--;
    }

    public void userEntered() {
        nbUsers++;
    }

    public int floor() {
        return floor;
    }

    public DoorState doorState() {
        return doorState;
    }

    public Direction direction() {
        return direction;
    }

    public void direction(Direction direction) {
        this.direction = direction;
    }

    @SuppressWarnings("unchecked")
    public <T> T getStrategyData() {
        return (T)strategyData;
    }

    public void setStrategyData(Object strategyData) {
        this.strategyData = strategyData;
    }

    public boolean isFull() {
        return nbUsers == cabinSize;
    }

    public boolean isOpen() {
        return DoorState.OPENED == doorState;
    }

    public Command updateForTarget(int targetFloor) {
        int cabinFloor = this.floor();

        if (this.doorState() == DoorState.OPENED) {
            this.closeDoor();
        } else if (cabinFloor < targetFloor) {
            this.moveUp();
        } else if (cabinFloor > targetFloor) {
            this.moveDown();
        } else {
            this.openDoorNoDirection();
        }
        return this.lastCommand();
    }
}
