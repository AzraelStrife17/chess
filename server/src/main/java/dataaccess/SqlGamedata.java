package dataaccess;

import model.AuthData;
import model.GameData;
import model.JoinGameRecord;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SqlGamedata implements GameDAO{

    public SqlGamedata() throws DataAccessException {
        configureDatabase();
    }

    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO GameTable (gameID, whiteUsername, blackUsername, gameName, ChessGame) VALUES (?, ?, ?, ?, ?)";

    }

    @Override
    public String joinGame(JoinGameRecord joinGameInfo, AuthData authData) {
        return "";
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void clearGames() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  GameTable (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar (256),
              `blackUsername` varchar(256),
              `gameName` varchar (256) Not Null,
              `ChessGame` TEXT,
              Primary KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create table in userdata");
        }
    }
}
