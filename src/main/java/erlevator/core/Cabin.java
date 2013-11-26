package erlevator.core;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Cabin {
    private final int cabinSize;

    private int floor;
    private int nbUsers;
    private Direction direction;
    private DoorState doorState;

    public Cabin(int cabinSize, int initialFloor, DoorState initialDoorState) {
        this.cabinSize = cabinSize;
        this.floor = initialFloor;
        this.doorState = initialDoorState;
    }

    public void moveUp() {
        if(doorState == DoorState.CLOSED)
            floor++;
        else
            throw new IllegalStateException("Doors open");
    }

    public void moveDown() {
        if(doorState == DoorState.CLOSED)
            floor--;
        else
            throw new IllegalStateException("Doors open");
    }

    public void openDoor() {
        doorState = DoorState.OPENED;
    }

    public void closeDoor() {
        doorState = DoorState.CLOSED;
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
}
