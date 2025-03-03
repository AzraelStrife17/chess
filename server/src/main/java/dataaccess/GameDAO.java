package dataaccess;
import model.GameData;
import model.GameNameRecord;

public interface GameDAO {
    Integer createGame(String gameName);

}
