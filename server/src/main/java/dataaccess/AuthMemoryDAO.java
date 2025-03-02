package dataaccess;

import model.AuthData;

import java.util.HashMap;

import java.util.UUID;

public class AuthMemoryDAO {
    final private HashMap<String, AuthData> authData = new HashMap<>();

    public AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authData.put(newAuth.authToken(), newAuth);
        return newAuth;
    }
}