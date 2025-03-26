package dataaccess;
import model.AuthData;
import model.AuthToken;

import java.sql.SQLException;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData getAuth(AuthToken authToken) throws DataAccessException;
    String deleteAuthToken(AuthToken authToken) throws DataAccessException, SQLException;
    void clearAuths() throws DataAccessException, SQLException;
}
