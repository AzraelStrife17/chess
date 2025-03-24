package service;
import dataaccess.DataAccessException;
import model.AuthToken;
import model.UserData;
import model.AuthData;
import model.LoginRecord;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }


    public AuthData registerUser(UserData user) throws DataAccessException {
        UserData userData = userDataAccess.createUser(user);
        if(userData == null){
            return null;
        }
        String username = userData.username();
        return authDataAccess.createAuth(username);
    }

    public AuthData loginUser(LoginRecord loginInfo) throws DataAccessException {
         boolean correctLogin = userDataAccess.getUser(loginInfo);
         if (correctLogin){
             return authDataAccess.createAuth(loginInfo.username());
         }
         else{
             return null;
         }
    }

    public String logoutUser(AuthToken authToken) throws DataAccessException, SQLException {
        String extractedToken = authToken.authToken();
        AuthData authExist = authDataAccess.getAuth(extractedToken);
        if(authExist != null){
            return authDataAccess.deleteAuthToken(extractedToken);
        }
        else{
                return extractedToken;
        }
    }
}
