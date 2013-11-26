package erlevator.core;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ElevatorsTest {

    private Elevators elevators;

    @Before
    public void setUp() {
        elevators = new Elevators(new OmnibusStrategy());
    }

    @Test
    public void reset () {
        elevators.reset(-5, 5, 10, 2);

        Cabin cabin0 = elevators.cabin(0);
        assertThat(cabin0.floor()).isEqualTo(0);
        assertThat(cabin0.doorState()).isEqualTo(DoorState.CLOSED);

        Cabin cabin1 = elevators.cabin(1);
        assertThat(cabin1.floor()).isEqualTo(0);
        assertThat(cabin1.doorState()).isEqualTo(DoorState.CLOSED);
    }
}
