package erlevator.core;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public abstract class UserCommand {

    public boolean isCall() {
        return this instanceof Call;
    }
    public boolean isGo() {
        return this instanceof Go;
    }

    public boolean implyFloor(int floor) {
        return floorImplied() == floor;
    }
    public abstract int floorImplied();


    public static class Call extends UserCommand {
        public final int atFloor;
        public final Direction dir;

        public Call(int atFloor, Direction dir) {
            this.atFloor = atFloor;
            this.dir = dir;
        }

        @Override
        public int floorImplied() {
            return atFloor;
        }

        @Override
        public String toString() {
            return "Call{" +
                    "atFloor=" + atFloor +
                    ", dir=" + dir +
                    '}';
        }
    }

    public static class Go extends UserCommand {

        public final int cabin;
        public final int floorToGo;

        public Go(int cabin, int floorToGo) {
            this.cabin = cabin;
            this.floorToGo = floorToGo;
        }

        @Override
        public int floorImplied() {
            return floorToGo;
        }

        @Override
        public String toString() {
            return "Go{" +
                    "cabin=" + cabin +
                    ", floorToGo=" + floorToGo +
                    '}';
        }
    }
}
