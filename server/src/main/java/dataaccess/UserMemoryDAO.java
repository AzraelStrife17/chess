package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemoryDAO {
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
}
