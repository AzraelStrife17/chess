package dataaccess;
import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData getAuth(String auth) throws DataAccessException;
    String deleteAuthToken(String authToken) throws DataAccessException;
    void clearAuths();
}
