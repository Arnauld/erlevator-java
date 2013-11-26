package erlevator.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ElevatorsSimulator {
/*

--------- Status ----------
Tick.............: 500
Arrived..........: 97
Min. wait time...: 17
Max. wait time...: 488
Avg. wait time...: 217

 */

    public static void main(String[] main) {
        //Elevators elevators = new Elevators(new OmnibusStrategy());
        Elevators elevators = new Elevators(new RawCommandPriorityStrategy());
        elevators.reset(-13, 27, 10, 2);
        ElevatorsSimulator simulator = new ElevatorsSimulator(elevators, 150);
        simulator.ticks(500);
        simulator.tick();
    }


    public static class Agent {
        public final int startFloor;
        public final int endFloor;
        public int ticksWaiting;
        public Integer inCabin = null;

        public Agent(int startFloor, int endFloor) {
            this.startFloor = startFloor;
            this.endFloor = endFloor;
        }

        public void enter(int cabin) {
            inCabin = cabin;
        }

        public Direction direction() {
            if (endFloor > startFloor)
                return Direction.UP;
            else
                return Direction.DOWN;
        }

        public void tick() {
            ticksWaiting++;
        }

        public boolean isInCabin() {
            return inCabin != null;
        }
    }

    private List<Agent> agents = new ArrayList<Agent>();
    private Elevators elevators;
    private int nbAgentsMax = 5;
    private Random random = new Random(17L);
    private int nbArrived = 0;
    private long totalWaittime = 0;
    private long minWaitime = Integer.MAX_VALUE;
    private long maxWaitime = Integer.MIN_VALUE;
    private int tick = 0;

    public ElevatorsSimulator(Elevators elevators, int nbAgentsMax) {
        this.elevators = elevators;
        this.nbAgentsMax = nbAgentsMax;
    }

    public void ticks(int nbTicks) {
        for (int i = 0; i < nbTicks; i++){
            tick();
            tick++;
        }
    }

    public void tick() {
        if (agents.size() < nbAgentsMax) {
            generateNewAgents();
        }

        Iterator<Agent> it = agents.iterator();
        while (it.hasNext()) {
            Agent agent = it.next();
            agent.tick();
            if (agent.isInCabin()) {
                Cabin cabin = elevators.cabin(agent.inCabin);
                if (cabin.floor() == agent.endFloor && cabin.isOpen()) {
                    elevators.userExited(agent.inCabin);
                    nbArrived ++ ;

                    int ticksWaiting = agent.ticksWaiting;
                    totalWaittime += ticksWaiting;
                    if(ticksWaiting > maxWaitime)
                        maxWaitime = ticksWaiting;
                    if(ticksWaiting < minWaitime)
                        minWaitime = ticksWaiting;

                    it.remove();
                }
                continue;
            }

            for (int i = 0; i < elevators.nbCabins(); i++) {
                Cabin cabin = elevators.cabin(i);
                if (cabin.isOpen() && !cabin.isFull() && agent.startFloor == cabin.floor()) {
                    elevators.userEntered(i);
                    agent.enter(i);
                    elevators.go(i, agent.endFloor);
                    break;
                }
            }
        }

        dumpElevator();
        List<Command> commands = elevators.nextCommands();
        System.out.println(commands);
    }

    private void dumpElevator() {
        System.out.println("--------- Status ----------");
        System.out.println("Tick.............: " + tick);
        System.out.println("Arrived..........: " + nbArrived);
        System.out.println("Min. wait time...: " + minWaitime);
        System.out.println("Max. wait time...: " + maxWaitime);
        System.out.println("Avg. wait time...: " + (nbArrived>0?(totalWaittime / nbArrived):0));
        for(int floor = elevators.higherFloor(); floor>=elevators.lowerFloor(); floor--) {
            StringBuilder floorData = new StringBuilder();
            floorData.append(String.format("%2d", floor)).append(": ");

            int nbAgentWaiting = 0;
            for(Agent agent: agents) {
                if(agent.startFloor == floor && !agent.isInCabin())
                    nbAgentWaiting++;
            }
            floorData.append("[").append(String.format("%2d", nbAgentWaiting)).append("] ");

            for(int i = 0; i< elevators.nbCabins(); i++) {
                Cabin cabin = elevators.cabin(i);

                if(cabin.floor() == floor) {
                    if(cabin.isOpen())
                        floorData.append("]");
                    else
                        floorData.append("[");
                    floorData.append(String.format("%2d", cabin.nbUsers()));
                    if(cabin.isOpen())
                        floorData.append("[");
                    else
                        floorData.append("]");
                }
                else {
                    floorData.append("    ");
                }
                floorData.append("  ");
            }
            System.out.println(floorData);
        }
    }

    private void generateNewAgents() {
        // no more than 5 new agents at the same time...
        int nbAgents = random.nextInt(Math.max(5, nbAgentsMax - agents.size()));
        for (int i = 0; i < nbAgents; i++) {
            int startFloor = elevators.lowerFloor() + random.nextInt(elevators.higherFloor() - elevators.lowerFloor());
            int endFloor = startFloor;
            while (endFloor == startFloor) {
                endFloor = elevators.lowerFloor() + random.nextInt(elevators.higherFloor() - elevators.lowerFloor());
            }
            Agent agent = new Agent(startFloor, endFloor);
            elevators.call(agent.startFloor, agent.direction());
            agents.add(agent);
        }
    }
}
