package service;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import dataaccess.GameDAO;
import dataaccess.GameMemoryDAO;
import chess.ChessGame.TeamColor;
import model.*;
import org.junit.jupiter.api.Test;

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


    @Test
    void registerUserSuccess() {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        assertNotNull(authData.authToken());
        assertEquals("James", authData.username());
    }

    @Test
    void registerUserAlreadyTaken(){
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user1);

        var user2 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData2 = userService.registerUser(user2);
        assertNull(authData2);
    }

    @Test
    void loginUserSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = userService.loginUser(userLogin);
        assertNotNull(authData.authToken());
        assertEquals("James", authData.username());
    }

    @Test
    void loginUserFailedPassword(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "000");
        AuthData authData = userService.loginUser(userLogin);
        assertNull(authData);
    }

    @Test
    void loginUserFailedUsername(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("SolidSnake", "007");
        AuthData authData = userService.loginUser(userLogin);
        assertNull(authData);
    }

    @Test
    void logoutUserSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = userService.loginUser(userLogin);

        String authToken = userService.logoutUser(authData.authToken());

        assertEquals("", authToken);

    }

    @Test
    void logoutUserFail(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        userService.loginUser(userLogin);

        String authToken = userService.logoutUser("cf6f6db8-38cb-446a-9589-8049b0154009");

        assertNotEquals("", authToken);

    }

    @Test
    void createGameSuccessful(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);
        var gameName = new GameNameRecord("TestName");
        Integer gameID = gameService.createGame(gameName.gameName(), authData.authToken());
        assertNotNull(gameID);
    }

    @Test
    void create2GameSuccessful(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        var gameName1 = new GameNameRecord("FirstName");
        gameService.createGame(gameName1.gameName(), authData.authToken());

        var gameName2 = new GameNameRecord("SecondGame");
        Integer gameID = gameService.createGame(gameName2.gameName(), authData.authToken());
        assertNotNull(gameID);
    }

    @Test
    void createUnauthorizedTest(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);
        var gameName1 = new GameNameRecord("FirstName");
        Integer gameID = gameService.createGame(gameName1.gameName(), "a");
        assertNull(gameID);
    }

    @Test
    void joinBlackTestSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        Integer gameID = gameService.createGame("GameTestName", authData.authToken());

        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID);
        String successfulJoin = gameService.joinGame(joinGameInfo, authData.authToken());
        assertEquals("success", successfulJoin);
    }

    @Test
    void joinWhiteTestSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        Integer gameID = gameService.createGame("GameTestName", authData.authToken());

        var joinGameInfo = new JoinGameRecord(TeamColor.WHITE, gameID);
        String successfulJoin = gameService.joinGame(joinGameInfo, authData.authToken());
        assertEquals("success", successfulJoin);
    }

    @Test
    void joinWhiteAndBlackSuccess(){
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = userService.registerUser(user1);

        Integer gameID = gameService.createGame("GameTestName", authData1.authToken());

        var joinGameInfoBlack = new JoinGameRecord(TeamColor.BLACK, gameID);
        String successfulJoin1 = gameService.joinGame(joinGameInfoBlack, authData1.authToken());

        var user2 = new UserData("Bond", "007", "JAMES@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.WHITE, gameID);
        String successfulJoin2 = gameService.joinGame(joinGameInfo2, authData2.authToken());

        assertEquals("success", successfulJoin1);
        assertEquals("success", successfulJoin2);
    }

    @Test
    void joinBlackWithBlackTaken(){
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = userService.registerUser(user1);

        Integer gameID = gameService.createGame("GameTestName", authData1.authToken());

        var joinGameInfoBlack = new JoinGameRecord(TeamColor.BLACK, gameID);
        String successfulJoin1 = gameService.joinGame(joinGameInfoBlack, authData1.authToken());

        var user2 = new UserData("Bond", "007", "JAMES@gmail.com");
        AuthData authData2 = userService.registerUser(user2);


        var joinGameInfo2 = new JoinGameRecord(TeamColor.BLACK, gameID);
        String failJoin = gameService.joinGame(joinGameInfo2, authData2.authToken());

        assertEquals("success", successfulJoin1);
        assertEquals("team color taken", failJoin);
    }

    @Test
    void getListSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = userService.registerUser(user);

        gameService.createGame("GameTestName", authData.authToken());

        Collection<GameData> gameList = gameService.listGames(authData.authToken());

        assertEquals(1, gameList.size());

    }

    @Test
    void getListUnauthorizedAuth(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userService.registerUser(user);

        Collection<GameData> gameList = gameService.listGames("unauthorized");
        Collection<GameData> expectedList = List.of(new GameData(0, null, null, null, null));

        assertEquals(expectedList, gameList);
    }

    @Test
    void clearTestSuccess(){
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


    }

    @Test
    void clearAllWhenEmpty() {

        clearService.clearDatabase();

        Collection<GameData> testList = gameData.listGames();
        assertEquals(0, testList.size());

        var gameName = new GameNameRecord("FirstName");
        Integer gameID = gameService.createGame(gameName.gameName(), "emptyAuthToken");
        assertNull(gameID);

        var userLogin = new LoginRecord("James", "007");
        AuthData loginAuthData = userService.loginUser(userLogin);
        assertNull(loginAuthData);

    }
}
