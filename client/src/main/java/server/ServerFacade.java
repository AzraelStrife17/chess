package server;
import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.Objects;


public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData RegisterResult(UserData request){
        var path = "/user";
        return this.makeRequest("POST", path, request, AuthData.class);
    }

    public AuthData LoginResult(LoginRecord request){
        var path = "/session";
        return this.makeRequest("POST", path, request, AuthData.class);
    }

    public StringResponse LogoutResult(AuthToken request){
        var path = "/session";
        return this.makeRequest("DELETE", path, request, StringResponse.class);
    }

    public CreateGameResponse CreateGameResult(String GameName, String authToken){
        var request = new CreateGameData(GameName, authToken);
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResponse.class);
    }

    public StringResponse JoinGameResult(JoinGameRecord request){
        var path = "/game";
        return this.makeRequest("PUT", path, request, StringResponse.class);
    }

    public Collection<GameData> ListGames(AuthToken request){
        var path = "/game";
        record gameListResponse(Collection<GameData> games){};
        var response = this.makeRequest("GET", path, request, gameListResponse.class);
        return response.games();
    }

    public void clearAll(){
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass){
        try{
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

        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException{
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {

                }
            }

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
}

