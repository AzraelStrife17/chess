package client;

import chess.ChessGame;
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
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearAll() throws Exception{
        facade.clearAll();
    }


    @Test
    void register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.RegisterResult(user);
        assertTrue(authData.authToken().length() > 10);
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
    void logout() throws Exception{
        UserData user = new UserData("player2", "password", "p2@email.com");
        var authData = facade.RegisterResult(user);
        AuthToken authToken = new AuthToken(authData.authToken());
        var logoutResult = facade.LogoutResult(authToken);
        assertEquals(null, logoutResult);
    }

    @Test
    void createGame() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        var createGameResult = facade.CreateGameResult("Altair", authData.authToken());
        assertNotNull(createGameResult);
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
    void listGames() throws Exception{
        UserData user = new UserData("EzioAuditore", "RequiescatInPace", "BrotherHood@email.com");
        var authData = facade.RegisterResult(user);
        facade.CreateGameResult("Altair", authData.authToken());
        AuthToken authToken = new AuthToken(authData.authToken());

        var gameList = facade.ListGames(authToken);
        assertNotNull(gameList);

    }


}
