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
    void registerUserSuccess() throws DataAccessException {
        clearService.clearDatabase();
        var user = new UserData("JAMES", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        assertNotNull(authData.authToken());
        assertEquals("JAMES", authData.username());
        clearService.clearDatabase();
    }
    @Test
    void registerUserAlreadyTaken() throws DataAccessException {
        clearService.clearDatabase();
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user1);

        var user2 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData2 = userService.registerUser(user2);
        assertNull(authData2);
        clearService.clearDatabase();
    }


    @Test
    void createGameSuccessful() throws DataAccessException {
        clearService.clearDatabase();
        var user = new UserData("BumbleBee", "BEEEZ", "Camaro@gmail.com");
        AuthData authData = userService.registerUser(user);
        var gameName = new GameNameRecord("TestName");
        Integer gameID = gameService.createGame(gameName.gameName(), authData.authToken());
        clearService.clearDatabase();
        assertNotNull(gameID);
    }

    @Test
    void joinWhiteBlackTestSuccess() throws DataAccessException {
        clearService.clearDatabase();
        var user = new UserData("Megatron", "cyber", "megatronus@gmail.com");
        AuthData authData = userService.registerUser(user);

        Integer gameID = gameService.createGame("GameTestName", authData.authToken());

        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID);
        String successfulJoin = gameService.joinGame(joinGameInfo, authData.authToken());
        assertEquals("success", successfulJoin);

        var user2 = new UserData("OptimusPrime", "cyber", "megatronus@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.WHITE,gameID);
        String successfulJoin2 = gameService.joinGame(joinGameInfo2, authData2.authToken());
        assertEquals("success", successfulJoin2);
        clearService.clearDatabase();
    }

}
