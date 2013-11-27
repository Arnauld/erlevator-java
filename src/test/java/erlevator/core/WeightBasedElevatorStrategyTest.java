package erlevator.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static erlevator.core.UserCommand.call;
import static erlevator.core.UserCommand.go;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class WeightBasedElevatorStrategyTest {

    private static final int CABIN_0 = 0;

    private Cabin[] cabins;
    private WeightBasedElevatorStrategy strategy;
    private List<UserCommand> userCommands;

    @Before
    public void setUp() {
        strategy = new WeightBasedElevatorStrategy();
        cabins = initCabins(1);
        strategy.reset(-5, 5, cabins);

        userCommands = new ArrayList<UserCommand>();
    }

    @Test
    public void __closest__go_first_deactivated() {
        strategy.calculator(new WeightCalculatorDistanceBased());
        strategy.featureGoFirst(false);

        appendCommands(
                call(4, Direction.UP),
                call(2, Direction.UP),
                call(4, Direction.DOWN),
                go(CABIN_0, 3),
                call(-1, Direction.DOWN));

        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, -1);

        assertNextCommands(Command.OPEN);
        assertNextCommands(Command.CLOSE);
        assertCabinFloor(CABIN_0, -1);

        appendCommands(//
                go(CABIN_0, 0), // user entered
                call(-2, Direction.UP));

        //System.out.println(Iterables.join(userCommands, "\n\t"));
        assertNextCommands(Command.UP); // for same distance oldest is picked first
        appendCommands(call(0, Direction.DOWN));
        //assertCabinFloor(CABIN_0, -1);
    }

    @Test
    public void __omnibus__go_first_deactivated() {
        strategy.calculator(new WeightCalculatorOmnibus());
        strategy.featureGoFirst(false);

        appendCommands(
                call(4, Direction.UP),
                call(2, Direction.UP),
                call(4, Direction.DOWN),
                go(CABIN_0, 3),
                call(-1, Direction.DOWN));

        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.UP);
        assertCabinFloor(CABIN_0, 1);
        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.UP);
        assertCabinFloor(CABIN_0, 2);
        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.UP);
        assertCabinFloor(CABIN_0, 3);
        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.UP);
        assertCabinFloor(CABIN_0, 4);
        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.UP);
        assertCabinFloor(CABIN_0, 5);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, 4);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, 3);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, 2);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, 1);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, 0);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, -1);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);

        assertThat(userCommands).isEmpty();
        assertNextCommands(Command.NOTHING); // no more orders :)

        appendCommands(call(5, Direction.DOWN));

        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, -2);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, -3);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, -4);
        assertNextCommands(Command.OPEN_DOWN);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.DOWN);
        assertCabinFloor(CABIN_0, -5);
        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
        assertNextCommands(Command.UP);
        assertCabinFloor(CABIN_0, -4);
        assertNextCommands(Command.OPEN_UP);
        assertNextCommands(Command.CLOSE);
    }

    private void assertCabinFloor(int cabinIndex, int expectedFloor) {
        assertThat(cabin(cabinIndex).floor()).isEqualTo(expectedFloor);
    }

    private void assertNextCommands(Command... expectedCommands) {
        List<Command> commands = strategy.nextCommands(userCommands);
        assertThat(commands).containsExactly(expectedCommands);
    }

    private void appendCommands(UserCommand... commands) {
        for (UserCommand c : commands)
            userCommands.add(c);
    }

    private Cabin cabin(int index) {
        return cabins[index];
    }

    private Cabin[] initCabins(int nb) {
        Cabin[] cabins = new Cabin[nb];
        for (int i = 0; i < nb; i++) {
            cabins[i] = new Cabin(10, 0, DoorState.CLOSED);
        }
        return cabins;
    }
}
