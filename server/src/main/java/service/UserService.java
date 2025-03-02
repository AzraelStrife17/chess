package service;
import model.UserData;
import model.AuthData;
import model.LoginRecord;
import java.util.Collection;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }


    public AuthData registerUser(UserData user){
        UserData userData = userDataAccess.createUser(user);
        if(userData == null){
            return null;
        }
        String username = userData.username();
        return authDataAccess.createAuth(username);
    }

    public AuthData loginUser(LoginRecord loginInfo){
         boolean correctLogin = userDataAccess.getUser(loginInfo);
         if (correctLogin){
             return authDataAccess.createAuth(loginInfo.username());
         }
         else{
             return null;
         }
    }

    public String logoutUser(String authToken){
        boolean authExist = authDataAccess.getAuth(authToken);
        if(authExist){
            return authDataAccess.deleteAuthToken(authToken);
        }
        else{
            return authToken;
        }
    }
}
