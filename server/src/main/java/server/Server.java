package server;
import dataaccess.*;
import model.*;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;
import service.ClearService;
import dataaccess.DatabaseManager;

import spark.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import server.websocket.WebSocketHandler;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class Server {
    private final WebSocketHandler webSocketHandler;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;


    public Server() {
        try {
            UserDAO userData = new MySqlUserdata();  // This can throw DataAccessException
            AuthDAO authData = new SqlAuthdata();  // Same for AuthDAO
            GameDAO gameData = new SqlGamedata();

            webSocketHandler = new WebSocketHandler(authData, gameData);
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
        Spark.webSocket("/ws", webSocketHandler);
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
            return new Gson().toJson(Map.of("message", "Error: Wrong Username or Password"));
        }
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object logoutUser(Request req, Response res) throws DataAccessException, SQLException {
        var authToken = new Gson().fromJson(req.body(), AuthToken.class);

        String deletedAuthToken;
        if (authToken == null){
            res.type("application/json");
            String authTokenString = req.headers("authorization");
            AuthToken authToken2 = new AuthToken(authTokenString);
            deletedAuthToken = userService.logoutUser(authToken2);
        }
        else{
            deletedAuthToken = userService.logoutUser(authToken);
        }


        if (!Objects.equals(deletedAuthToken, "")){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        res.status(200);
        return "{}";
    }

    private Object createGame(Request req, Response res) throws DataAccessException {


        var createdGame = new Gson().fromJson(req.body(), CreateGameData.class);

        if (createdGame.authToken() == null){
            res.type("application/json");
            String authTokenString = req.headers("authorization");
            AuthToken authToken = new AuthToken(authTokenString);
            createdGame = new CreateGameData(createdGame.gameName(), authToken.authToken());
        }


        if (createdGame.gameName().isBlank() || createdGame.authToken().isBlank()){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        Integer gameId = gameService.createGame(createdGame);
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
        if (joinGameInfo.authToken() == null){
            res.type("application/json");
            String authTokenString = req.headers("authorization");
            AuthToken authToken = new AuthToken(authTokenString);
            joinGameInfo = new JoinGameRecord(joinGameInfo.playerColor(), joinGameInfo.gameID(), authToken.authToken());
        }
        if(joinGameInfo.playerColor() == null || joinGameInfo.gameID() == null
        || (joinGameInfo.playerColor() != WHITE && joinGameInfo.playerColor() != BLACK)){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad requests"));
        }
        JoinGameResponse joinGameSuccess = gameService.joinGame(joinGameInfo);
        if(Objects.equals(joinGameSuccess.result(), "success")){
            res.status(200);
            return new Gson().toJson(joinGameSuccess);
        }
        else if(Objects.equals(joinGameSuccess.result(), "team color taken")){
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        }

        res.status(401);
        return new Gson().toJson(Map.of("message", "Error: team color does not exist"));
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        String authTokenString = req.headers("authorization");
        AuthToken authToken = new AuthToken(authTokenString);
        var list = gameService.listGames(authToken).toArray();
        if(list.length > 0 && ((GameData) list[0]).gameID() == 0){
            res.status(401);
            return new  Gson().toJson(Map.of("message", "Error: Unauthorized"));
        }
        res.status(200);
        return new Gson().toJson(Map.of("games", list));
    }

    private Object clearDatabase(Request req, Response res) throws DataAccessException, SQLException {
        clearService.clearDatabase();
        res.status(200);
        return "{}";
    }
}
