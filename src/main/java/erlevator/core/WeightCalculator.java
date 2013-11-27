package erlevator.core;

import java.util.concurrent.Callable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface WeightCalculator {

    void reset(int lowerFloor, int higherFloor, Cabin[] cabins);

    Weight weight(int index, Cabin cabin, UserCommand userCommand);

}
