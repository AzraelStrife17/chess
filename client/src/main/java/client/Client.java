package client;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.util.Arrays;

import static client.DrawBoard.*;

public class Client {
    private String userName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private AuthData currentAuth;

    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
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
                currentAuth = server.RegisterResult(userData);
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
                currentAuth = server.LoginResult(loginRecord);
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
                server.LogoutResult(authToken);
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
                    server.CreateGameResult(params[0], currentAuth.authToken());
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
                var games = server.ListGames(authToken);
                var result = new StringBuilder();
                for (var game : games) {
                    result.append("Game ID: ").append(game.gameID()).
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
                    ChessGame.TeamColor team = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
                    int id = Integer.parseInt(params[0]);
                    JoinGameRecord joinInfo = new JoinGameRecord(team, id, currentAuth.authToken());
                    server.JoinGameResult(joinInfo);
                    String board = drawChessBoard(params[1]);
                    System.out.println(board);
                    state = State.GAMESTATE;
                    return String.format("playing as %s.", params[1]);
                }
            }
            catch(ResponseException ex) {
                return ex.getMessage();
            }
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

        return """
                create <gamename>
                listgames
                playgame <gameID> <teamColor>
                help
                logout
                
                """;
    }
}

