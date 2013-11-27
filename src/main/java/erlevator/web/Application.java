package erlevator.web;

import erlevator.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swoop.Action;
import swoop.Request;
import swoop.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static swoop.Swoop.get;
import static swoop.Swoop.setPort;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        String portS = System.getenv("PORT");
        Integer port = (portS != null) ? Integer.valueOf(portS) : 8080;
        LOG.info("Application port: {}", port);

        ElevatorStrategy strategy = new WeightBasedElevatorStrategy();
        Elevators elevators = new Elevators(strategy, new TimeService());
        elevators.reset(0, 5, 10, 2);
        bindRoutes(new Application(elevators), port);
    }

    private static void bindRoutes(final Application application, int port) {
        setPort(port);

        get(new Action("/nextCommands") {
            @Override
            public void handle(Request request, Response response) {
                application.handleNextCommands(request, response);
            }
        });
        get(new Action("/reset") {
            @Override
            public void handle(Request request, Response response) {
                application.handleReset(request, response);
            }
        });
        get(new Action("/go") {
            @Override
            public void handle(Request request, Response response) {
                application.handleGo(request, response);
            }
        });
        get(new Action("/userHasEntered") {
            @Override
            public void handle(Request request, Response response) {
                application.handleUserHasEntered(request, response);
            }
        });
        get(new Action("/userHasExited") {
            @Override
            public void handle(Request request, Response response) {
                application.handleUserHasExited(request, response);
            }
        });
        get(new Action("/call") {
            @Override
            public void handle(Request request, Response response) {
                application.handleCall(request, response);
            }
        });
        get(new Action("/status") {
            @Override
            public void handle(Request request, Response response) {
                application.handleStatus(request, response);
            }
        });

        LOG.info("Application routes configured");
    }

    private final Elevators elevators;

    public Application(Elevators elevators) {
        this.elevators = elevators;
    }

    private void handleStatus(Request request, Response response) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        Long lastTick = elevators.lastTick();

        StringBuilder body = new StringBuilder();

        int nbCabins = elevators.nbCabins();
        body.append("nbCabins........: ").append(nbCabins).append('\n');
        body.append("lowerFloor......: ").append(elevators.lowerFloor()).append('\n');
        body.append("higherFloor.....: ").append(elevators.higherFloor()).append('\n');
        body.append("last tick.......: ").append(lastTick != null ? df.format(lastTick) : "n/a").append('\n');

        for (int i = 0; i < nbCabins; i++) {
            Cabin cabin = elevators.cabin(i);
            body.append('#').append(i)
                    .append(", floor: ").append(String.format("%3d", cabin.floor()))
                    .append("  ");
            switch (cabin.direction()) {
                case UP:
                    body.append("↑");
                    break;
                case DOWN:
                    body.append("↓");
                    break;
            }
            body.append(" ");

            String door = "[";
            if (cabin.isOpen()) {
                door = "]";
            }
            body.append(door);

            if (cabin.isFull()) {
                body.append(door);
            } else {
                body.append(" ");
            }

            body.append(" ").append(String.format("%3d", cabin.nbUsers())).append(" ");

            door = "]";
            if (cabin.isOpen()) {
                door = "[";
            }

            if (cabin.isFull()) {
                body.append(door);
            } else {
                body.append(" ");
            }
            body.append(door);

            body.append('\n');
        }

        response.status(200);
        response.contentType("text/plain");
        response.body(body.toString());
    }

    private void handleCall(Request request, Response response) {
        // /go?cabin=[0|1]&floorToGo=x
        int atFloor = Integer.parseInt(request.queryParam("atFloor"));
        Direction dir = Direction.from(request.queryParam("to"));
        elevators.call(atFloor, dir);
        response.status(200);
    }

    private void handleGo(Request request, Response response) {
        // /go?cabin=[0|1]&floorToGo=x
        int cabin = Integer.parseInt(request.queryParam("cabin"));
        int floorToGo = Integer.parseInt(request.queryParam("floorToGo"));
        elevators.go(cabin, floorToGo);
        response.status(200);
    }

    private void handleUserHasExited(Request request, Response response) {
        // /userHasExited?cabin=[0|1]
        int cabin = Integer.parseInt(request.queryParam("cabin"));
        elevators.userExited(cabin);
        response.status(200);
    }

    private void handleUserHasEntered(Request request, Response response) {
        // /userHasEntered?cabin=[0|1]
        int cabin = Integer.parseInt(request.queryParam("cabin"));
        elevators.userEntered(cabin);
        response.status(200);
    }


    private void handleReset(Request request, Response response) {
        //reset?lowerFloor=-13&higherFloor=27&cabinSize=40&cabinCount=2&cause=NEW+RULES+FTW
        int lowerFloor = Integer.parseInt(request.queryParam("lowerFloor"));
        int higherFloor = Integer.parseInt(request.queryParam("higherFloor"));
        int cabinSize = Integer.parseInt(request.queryParam("cabinSize"));
        int cabinCount = Integer.parseInt(request.queryParam("cabinCount"));
        elevators.reset(lowerFloor, higherFloor, cabinSize, cabinCount);
        response.status(200);
    }

    private void handleNextCommands(Request request, Response response) {
        List<Command> commands = elevators.nextCommands();
        StringBuilder body = new StringBuilder();
        for (Command command : commands) {
            body.append(command.asString()).append('\n');
        }

        response.status(200);
        response.body(body.toString());
    }

}
