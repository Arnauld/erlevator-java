package erlevator.core;

import erlevator.util.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class WeightBasedElevatorStrategy implements ElevatorStrategy {

    private Logger log = LoggerFactory.getLogger(WeightBasedElevatorStrategy.class);

    private int lowerFloor;
    private int higherFloor;
    private Cabin[] cabins;
    private WeightCalculator calculator;
    private boolean featureGoFirst = true;

    public WeightBasedElevatorStrategy() {
        this(new WeightCalculatorDistanceBased());
    }

    public WeightBasedElevatorStrategy(WeightCalculator calculator) {
        this.calculator = calculator;
    }

    public void calculator(WeightCalculator calculator) {
        this.calculator = calculator;
        resetCalculator();
    }

    public void featureGoFirst(boolean activate) {
        featureGoFirst = activate;
    }

    @Override
    public void reset(int lowerFloor, int higherFloor, Cabin[] cabins) {
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        this.cabins = cabins;
        resetCalculator();
    }

    private void resetCalculator() {
        this.calculator.reset(lowerFloor, higherFloor, cabins);
    }

    @Override
    public List<Command> nextCommands(Iterable<UserCommand> userCommands) {
        List<Command> commands = new ArrayList<Command>();
        for (int cabinIndex = 0; cabinIndex < cabins.length; cabinIndex++) {
            Cabin cabin = cabins[cabinIndex];

            Weight weight = findMostSuitableUserCommand(cabinIndex, cabin, userCommands);
            if (weight != null) {
                Command command = weight.applyAndGetCommand();

                // discard any applyAndGetCommand belonging to the floor one leaves :)
                if (command == Command.CLOSE) {
                    removeAllCommandsInvolvingFloor(userCommands, cabin.floor());
                }
                commands.add(command);
            } else
                commands.add(Command.NOTHING);
        }
        return commands;
    }

    private void removeAllCommandsInvolvingFloor(Iterable<UserCommand> userCommands, int floor) {
        Iterator<UserCommand> it = userCommands.iterator();
        while (it.hasNext()) {
            UserCommand command = it.next();
            if (command.implyFloor(floor)) {
                log.debug("UserCommand removed {}", command);
                it.remove();
            }
        }
    }


    private Weight findMostSuitableUserCommand(int cabinIndex, Cabin cabin, Iterable<UserCommand> userCommands) {
        if (cabin.isFull() || featureGoFirst) {
            // attempt to empty it by going to the closest queried floor
            Weight cmd = findHeaviest(cabinIndex, cabin, goOnlyForCabin(cabinIndex), userCommands);
            if (cmd != null) {
                return cmd;
            }
        }
        return findHeaviest(cabinIndex, cabin, forCabin(cabinIndex), userCommands);
    }

    private static Filter<UserCommand> forCabin(final int cabinIndex) {
        return new Filter<UserCommand>() {
            @Override
            public boolean accepts(UserCommand cmd) {
                if (cmd.isGo()) {
                    UserCommand.Go go = (UserCommand.Go) cmd;
                    return go.cabin == cabinIndex;
                }
                return true;
            }
        };
    }

    private static Filter<UserCommand> goOnlyForCabin(final int cabinIndex) {
        return new Filter<UserCommand>() {
            @Override
            public boolean accepts(UserCommand cmd) {
                if (cmd.isGo()) {
                    UserCommand.Go go = (UserCommand.Go) cmd;
                    return go.cabin == cabinIndex;
                }
                return false;
            }
        };
    }

    private Weight findHeaviest(int cabinIndex,
                                Cabin cabin,
                                Filter<UserCommand> filter,
                                Iterable<UserCommand> userCommands) {

        Weight weight = null;
        for (UserCommand command : userCommands) {
            if (!filter.accepts(command))
                continue;

            Weight w = calculator.weight(cabinIndex, cabin, command);
            if (w.isHeavierThan(weight))
                weight = w;

            if(w.isExclusive())
                return w;
        }
        return weight;
    }

}
