package erlevator.web;

import erlevator.core.Command;
import erlevator.core.Direction;
import erlevator.core.Elevators;
import swoop.Action;
import swoop.Request;
import swoop.Response;

import java.util.List;

import static swoop.Swoop.get;
import static swoop.Swoop.setPort;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Application {
    public static void main(String[] args) throws Exception {
        final Application application = new Application(new Elevators());

        Integer port = Integer.valueOf(System.getenv("PORT"));
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
    }

    private final Elevators elevators;

    public Application(Elevators elevators) {
        this.elevators = elevators;
    }

    private void handleCall(Request request, Response response) {
        // /go?cabin=[0|1]&floorToGo=x
        int atFloor = Integer.parseInt(request.queryParam("atFloor"));
        Direction dir = Direction.from(request.queryParam("to"));
        elevators.call(atFloor, dir);
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
