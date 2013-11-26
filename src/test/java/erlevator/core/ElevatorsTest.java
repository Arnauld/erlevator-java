package erlevator.core;

import org.junit.Before;
import org.junit.Test;

import static erlevator.core.Command.NOTHING;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ElevatorsTest {

    private Elevators elevators;

    @Before
    public void setUp() {
        elevators = new Elevators();
    }

    @Test
    public void reset () {
        elevators.reset(-5, 5, 10, 1);

        Cabin cabin = elevators.cabin(0);
        assertThat(cabin.floor()).isEqualTo(0);
        assertThat(cabin.doorState()).isEqualTo(DoorState.CLOSED);
    }

    @Test
    public void nothing_when_no_user_commands() {
        elevators.reset(-5, 5, 10, 1);
        assertThat(elevators.nextCommands()).containsExactly(NOTHING);
    }
}
