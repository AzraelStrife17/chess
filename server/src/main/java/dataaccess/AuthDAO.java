package dataaccess;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData getAuth(String auth) throws DataAccessException;
    String deleteAuthToken(String authToken) throws DataAccessException, SQLException;
    void clearAuths() throws DataAccessException, SQLException;
}
