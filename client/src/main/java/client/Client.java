package client;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import websocket.ServerMessageHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

import static client.DrawBoard.*;

public class Client {
    private String userName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private AuthData currentAuth;
    private final ServerMessageHandler serverMessageHandler;
    private WebSocketFacade ws;


    public Client(String serverUrl, ServerMessageHandler serverMessageHandler) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.serverMessageHandler = serverMessageHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "listgames" -> listGames();
                case "playgame" -> playGame(params);
                case "observegame" -> observeGame(params);
                case "makemove" -> makeMove(params);
                case "quit" -> "quit";

                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String register(String... params) {
        if (params.length == 3){
            UserData userData = new UserData(params[0], params[1], params[2]);
            try {
                currentAuth = server.registerResult(userData);
                if (currentAuth != null) {
                    state = State.POSTLOGIN;
                    userName = params[0];
                    return String.format("Logged in as %s.", userName);
                }
            }
            catch (ResponseException ex){
                return ex.getMessage();
            }
        }

        return "Error wrong format";
    }

    public String login(String... params) throws Exception{
        if (params.length == 2){
            LoginRecord loginRecord = new LoginRecord(params[0], params[1]);
            try {
                currentAuth = server.loginResult(loginRecord);
                if (currentAuth != null) {
                    state = State.POSTLOGIN;
                    userName = params[0];
                    return String.format("Logged in as %s.", userName);
                }
            }
            catch (ResponseException ex){
                return ex.getMessage();
            }
        }
        return "Error wrong format";
    }

    public String logout() throws ResponseException {
        if (state == State.POSTLOGIN){
            try {
                AuthToken authToken = new AuthToken(currentAuth.authToken());
                server.logoutResult(authToken);
                state = State.PRELOGIN;
                return "Logged out";
            }
            catch (ResponseException ex){
                return ex.getMessage();
            }
        }
        return "Login to use";
    }

    public String create(String... params) throws ResponseException {
        if (state == State.POSTLOGIN) {
            try {
                if (params.length == 1) {
                    server.createGameResult(params[0], currentAuth.authToken());
                    return "game created";
                }
            } catch(ResponseException ex){
                return ex.getMessage();
            }
        }
        return "Login to use";
    }

    public String listGames() throws ResponseException {
        if (state == State.POSTLOGIN) {
            try {
                AuthToken authToken = new AuthToken(currentAuth.authToken());
                var games = server.listGames(authToken);
                var result = new StringBuilder();
                server.clearDisplayIdMap();
                for (var game : games) {
                    int gameID = game.gameID();
                    int displayID = server.createDisplayID(gameID);
                    result.append("Game ID: ").append(displayID).
                            append("    White: ").append(game.whiteUsername()).
                            append("    Black: ").append(game.blackUsername()).
                            append("    Game name: ").append(game.gameName()).
                            append("\n");
                }
                return result.toString();
            }
            catch(ResponseException ex) {
                return ex.getMessage();
            }
        }
        return "Login to use";
    }

    public String playGame(String... params) throws ResponseException {
        if (state == State.POSTLOGIN){
            try {
                if (params.length == 2) {
                    ChessGame.TeamColor team;
                    int id;

                    try {
                        id = Integer.parseInt(params[0]);
                        team = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return "Error: Invalid team color or layout, proper format is <gameID> <white/black>.";
                    }

                    int displayID = server.getGameID(id);
                    if(displayID == 0){
                        return "Error: ID does not exist";
                    }

                    JoinGameRecord joinInfo = new JoinGameRecord(team, id, currentAuth.authToken());
                    server.joinGameResult(joinInfo);
                    ws.connectToGame(joinInfo.authToken(), joinInfo.gameID());
                    String board = drawChessBoard(params[1]);
                    System.out.println(board);
                    state = State.GAMESTATE;
                    return String.format("playing as %s.", params[1]);
                }
            }
            catch(ResponseException ex) {
                return ex.getMessage();
            }
            return "Error: BadArgument";
        }
        return "Login to use";
    }

    public String makeMove(String... params){
        if (state == state.GAMESTATE){
            return null;
        }
        else return "must join a game to use this method";
    }



    public String observeGame(String... params) throws ResponseException {
        if (state == State.POSTLOGIN) {

            int id;

            try {
                id = Integer.parseInt(params[0]);
            }catch (IllegalArgumentException e) {
                return "Error: Invalid team color or layout, proper format is <gameID> <white/black>.";
            }

            int displayID = server.getGameID(id);
            if(displayID == 0){
                return "Error: ID does not exist";
            }

            String board = drawChessBoard("white");
            System.out.println(board);

            return String.format("watching game %s", params[0]);
        }
        return "Login to use";
    }

    public String help() {
        if (state == State.PRELOGIN) {
            return """
                    register <username> <password> <email>
                    login <username> <password>
                    help
                    quit
                    """;
        }

        if (state == State.POSTLOGIN) {
            return """
                    create <gamename>
                    listgames
                    playgame <gameID> <teamColor>
                    observegame <gameID>
                    help
                    logout
                    
                    """;
        }

        else{
            return """
                    makemove <startingpostion> <endpostion>
                    """;
        }
    }

}

