package server;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import service.UserService;
import com.google.gson.Gson;
import model.UserData;
import model.AuthData;
import model.LoginRecord;
import service.ClearService;

import service.UserService;

import spark.*;

import java.util.Map;
import java.util.Objects;

public class Server {
    private UserService userService;
    private ClearService clearService;

    public Server() {
        UserDAO userData = new UserMemoryDAO();
        AuthDAO authData = new AuthMemoryDAO();

        this.userService = new UserService(userData, authData);
        this.clearService = new ClearService(userData, authData);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);

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
        if ( user.username().isBlank() || user.password().isBlank() || user.email().isBlank()){
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

    private Object loginUser(Request req, Response res){
        var userLogin = new Gson().fromJson(req.body(), LoginRecord.class);
        AuthData authData = userService.loginUser(userLogin);
        if(authData == null){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object logoutUser(Request req, Response res){
        String authToken = req.headers("authorization");
        String deletedAuthToken = userService.logoutUser(authToken);
        if (!Objects.equals(deletedAuthToken, "")){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        res.status(200);
        return "{}";
    }

    private Object clearDatabase(Request req, Response res){
        clearService.clearDatabase();
        res.status(200);
        return new Gson().toJson(null);
    }
}
