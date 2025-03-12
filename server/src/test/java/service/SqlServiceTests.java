package service;
import dataaccess.*;
import chess.ChessGame.TeamColor;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class SqlServiceTests {
    UserDAO userData = new MySqlUserdata();
    AuthDAO authData = new SqlAuthdata();
    GameDAO gameData = new SqlGamedata();

    private final UserService userService = new UserService(userData, authData);
    private final GameService gameService = new GameService(authData, gameData);
    private final ClearService clearService = new ClearService(userData, authData, gameData);

    public SqlServiceTests() throws DataAccessException {
    }

    @Test
    void createGameSuccessful() throws DataAccessException {
        var user = new UserData("BumbleBee5", "BEEEZ", "Camaro@gmail.com");
        AuthData authData = userService.registerUser(user);
        var gameName = new GameNameRecord("TestName");
        Integer gameID = gameService.createGame(gameName.gameName(), authData.authToken());
        assertNotNull(gameID);
    }

    @Test
    void joinBlackTestSuccess() throws DataAccessException {
        var user = new UserData("Megatron3", "cyber", "megatronus@gmail.com");
        AuthData authData = userService.registerUser(user);

        Integer gameID = gameService.createGame("GameTestName", authData.authToken());

        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID);
        String successfulJoin = gameService.joinGame(joinGameInfo, authData.authToken());
        assertEquals("success", successfulJoin);
    }

    @Test
    void joinWhiteTestSuccess() throws DataAccessException {
        var user = new UserData("OptimusPrime2", "cyber", "megatronus@gmail.com");
        AuthData authData = userService.registerUser(user);


        var joinGameInfo = new JoinGameRecord(TeamColor.WHITE, 8);
        String successfulJoin = gameService.joinGame(joinGameInfo, authData.authToken());
        assertEquals("success", successfulJoin);
    }
}
