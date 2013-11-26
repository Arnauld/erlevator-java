package erlevator.core;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public abstract class UserCommand {
    public static class Call extends UserCommand {
        public final int atFloor;
        public final Direction dir;

        public Call(int atFloor, Direction dir) {
            this.atFloor = atFloor;
            this.dir = dir;
        }
    }

    public static class Go extends UserCommand {

        public final int cabin;
        public final int floorToGo;

        public Go(int cabin, int floorToGo) {
            this.cabin = cabin;
            this.floorToGo = floorToGo;
        }
    }
}
