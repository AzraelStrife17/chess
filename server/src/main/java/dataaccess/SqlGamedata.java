package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.JoinGameRecord;
import chess.ChessGame;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGamedata implements GameDAO{

    public SqlGamedata() throws DataAccessException {
        configureDatabase();
    }

    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO GameTable (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
        ChessGame chessGame = new ChessGame();
        var json = new Gson().toJson(chessGame);
        var gameID = executeUpdate(statement, null, null, gameName, json);

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

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof ChessGame p) {
                        String json = new Gson().toJson(p);
                        ps.setString(i + 1, json);
                    }
                }
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){
                    throw new DataAccessException("failed to update users");
                }
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;

            }
        }
        catch (SQLException e) {
            throw new DataAccessException("failed to update game");
        }
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
