package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.JoinGameRecord;
import chess.ChessGame;
import model.JoinGameResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGamedata implements GameDAO{

    final private HashMap<Integer, String> endedGames = new HashMap<>();

    public SqlGamedata() throws DataAccessException {
        configureDatabase();
    }

    public String getEndedGamesStatus(Integer gameID){
        return endedGames.get(gameID);
    }

    public void addEndedGamesStatus(Integer gameID, String status){
        if(!endedGames.containsKey(gameID)) {
            endedGames.put(gameID, status);
        }
    }

    public void updateEndedGamesStatus(Integer gameID, String status){
        if(endedGames.containsKey(gameID)) {
            endedGames.put(gameID, status);
        }
    }

    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO GameTable (whiteUsername, blackUsername, gameName, ChessGame) VALUES (?, ?, ?, ?)";
        ChessGame chessGame = new ChessGame();
        return executeUpdate(statement, null, null, gameName, chessGame);
    }




    public JoinGameResponse joinGame(JoinGameRecord joinGameInfo, AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM GameTable WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, joinGameInfo.gameID());
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    String storedWhite = rs.getString("whiteUsername");
                    String storedBlack = rs.getString("blackUsername");
                    if (addPlayerToTeam(joinGameInfo, authData, storedWhite, storedBlack)) {
                        GameData gameData = retrieveGame(joinGameInfo.gameID());
                        return new JoinGameResponse("success", gameData.game());
                    }
                    else{
                        return new JoinGameResponse("team color taken", null);
                    }
                }
                return new JoinGameResponse("gameID not found", null);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to join game");
        }
    }

    public GameData retrieveGame(Integer gameID){
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM GameTable WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    GameData data = readGameTable(rs);
                    return data;
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addPlayerToTeam(JoinGameRecord playerInfo, AuthData authData, String storedWhite, String storedBlack) throws DataAccessException {
        if (playerInfo.playerColor() == ChessGame.TeamColor.BLACK) {
            if (storedBlack == null) {
                var addBlackPlayerStatement = "UPDATE GameTable SET blackUsername = ? WHERE gameID = ?";
                executeUpdate(addBlackPlayerStatement, authData.username(), playerInfo.gameID());
                return true;

            }
            return false;
        } else {
            if (storedWhite == null) {
                var addWhitePlayerStatement = "UPDATE GameTable SET whiteUsername = ? WHERE gameID = ?";
                executeUpdate(addWhitePlayerStatement, authData.username(), playerInfo.gameID());
                return true;

            }
            return false;
        }
    }

    public boolean removePlayer(JoinGameRecord playerInfo) throws DataAccessException {
        if (playerInfo.playerColor() == ChessGame.TeamColor.BLACK) {
                var removeBlackPlayerStatement = "UPDATE GameTable SET blackUsername = null WHERE gameID = ?";
                executeUpdate(removeBlackPlayerStatement, playerInfo.gameID());
                return true;

        }
        else if (playerInfo.playerColor() == ChessGame.TeamColor.WHITE) {
                var addWhitePlayerStatement = "UPDATE GameTable SET whiteUsername = null WHERE gameID = ?";
                executeUpdate(addWhitePlayerStatement, playerInfo.gameID());
                return true;

        }
        else{
            return false;
        }
    }

    public boolean updateGame(Integer gameID, ChessGame chessGame) throws DataAccessException {
        var updateGameState = "UPDATE GameTable SET ChessGame = ? WHERE gameID = ?";
        executeUpdate(updateGameState, chessGame, gameID);
        return true;
    }

    public boolean verifyGameID(Integer gameID){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM GameTable WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, String.valueOf(gameID));
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    return true;
                }
                return false;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, ChessGame FROM GameTable";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGameTable(rs));
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("failed to retrieve list");
        }
        return result;
    }

    private GameData readGameTable(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUser = rs.getString("whiteUsername");
        var blackUser = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var chessGame = rs.getString("ChessGame");
        ChessGame game = new Gson().fromJson(chessGame, ChessGame.class);
        return new GameData(gameID, whiteUser, blackUser, gameName, game);
    }

    public void clearGames() throws DataAccessException {
        endedGames.clear();
        var statement = "TRUNCATE GameTable";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if (param instanceof ChessGame p) {ps.setString(i + 1, new Gson().toJson(p));}
                    else if (param instanceof Integer p) {ps.setInt(i + 1, p);}
                    else {ps.setString(i+1, null);}
                }
                ps.executeUpdate();

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
            throw new DataAccessException("failed to create table in GameTable");
        }
    }
}
