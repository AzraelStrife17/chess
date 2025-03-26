package client;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthToken;
import model.JoinGameRecord;
import model.LoginRecord;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearAll() throws Exception{
        facade.clearAll();
    }

    @AfterEach
    void clear() throws Exception{
        facade.clearAll();
    }


    @Test
    void register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.RegisterResult(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerDuplicate() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.RegisterResult(user);
        assertThrows(ResponseException.class, () -> {
            facade.RegisterResult(user);
        });
    }

    @Test
    void login() throws Exception{
        UserData user = new UserData("player2", "password", "p2@email.com");
        facade.RegisterResult(user);
        LoginRecord userLogin = new LoginRecord("player2", "password");
        var authData = facade.LoginResult(userLogin);
        assertTrue(authData.authToken().length() > 10);
    }


    @Test
    void loginFail() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.RegisterResult(user);
        LoginRecord loginInfo = new LoginRecord(user.username(), "wrongPassword");
        assertThrows(ResponseException.class, () -> {
            facade.LoginResult(loginInfo);
        });
    }

    @Test
    void logout() throws Exception{
        UserData user = new UserData("player2", "password", "p2@email.com");
        var authData = facade.RegisterResult(user);
        AuthToken authToken = new AuthToken(authData.authToken());
        var logoutResult = facade.LogoutResult(authToken);
        assertNull(logoutResult.authToken());
    }

    @Test
    void logoutFail() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.RegisterResult(user);
        AuthToken authToken = new AuthToken("non-existing-token");
        assertThrows(ResponseException.class, () -> {
            facade.LogoutResult(authToken);
        });
    }

    @Test
    void createGame() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        var createGameResult = facade.CreateGameResult("Altair", authData.authToken());
        assertNotNull(createGameResult);
    }

    @Test
    void createGameFail() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        assertThrows(ResponseException.class, () -> {
            facade.CreateGameResult("", authData.authToken());
        });
    }

    @Test
    void joinGame() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        var createResult = facade.CreateGameResult("Altair", authData.authToken());

        JoinGameRecord joinInfo = new JoinGameRecord(ChessGame.TeamColor.WHITE, createResult.gameID(), authData.authToken());
        var joinGameResult = facade.JoinGameResult(joinInfo);

        assertNull(joinGameResult.authToken());

    }

    @Test
    void joinGameFailAlreadyTaken() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        var createResult = facade.CreateGameResult("Altair", authData.authToken());

        JoinGameRecord joinInfo = new JoinGameRecord(ChessGame.TeamColor.WHITE, createResult.gameID(), authData.authToken());

        facade.JoinGameResult(joinInfo);

        assertThrows(ResponseException.class, () -> {
            facade.JoinGameResult(joinInfo);
        });

    }

    @Test
    void listGames() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        facade.CreateGameResult("Altair", authData.authToken());
        AuthToken authToken = new AuthToken(authData.authToken());

        var gameList = facade.ListGames(authToken);
        assertNotNull(gameList);

    }

    @Test
    void listGamesFail() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        facade.CreateGameResult("Altair", authData.authToken());
        AuthToken authToken = new AuthToken("dummyToken");

        assertThrows(ResponseException.class, () -> {
            facade.ListGames(authToken);
        });

    }




}
