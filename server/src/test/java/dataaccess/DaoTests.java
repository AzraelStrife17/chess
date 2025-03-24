package dataaccess;
import chess.ChessGame.TeamColor;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DaoTests {
    UserDAO userData = new MySqlUserdata();
    AuthDAO authData = new SqlAuthdata();
    GameDAO gameData = new SqlGamedata();

    public DaoTests() throws DataAccessException {}

    @Test
    void createUserTest() throws DataAccessException {
        var user = new UserData("JAMES", "007", "BOND@gmail.com");
        UserData addedUser = userData.createUser(user);

        assertEquals(user, addedUser);
    }

    @Test
    void createUserTakenName() throws DataAccessException{
        var user = new UserData("JAMES", "007", "BOND@gmail.com");
        UserData addedUser = userData.createUser(user);

        assertNull(addedUser);
    }

    @Test
    void getUserTest() throws DataAccessException {
        var loginRecord = new LoginRecord("JAMES", "007");
        boolean loginAttempt = userData.getUser(loginRecord);

        assertTrue(loginAttempt);
    }

    @Test
    void getUserWrongPassword() throws DataAccessException {
        var loginRecord = new LoginRecord("JAMES", "7");
        boolean loginAttempt = userData.getUser(loginRecord);

        assertFalse(loginAttempt);
    }

    @Test
    void clearUsersTest() throws SQLException, DataAccessException {
        userData.clearUsers();

        var loginRecord = new LoginRecord("JAMES", "007");
        boolean loginAttempt = userData.getUser(loginRecord);

        assertFalse(loginAttempt);
    }

    @Test
    void clearUsersRemainingUsers() throws SQLException, DataAccessException {
        userData.clearUsers();
        var user = new UserData("JAMES", "007", "BOND@gmail.com");
        UserData addedUser = userData.createUser(user);

        assertEquals(user, addedUser);

        var loginRecord = new LoginRecord("JAMES", "007");
        boolean loginAttempt = userData.getUser(loginRecord);

        assertTrue(loginAttempt);

        userData.clearUsers();
    }

    @Test
    void createAuthTest(){
        AuthData createdAuth = authData.createAuth("OptimusPrime");
        assertNotNull(createdAuth.authToken());
        assertEquals("OptimusPrime", createdAuth.username());

    }

    @Test
    void createAuthWrongUsername(){
        AuthData createdAuth = authData.createAuth("Megatron");
        assertNotNull(createdAuth.authToken());
        assertNotEquals("OptimusPrime", createdAuth.username());
    }

    @Test
    void getAuthTest() throws DataAccessException {
        AuthData createdAuth = authData.createAuth("Bumblebee");
        AuthData foundAuth = authData.getAuth(createdAuth.authToken());

        assertEquals(createdAuth, foundAuth);
    }

    @Test
    void getAuthWrongAuthToken() throws DataAccessException {
        AuthData createdAuth = authData.createAuth("Jetfire");
        AuthData foundAuth = authData.getAuth("not-existingToken");

        assertNotEquals(createdAuth, foundAuth);
    }

    @Test
    void deleteAuthTokenTest() throws SQLException, DataAccessException {
        AuthData createdAuth = authData.createAuth("Arcee");
        String authDeletionResult = authData.deleteAuthToken(createdAuth.authToken());

        assertEquals("", authDeletionResult);
    }

    @Test
    void deleteAuthFail() throws SQLException, DataAccessException {
        AuthData createdAuth = authData.createAuth("Soundwave");
        String authDeletionResult = authData.deleteAuthToken("unauthorizedAuth");

        assertNotEquals(createdAuth.authToken(), authDeletionResult);
    }

    @Test
    void clearAuthsSuccess() throws DataAccessException, SQLException {
        AuthData createdAuth = authData.createAuth("ShockWave");

        authData.clearAuths();

        AuthData foundAuth = authData.getAuth(createdAuth.authToken());

        assertNull(foundAuth);
    }

    @Test
    void createGameTest() throws DataAccessException {
        Integer gameID = gameData.createGame("TestGame");
        assertNotNull(gameID);
    }

    @Test
    void createGameFailTest() {
        assertThrows(DataAccessException.class, () -> gameData.createGame(null));
    }

    @Test
    void joinGameTest() throws DataAccessException {
        Integer gameID = gameData.createGame("TestGame");
        AuthData createdAuth = authData.createAuth("OptimusPrime");
        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID, null);

        String joinResult = gameData.joinGame(joinGameInfo, createdAuth);

        assertEquals("success", joinResult);
    }

    @Test
    void joinGameBlackTakenTest() throws DataAccessException {
        Integer gameID = gameData.createGame("TestGame");
        AuthData createdAuth = authData.createAuth("OptimusPrime");
        var joinGameInfo = new JoinGameRecord(TeamColor.BLACK, gameID, null);

        String joinResult = gameData.joinGame(joinGameInfo, createdAuth);

        assertEquals("success", joinResult);

        AuthData createdAuth2 = authData.createAuth("Megatron");
        String joinResult2 = gameData.joinGame(joinGameInfo, createdAuth2);

        assertEquals("team color taken", joinResult2);
    }

    @Test
    void gameListTest() throws DataAccessException {
        gameData.createGame("TestGame");

        Collection<GameData> gameList = gameData.listGames();

        assertNotEquals(0, gameList.size());
    }

    @Test
    void gameListEmptyTest() throws DataAccessException {
        gameData.clearGames();
        Collection<GameData> gameList = gameData.listGames();

        assertEquals(0, gameList.size());
    }

    @Test
    void clearGamesTest() throws DataAccessException {
        gameData.createGame("TestGame");

        gameData.clearGames();

        Collection<GameData> gameList = gameData.listGames();

        assertEquals(0, gameList.size());
    }

    @Test
    void clearGamesFilled() throws DataAccessException {
        gameData.createGame("TestGame");

        gameData.clearGames();

        Collection<GameData> gameList = gameData.listGames();

        assertEquals(0, gameList.size());

        gameData.createGame("TestGame");

        Collection<GameData> gameList2 = gameData.listGames();

        assertEquals(1, gameList2.size());
    }
}
