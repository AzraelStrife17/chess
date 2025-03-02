package dataaccess;
import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username);
    boolean getAuth(String auth);
    String deleteAuthToken(String authToken);
    void clearAuths();
}
