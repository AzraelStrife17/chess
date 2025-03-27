package server;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;
import java.util.*;


public class ServerFacade {
    private final String serverUrl;
    private Map<Integer, Integer> displayIdMap = new HashMap<>();;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData registerResult(UserData request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, AuthData.class);
    }

    public AuthData loginResult(LoginRecord request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, AuthData.class);
    }

    public StringResponse logoutResult(AuthToken request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, StringResponse.class);
    }

    public CreateGameResponse createGameResult(String gameName, String authToken) throws ResponseException {
        var request = new CreateGameData(gameName, authToken);
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResponse.class);
    }

    public StringResponse joinGameResult(JoinGameRecord request) throws ResponseException {
        var path = "/game";
        int gameID = getGameID(request.gameID());
        JoinGameRecord updatedRequest = new JoinGameRecord(request.playerColor(), gameID, request.authToken());
        return this.makeRequest("PUT", path, updatedRequest, StringResponse.class);
    }

    public Collection<GameData> listGames(AuthToken request) throws ResponseException {
        var path = "/game";
        record GameListResponse(Collection<GameData> games){};
        var response = this.makeRequest("GET", path, request, GameListResponse.class);
        return response.games();
    }

    public void clearAll() throws ResponseException {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (Objects.equals(method, "GET") && request instanceof AuthToken authToken) {
                http.setRequestProperty("Authorization", authToken.authToken());
            }
            else{
            writeBody(request, http);
                }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException("other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public int createDisplayID(int gameID){
        int displayID = gameID;
        displayIdMap.put(displayID, gameID);
        return displayID;
    }

    public int getGameID(int displayID){
        if(displayIdMap.get(displayID) != null){
            return displayIdMap.get(displayID);
        }
        else{
            return 0;
        }
    }
    public void clearDisplayIdMap(){
        displayIdMap.clear();
    }
}

