package dataaccess;

import model.AuthData;

import java.util.HashMap;

import java.util.UUID;

public class AuthMemoryDAO implements AuthDAO{
    final private HashMap<String, AuthData> authData = new HashMap<>();

    public AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authData.put(newAuth.authToken(), newAuth);
        return newAuth;
    }

    public boolean getAuth(String authToken){
        AuthData userAuth = authData.get(authToken);
        return userAuth != null;
    }

    public String deleteAuthToken(String authToken){
        authData.remove(authToken);
        boolean verifyDeletion = getAuth(authToken);
        if (!verifyDeletion){
            return "";
        }
        else{
            return authToken;
        }

    }

    public void clearAuths(){
        authData.clear();
    }
}