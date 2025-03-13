package service;
import chess.ChessGame.TeamColor;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SqlTests {
    UserDAO userData = new MySqlUserdata();
    AuthDAO authData = new SqlAuthdata();
    GameDAO gameData = new SqlGamedata();

    private final UserService userService = new UserService(userData, authData);
    private final GameService gameService = new GameService(authData, gameData);
    private final ClearService clearService = new ClearService(userData, authData, gameData);

    public SqlTests() throws DataAccessException {
    }

    @Test
    void registerUserSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("JAMES", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        assertNotNull(authData.authToken());
        assertEquals("JAMES", authData.username());
        clearService.clearDatabase();
    }
    @Test
    void registerUserAlreadyTaken() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user1);

        var user2 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData2 = userService.registerUser(user2);
        assertNull(authData2);
        clearService.clearDatabase();
    }

    @Test
    void loginUserSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = userService.loginUser(userLogin);
        assertNotNull(authData.authToken());
        assertEquals("James", authData.username());
        clearService.clearDatabase();
    }

    @Test
    void loginUserFailedPassword() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "000");
        AuthData authData = userService.loginUser(userLogin);
        assertNull(authData);
        clearService.clearDatabase();
    }

    @Test
    void logoutUserSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = userService.loginUser(userLogin);

        String authToken = userService.logoutUser(authData.authToken());

        assertEquals("", authToken);
        clearService.clearDatabase();
    }

    @Test
    void logoutUserFail() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        userService.loginUser(userLogin);

        String authToken = userService.logoutUser("cf6f6db8-38cb-446a-9589-8049b0154009");

        assertNotEquals("", authToken);
        clearService.clearDatabase();
    }

    @Test
    void createGameSuccessful() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("BumbleBee", "BEEEZ", "Camaro@gmail.com");
        AuthData authData = userService.registerUser(user);
        var gameName = new GameNameRecord("TestName");
        Integer gameID = gameService.createGame(gameName.gameName(), authData.authToken());
        clearService.clearDatabase();
        assertNotNull(gameID);
    }

    @Test
    void createUnauthorizedTest() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);
        var gameName1 = new GameNameRecord("FirstName");
        Integer gameID = gameService.createGame(gameName1.gameName(), "a");
        clearService.clearDatabase();
        assertNull(gameID);
    }

    @Test
    void joinWhiteBlackTestSuccess() throws DataAccessException, SQLException {
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

    @Test
    void joinBlackWithBlackTaken() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = userService.registerUser(user1);

        Integer gameID = gameService.createGame("GameTestName", authData1.authToken());

        var joinGameInfoBlack = new JoinGameRecord(TeamColor.BLACK, gameID);
        String successfulJoin1 = gameService.joinGame(joinGameInfoBlack, authData1.authToken());

        var user2 = new UserData("Bond", "007", "JAMES@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.BLACK, gameID);
        String failJoin = gameService.joinGame(joinGameInfo2, authData2.authToken());
        clearService.clearDatabase();

        assertEquals("success", successfulJoin1);
        assertEquals("team color taken", failJoin);
    }

    @Test
    void getListSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        gameService.createGame("GameTestName", authData.authToken());

        Collection<GameData> gameList = gameService.listGames(authData.authToken());
        clearService.clearDatabase();

        assertEquals(1, gameList.size());

    }

    @Test
    void getListUnauthorizedAuth() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);

        Collection<GameData> gameList = gameService.listGames("unauthorized");
        Collection<GameData> expectedList = List.of(new GameData(0, null, null, null, null));

        assertEquals(expectedList, gameList);
        clearService.clearDatabase();
    }

    @Test
    void clearTestSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData registerAuthData = userService.registerUser(user);

        gameService.createGame("GameTestName", registerAuthData.authToken());

        clearService.clearDatabase();

        Collection<GameData> testList = gameData.listGames();
        assertEquals(0, testList.size());

        var gameName = new GameNameRecord("FirstName");
        Integer gameID = gameService.createGame(gameName.gameName(), registerAuthData.authToken());
        assertNull(gameID);

        var userLogin = new LoginRecord("James", "007");
        AuthData loginAuthData = userService.loginUser(userLogin);
        assertNull(loginAuthData);
        clearService.clearDatabase();

    }

    @Test
    void clearAllWhenEmpty() throws DataAccessException, SQLException {

        clearService.clearDatabase();

        Collection<GameData> testList = gameData.listGames();
        assertEquals(0, testList.size());

        var gameName = new GameNameRecord("FirstName");
        Integer gameID = gameService.createGame(gameName.gameName(), "emptyAuthToken");
        assertNull(gameID);

        var userLogin = new LoginRecord("James", "007");
        AuthData loginAuthData = userService.loginUser(userLogin);
        assertNull(loginAuthData);
        clearService.clearDatabase();
    }
    }
