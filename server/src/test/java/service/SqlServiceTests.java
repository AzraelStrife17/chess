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
}
