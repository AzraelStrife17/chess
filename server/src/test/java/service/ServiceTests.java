package service;
import dataaccess.*;
import chess.ChessGame.TeamColor;
import model.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    UserDAO userData = new UserMemoryDAO();
    AuthDAO authData = new AuthMemoryDAO();
    GameDAO gameData = new GameMemoryDAO();

    private final UserService userService = new UserService(userData, authData);
    private final GameService gameService = new GameService(authData, gameData);
    private final ClearService clearService = new ClearService(userData, authData, gameData);

    public ServiceTests() {
    }


    @Test
    void registerUserSuccess() throws DataAccessException {
        var user = new UserData("JAMES", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        assertNotNull(authData.authToken());
        assertEquals("JAMES", authData.username());
    }

    @Test
    void registerUserAlreadyTaken() throws DataAccessException {
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user1);

        var user2 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData2 = userService.registerUser(user2);
        assertNull(authData2);
    }

    @Test
    void loginUserSuccess() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = userService.loginUser(userLogin);
        assertNotNull(authData.authToken());
        assertEquals("James", authData.username());
    }

    @Test
    void loginUserFailedPassword() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "000");
        AuthData authData = userService.loginUser(userLogin);
        assertNull(authData);
    }

    @Test
    void loginUserFailedUsername() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("SolidSnake", "007");
        AuthData authData = userService.loginUser(userLogin);
        assertNull(authData);
    }

    @Test
    void logoutUserSuccess() throws DataAccessException, SQLException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = userService.loginUser(userLogin);
        AuthToken extractedToken = new AuthToken(authData.authToken());
        String authToken = userService.logoutUser(extractedToken);

        assertEquals("", authToken);

    }

    @Test
    void logoutUserFail() throws DataAccessException, SQLException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        userService.loginUser(userLogin);
        AuthToken extractedToken = new AuthToken("unauthorized");
        String authToken = userService.logoutUser(extractedToken);

        assertNotEquals("", authToken);

    }

    @Test
    void createGameSuccessful() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);
        var createdGame = new CreateGameData("TestName", authData.authToken());
        Integer gameID = gameService.createGame(createdGame);
        assertNotNull(gameID);
    }

    @Test
    void create2GameSuccessful() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        var createdGame = new CreateGameData("TestName", authData.authToken());
        gameService.createGame(createdGame);

        var createdGame2 = new CreateGameData("TestName2", authData.authToken());
        Integer gameID = gameService.createGame(createdGame2);
        assertNotNull(gameID);
    }

    @Test
    void createUnauthorizedTest() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);
        var createdGame = new CreateGameData("TestName", "a");
        Integer gameID = gameService.createGame(createdGame);
        assertNull(gameID);
    }

    @Test
    void joinBlackTestSuccess() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);
        var createdGame = new CreateGameData("TestName", authData.authToken());
        Integer gameID = gameService.createGame(createdGame);

        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID, authData.authToken());
        String successfulJoin = gameService.joinGame(joinGameInfo);
        assertEquals("success", successfulJoin);
    }

    @Test
    void joinWhiteTestSuccess() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);
        var createdGame = new CreateGameData("TestName", authData.authToken());
        Integer gameID = gameService.createGame(createdGame);

        var joinGameInfo = new JoinGameRecord(TeamColor.WHITE, gameID, authData.authToken());
        String successfulJoin = gameService.joinGame(joinGameInfo);
        assertEquals("success", successfulJoin);
    }

    @Test
    void joinWhiteAndBlackSuccess() throws DataAccessException {
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = userService.registerUser(user1);

        var createdGame = new CreateGameData("TestName", authData1.authToken());
        Integer gameID = gameService.createGame(createdGame);

        var joinGameInfoBlack = new JoinGameRecord(TeamColor.BLACK, gameID, authData1.authToken());
        String successfulJoin1 = gameService.joinGame(joinGameInfoBlack);

        var user2 = new UserData("Bond", "007", "JAMES@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.WHITE, gameID, authData2.authToken());
        String successfulJoin2 = gameService.joinGame(joinGameInfo2);

        assertEquals("success", successfulJoin1);
        assertEquals("success", successfulJoin2);
    }

    @Test
    void joinBlackWithBlackTaken() throws DataAccessException {
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = userService.registerUser(user1);

        var createdGame = new CreateGameData("TestName", authData1.authToken());
        Integer gameID = gameService.createGame(createdGame);

        var joinGameInfoBlack = new JoinGameRecord(TeamColor.BLACK, gameID, authData1.authToken());
        String successfulJoin1 = gameService.joinGame(joinGameInfoBlack);

        var user2 = new UserData("Bond", "007", "JAMES@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.BLACK, gameID, authData2.authToken());
        String failJoin = gameService.joinGame(joinGameInfo2);

        assertEquals("success", successfulJoin1);
        assertEquals("team color taken", failJoin);
    }

    @Test
    void getListSuccess() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        var createdGame = new CreateGameData("TestName", authData.authToken());
        gameService.createGame(createdGame);
        AuthToken authToken = new AuthToken(authData.authToken());

        Collection<GameData> gameList = gameService.listGames(authToken);

        assertEquals(1, gameList.size());

    }

    @Test
    void getListUnauthorizedAuth() throws DataAccessException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);
        AuthToken authToken = new AuthToken("unauthorized");

        Collection<GameData> gameList = gameService.listGames(authToken);
        Collection<GameData> expectedList = List.of(new GameData(0, null, null, null, null));

        assertEquals(expectedList, gameList);
    }

    @Test
    void clearTestSuccess() throws DataAccessException, SQLException {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData registerAuthData = userService.registerUser(user);

        var createdGame = new CreateGameData("TestName", registerAuthData.authToken());
        Integer gameID = gameService.createGame(createdGame);

        clearService.clearDatabase();

        Collection<GameData> testList = gameData.listGames();
        assertEquals(0, testList.size());

        var createdGame2 = new CreateGameData("TestName", registerAuthData.authToken());
        Integer gameID2 = gameService.createGame(createdGame2);
        assertNull(gameID2);

        var userLogin = new LoginRecord("James", "007");
        AuthData loginAuthData = userService.loginUser(userLogin);
        assertNull(loginAuthData);


    }

    @Test
    void clearAllWhenEmpty() throws DataAccessException, SQLException {

        clearService.clearDatabase();

        Collection<GameData> testList = gameData.listGames();
        assertEquals(0, testList.size());

        var createdGame = new CreateGameData("TestName", "emptyAuthToken");
        Integer gameID = gameService.createGame(createdGame);
        assertNull(gameID);

        var userLogin = new LoginRecord("James", "007");
        AuthData loginAuthData = userService.loginUser(userLogin);
        assertNull(loginAuthData);

    }
}
