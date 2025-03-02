package server;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import service.UserService;
import com.google.gson.Gson;
import model.UserData;
import model.AuthData;
import service.UserService;

import spark.*;

public class Server {
    private UserService userService;

    public Server() {
        UserDAO userData = new UserMemoryDAO();
        AuthDAO authData = new AuthMemoryDAO();

        this.userService = new UserService(userData, authData);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request req, Response res){
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = userService.registerUser(user);
        res.status(200);
        return new Gson().toJson(authData);
    }
}
