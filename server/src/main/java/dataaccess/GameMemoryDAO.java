package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameRecord;

import java.util.*;

public class GameMemoryDAO implements GameDAO {
    final private HashMap<Integer, GameData> gameData = new HashMap<>();
    final private Set<Integer> createdIDs = new HashSet<>();

    public Integer createGame(String gameName){
        Integer newGameID = 1000;
        while(checkGameID(newGameID)){
            Random randomID = new Random();
            newGameID = 1000 + randomID.nextInt(9000);
        }
        createdIDs.add(newGameID);
        ChessGame game = new ChessGame();
        GameData gameInfo = new GameData(newGameID, null, null,
                gameName, game);
        gameData.put(newGameID, gameInfo);
        return newGameID;

    }

    public String joinGame(JoinGameRecord joinGameInfo, AuthData authData){
        if (checkGameID(joinGameInfo.gameID())){
            GameData game = gameData.get(joinGameInfo.gameID());
            String blackUsername = game.blackUsername();
            String whiteUsername = game.whiteUsername();
            ChessGame chessGame = game.game();
            String gameName = game.gameName();
            if(joinGameInfo.playerColor() == ChessGame.TeamColor.BLACK){
                if(Objects.equals(blackUsername, null)){
                    gameData.remove(joinGameInfo.gameID());
                    GameData updatedGame = new GameData(joinGameInfo.gameID(), whiteUsername, authData.username(),
                            gameName, chessGame);
                    gameData.put(joinGameInfo.gameID(), updatedGame);
                    return "success";

                }
                return "team color taken";

            }
            else{
                if(Objects.equals(whiteUsername, null)){
                    gameData.remove(joinGameInfo.gameID());
                    GameData updatedGame = new GameData(joinGameInfo.gameID(), authData.username(), blackUsername,
                            gameName, chessGame);
                    gameData.put(joinGameInfo.gameID(), updatedGame);
                    return "success";
                }
                return "team color taken";
            }
        }
        return "gameID not found";

    }

    public Collection<GameData> listGames(){
        return gameData.values();
    }

    boolean checkGameID(Integer gameID){
        return createdIDs.contains(gameID);
    }

    public void clearGames(){
        gameData.clear();
    }
}
