package erlevator.core;

import org.junit.Before;
import org.junit.Test;

import static erlevator.core.Command.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OmnibusStrategyTest {
    private Elevators elevators;

    @Before
    public void setUp() {
    }

    @Test
    public void cycle_up_and_down_one_cabine() {
        elevators = new Elevators(new OmnibusStrategy());
        elevators.reset(-5, 3, 10, 1);
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP); // 0
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(UP); // 1
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(UP); // 2
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(UP); // 3
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // 2
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // 1
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // 0
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // -1
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // -2
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // -3
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // -4
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(DOWN); // -5
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(UP); // -4
    }

    @Test
    public void cycle_up_and_down_two_cabins() {
        elevators = new Elevators(new OmnibusStrategy());
        elevators.reset(-2, 3, 10, 2);
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP, NOTHING); // 0 - 0
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, NOTHING);
        assertThat(elevators.nextCommands()).containsExactly(UP, NOTHING); // [1] - 0
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP, NOTHING);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, NOTHING);
        assertThat(elevators.nextCommands()).containsExactly(UP, OPEN_DOWN); // [2] - 0
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, DOWN); //  2 - [-1]
        assertThat(elevators.nextCommands()).containsExactly(UP, OPEN_DOWN); // [3] - -1
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, DOWN); // 3 - [-2]
        assertThat(elevators.nextCommands()).containsExactly(DOWN, OPEN_UP); // [2] - -2
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, UP); // 2 - [-1]
        assertThat(elevators.nextCommands()).containsExactly(DOWN, OPEN_UP); // [1] - -1
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, UP); // 1 - [0]
        assertThat(elevators.nextCommands()).containsExactly(DOWN, OPEN_UP); // [0] - 0
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, UP); // 0 - [1]
        assertThat(elevators.nextCommands()).containsExactly(DOWN, OPEN_UP); // [-1] - 1
        assertThat(elevators.nextCommands()).containsExactly(OPEN_DOWN, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, UP); // -1 - [2]
        assertThat(elevators.nextCommands()).containsExactly(DOWN, OPEN_UP); // [-2] - 2
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, UP); // -2 - [3]
        assertThat(elevators.nextCommands()).containsExactly(UP, OPEN_DOWN); // [-1] - 3
        assertThat(elevators.nextCommands()).containsExactly(OPEN_UP, CLOSE);
        assertThat(elevators.nextCommands()).containsExactly(CLOSE, DOWN); // -1 - [2]
    }

}
