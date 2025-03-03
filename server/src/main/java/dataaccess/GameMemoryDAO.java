package dataaccess;
import chess.ChessGame;
import model.GameData;
import model.GameNameRecord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameMemoryDAO implements GameDAO {
    final private HashMap<Integer, GameData> gameData = new HashMap<>();
    final private Set<Integer> createdIDs = new HashSet<>();

    public Integer createGame(String gameName){
        Integer newGameID = 1000;
        while(CheckGameID(newGameID)){
            Random randomID = new Random();
            newGameID = 1000 + randomID.nextInt(9000);
        }
        createdIDs.add(newGameID);
        ChessGame game = new ChessGame();
        GameData gameInfo = new GameData(newGameID, "whiteUsername", "blackUsername",
                gameName, game);
        gameData.put(newGameID, gameInfo);
        return newGameID;

    }

    boolean CheckGameID(Integer gameID){
        if (createdIDs.contains(gameID)){
            return true;
        }
        return false;
    }
}
