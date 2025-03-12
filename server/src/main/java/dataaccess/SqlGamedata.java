package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.JoinGameRecord;
import chess.ChessGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGamedata implements GameDAO{

    public SqlGamedata() throws DataAccessException {
        configureDatabase();
    }

    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO GameTable (whiteUsername, blackUsername, gameName, ChessGame) VALUES (?, ?, ?, ?)";
        ChessGame chessGame = new ChessGame();
        var gameID = executeUpdate(statement, null, null, gameName, chessGame);
        return gameID;
    }


    public String joinGame(JoinGameRecord joinGameInfo, AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM GameTable WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, joinGameInfo.gameID());
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    String storedWhite = rs.getString("whiteUsername");
                    String storedBlack = rs.getString("blackUsername");
                    if (joinGameInfo.playerColor() == ChessGame.TeamColor.BLACK) {
                        if (storedBlack == null) {
                            var addBlackPlayerStatement = "UPDATE GameTable SET blackUsername = ? WHERE gameID = ?";
                            executeUpdate(addBlackPlayerStatement, authData.username());
                            return "success";

                        }
                        return "team color taken";
                    } else {
                        if (storedWhite == null) {
                            var addWhitePlayerStatement = "UPDATE GameTable SET whiteUsername = ? WHERE gameID = ?";
                            executeUpdate(addWhitePlayerStatement, authData.username());
                            return "success";

                        }
                        return "team color taken";
                    }
                }
                return "gameID not found";
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to join game");
        }
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
                    else ps.setString(i+1, null);
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
