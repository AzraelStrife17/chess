package server;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.LoginRecord;

import java.io.*;
import java.net.*;


public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData RegisterResult(UserData request){
        var path = "/user";
        return this.makeRequest("POST", path, request, UserData.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass){

    }
}

