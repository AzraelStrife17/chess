package dataaccess;
import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData getAuth(String auth);
    String deleteAuthToken(String authToken);
    void clearAuths();
}
