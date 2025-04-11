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
        AuthToken authToken = new AuthToken(authData.authToken());

        String finalAuthToken = userService.logoutUser(authToken);

        assertEquals("", finalAuthToken);
        clearService.clearDatabase();
    }

    @Test
    void logoutUserFail() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        userService.loginUser(userLogin);
        AuthToken authToken = new AuthToken("unauthorized");

        String finalAuthToken = userService.logoutUser(authToken);

        assertNotEquals("", finalAuthToken);
        clearService.clearDatabase();
    }

    @Test
    void createGameSuccessful() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("BumbleBee", "BEEEZ", "Camaro@gmail.com");
        AuthData authData = userService.registerUser(user);
        var gameCreated = new CreateGameData("TestName", authData.authToken());

        Integer gameID = gameService.createGame(gameCreated);
        clearService.clearDatabase();
        assertNotNull(gameID);
    }

    @Test
    void createUnauthorizedTest() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);
        var game1 = new CreateGameData("FirstName", "a");
        Integer gameID = gameService.createGame(game1);
        clearService.clearDatabase();
        assertNull(gameID);
    }

    @Test
    void joinWhiteBlackTestSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("Megatron", "cyber", "megatronus@gmail.com");
        AuthData authData = userService.registerUser(user);
        var gameCreated = new CreateGameData("GameTestName", authData.authToken());
        Integer gameID = gameService.createGame(gameCreated);

        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID, authData.authToken());
        JoinGameResponse successfulJoin = gameService.joinGame(joinGameInfo);
        assertEquals("success", successfulJoin.result());

        var user2 = new UserData("OptimusPrime", "cyber", "megatronus@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.WHITE,gameID,  authData2.authToken());
        JoinGameResponse successfulJoin2 = gameService.joinGame(joinGameInfo2);
        assertEquals("success", successfulJoin2.result());
        clearService.clearDatabase();
    }

    @Test
    void joinBlackWithBlackTaken() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = userService.registerUser(user1);
        var createdGame = new CreateGameData("GameTestName", authData1.authToken());
        Integer gameID = gameService.createGame(createdGame);

        var joinGameInfoBlack = new JoinGameRecord(TeamColor.BLACK, gameID, authData1.authToken());
        JoinGameResponse successfulJoin1 = gameService.joinGame(joinGameInfoBlack);

        var user2 = new UserData("Bond", "007", "JAMES@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.BLACK, gameID, authData2.authToken());
        JoinGameResponse failJoin = gameService.joinGame(joinGameInfo2);
        clearService.clearDatabase();

        assertEquals("success", successfulJoin1.result());
        assertEquals("team color taken", failJoin.result());
    }

    @Test
    void getListSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);
        var createdGame = new CreateGameData("GameTestName", authData.authToken());
        gameService.createGame(createdGame);
        AuthToken authToken = new AuthToken(authData.authToken());

        Collection<GameData> gameList = gameService.listGames(authToken);
        clearService.clearDatabase();

        assertEquals(1, gameList.size());

    }

    @Test
    void getListUnauthorizedAuth() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);

        AuthToken authToken = new AuthToken("unauthorized");

        Collection<GameData> gameList = gameService.listGames(authToken);
        Collection<GameData> expectedList = List.of(new GameData(0, null, null, null, null));

        assertEquals(expectedList, gameList);
        clearService.clearDatabase();
    }

    @Test
    void clearTestSuccess() throws DataAccessException, SQLException {
        clearService.clearDatabase();
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData registerAuthData = userService.registerUser(user);
        var createdGame = new CreateGameData("GameTestName", registerAuthData.authToken());
        gameService.createGame(createdGame);

        clearService.clearDatabase();

        Collection<GameData> testList = gameData.listGames();
        assertEquals(0, testList.size());

        var gameCreated = new CreateGameData("FirstName", registerAuthData.authToken());
        Integer gameID = gameService.createGame(gameCreated);
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

        var createdGame = new CreateGameData("FirstName","emptyAuthToken");
        Integer gameID = gameService.createGame(createdGame);
        assertNull(gameID);

        var userLogin = new LoginRecord("James", "007");
        AuthData loginAuthData = userService.loginUser(userLogin);
        assertNull(loginAuthData);
        clearService.clearDatabase();
    }
    }
