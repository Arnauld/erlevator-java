package erlevator.core;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class WeightCalculatorMixed implements WeightCalculator {
    private final WeightCalculator[] calculators;

    public WeightCalculatorMixed(WeightCalculator... calculators) {
        this.calculators = calculators;
    }

    @Override
    public void reset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        for(WeightCalculator weightCalculator : calculators)
            weightCalculator.reset(lowerFloor, higherFloor, cabins);
    }

    @Override
    public Weight weight(int index, Cabin cabin, UserCommand userCommand) {
        return calculators[index % calculators.length].weight(index, cabin, userCommand);
    }
}
