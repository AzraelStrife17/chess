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

import java.util.Map;

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

        if (user.username() == null || user.username().isEmpty()
                || user.password() == null || user.password().isEmpty()
                ||user.email() == null || user.email().isEmpty()){
                    res.status(400);
                    return new Gson().toJson(Map.of("message", "Error: bad request"));
        }


        AuthData authData = userService.registerUser(user);
        if(authData == null){
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        }
        res.status(200);
        return new Gson().toJson(authData);
    }
}
