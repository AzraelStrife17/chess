package dataaccess;

import model.LoginRecord;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class UserMemoryDAO implements UserDAO {
    final private HashMap<String, UserData> userData = new HashMap<>();

    public UserData createUser(UserData user){
        if (userData.containsKey(user.username())){
            return null;

        }
        else{
            userData.put(user.username(), user);
            return user;
        }
    }

    public boolean getUser(LoginRecord loginInfo){
       UserData user = userData.get(loginInfo.username());
       if (user == null){
           return false;
       }
       if (Objects.equals(user.password(), loginInfo.password())){
           return true;
       }
       else{
           return false;
       }
    }

    public void clearUsers(){
        userData.clear();
    }
}
