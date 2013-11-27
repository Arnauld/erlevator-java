package erlevator.core;

import java.util.concurrent.Callable;

/**
 *
 */
public class WeightCalculatorDistanceBased implements WeightCalculator {
    @Override
    public Weight weight(int index, final Cabin cabin, final UserCommand userCommand) {
        int distance = cabin.floor() - userCommand.floorImplied();
        return new Weight(1000.0f - Math.abs(distance), new Callable<Command>() {
            @Override
            public Command call() throws Exception {
                int floor = userCommand.floorImplied();
                return cabin.updateForTarget(floor);
            }
        });
    }

    @Override
    public void reset(int lowerFloor, int higherFloor, Cabin[] cabins) {
    }
}
