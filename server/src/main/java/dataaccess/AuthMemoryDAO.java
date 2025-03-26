package dataaccess;

import model.AuthData;
import model.AuthToken;

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

    public AuthData getAuth(AuthToken authToken){
        AuthData userAuth = authData.get(authToken.authToken());
        return userAuth;
    }

    public String deleteAuthToken(AuthToken authToken){
        authData.remove(authToken.authToken());
        AuthData verifyDeletion = getAuth(authToken);
        if (verifyDeletion == null){
            return "";
        }
        else{
            return authToken.authToken();
        }

    }

    public void clearAuths(){
        authData.clear();
    }
}