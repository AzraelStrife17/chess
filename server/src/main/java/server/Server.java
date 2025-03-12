package server;
import dataaccess.*;
import model.*;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;
import service.ClearService;
import dataaccess.DatabaseManager;

import spark.*;

import java.util.Map;
import java.util.Objects;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        try {
            UserDAO userData = new MySqlUserdata();  // This can throw DataAccessException
            AuthDAO authData = new SqlAuthdata();  // Same for AuthDAO
            GameDAO gameData = new SqlGamedata();

            this.userService = new UserService(userData, authData);
            this.clearService = new ClearService(userData, authData, gameData);
            this.gameService = new GameService(authData, gameData);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error initializing Server: " + e.getMessage(), e);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            DatabaseManager.createDatabase();
            System.out.println("Database initialized successfully!");
        }
        catch (Exception e) {
            System.err.println("Failed to initialize database.");
        }


        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        if ( user.username() == null || user.username().isBlank()
             || user.password() == null || user.password().isBlank()
             || user.email() == null   || user.email().isBlank()){
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

    private Object loginUser(Request req, Response res) throws DataAccessException {
        var userLogin = new Gson().fromJson(req.body(), LoginRecord.class);
        AuthData authData = userService.loginUser(userLogin);
        if(authData == null){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object logoutUser(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        String deletedAuthToken = userService.logoutUser(authToken);
        if (!Objects.equals(deletedAuthToken, "")){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        res.status(200);
        return "{}";
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        var gameName = new Gson().fromJson(req.body(), GameNameRecord.class);
        if (gameName == null || gameName.gameName().isBlank()){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        String authToken = req.headers("authorization");
        Integer gameId = gameService.createGame(gameName.gameName(), authToken);
        if(gameId == null){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: Unauthorized"));
        }
        GameIdRecord id = new GameIdRecord(gameId);
        res.status(200);
        return new Gson().toJson(id);
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        var joinGameInfo = new Gson().fromJson(req.body(), JoinGameRecord.class);
        if(joinGameInfo.playerColor() == null || joinGameInfo.gameID() == null){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad requests"));
        }
        String authToken = req.headers("authorization");
        String joinGameSuccess = gameService.joinGame(joinGameInfo, authToken);
        if(Objects.equals(joinGameSuccess, "success")){
            res.status(200);
            return "{}";
        }
        else if(Objects.equals(joinGameSuccess, "team color taken")){
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        }

        res.status(401);
        return new  Gson().toJson(Map.of("message", "Error: Unauthorized"));
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        String authToken = req.headers("authorization");
        var list = gameService.listGames(authToken).toArray();
        if(list.length > 0 && ((GameData) list[0]).gameID() == 0){
            res.status(401);
            return new  Gson().toJson(Map.of("message", "Error: Unauthorized"));
        }
        res.status(200);
        return new Gson().toJson(Map.of("games", list));
    }

    private Object clearDatabase(Request req, Response res) throws DataAccessException {
        clearService.clearDatabase();
        res.status(200);
        return "{}";
    }
}
