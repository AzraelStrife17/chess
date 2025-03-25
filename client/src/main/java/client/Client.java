package client;

import model.AuthData;
import model.AuthToken;
import model.LoginRecord;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

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

                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }
    }

    public String register(String... params) throws Exception{
        if (params.length == 3){
            UserData userData = new UserData(params[0], params[1], params[2]);
            currentAuth = server.RegisterResult(userData);
            if (currentAuth != null) {
                state = State.POSTLOGIN;
                userName = params[0];
                return String.format("Logged in as %s.", userName);
            }
            return "username already taken";
        }

        return "Error wrong format";
    }

    public String login(String... params) throws Exception{
        if (params.length == 2){
            LoginRecord loginRecord = new LoginRecord(params[0], params[1]);
            currentAuth = server.LoginResult(loginRecord);
            if (currentAuth != null){
                state = State.POSTLOGIN;
                userName = params[0];
                return String.format("Logged in as %s.", userName);
            }
            return "wrong username or password";
        }
        return "Error wrong format";
    }

    public String logout(){
        if (state == State.POSTLOGIN){
            AuthToken authToken = new AuthToken(currentAuth.authToken());
            server.LogoutResult(authToken);
            state = State.PRELOGIN;
            return "Logged out";
        }
        return "Login to use";
    }

    public String create(String... params) {
        if (state == State.POSTLOGIN) {
            if (params.length == 1) {
                server.CreateGameResult(params[0], currentAuth.authToken());
                return "game created";
            }
            return "Error wrong format";
        }
        return "Login to use";
    }


    public String help() {
        if (state == State.PRELOGIN) {
            return """
                    register <username> <password> <email>
                    login <username> <password>
                    quit
                    """;
        }

        return """
                create <gamename>
                logout
                
                """;
    }
}

