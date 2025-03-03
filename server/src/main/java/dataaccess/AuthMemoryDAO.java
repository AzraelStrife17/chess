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

    public AuthData getAuth(String authToken){
        AuthData userAuth = authData.get(authToken);
        return userAuth;
    }

    public String deleteAuthToken(String authToken){
        authData.remove(authToken);
        AuthData verifyDeletion = getAuth(authToken);
        if (verifyDeletion == null){
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