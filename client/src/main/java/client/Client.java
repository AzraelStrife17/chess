package client;

import chess.*;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import websocket.LoadGameHandler;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static client.DrawBoard.*;

public class Client {
    private String userName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private AuthData currentAuth;
    private Integer currentGameID;
    private static ChessGame currentGame;
    private ChessBoard currentBoard;
    private final NotificationHandler serverMessageHandler;
    private final LoadGameHandler loadGameHandler;
    private WebSocketFacade ws;


    public Client(String serverUrl, NotificationHandler serverMessageHandler, LoadGameHandler loadGameHandler) throws ResponseException, DeploymentException, IOException {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.serverMessageHandler = serverMessageHandler;
        this.loadGameHandler = loadGameHandler;
        ws = new WebSocketFacade(serverUrl, serverMessageHandler, loadGameHandler);
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "listgames" -> listGames();
                case "playgame" -> playGame(params);
                case "observegame" -> observeGame(params);
                case "makemove" -> makeMove(params);
                case "redrawboard" -> redrawBoard(params);
                case "viewmoves" -> viewMoves(params);
                case "leavegame" -> leaveGame(params);
                case "quit" -> "quit";

                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String register(String... params) {
        if (params.length == 3){
            UserData userData = new UserData(params[0], params[1], params[2]);
            try {
                currentAuth = server.registerResult(userData);
                if (currentAuth != null) {
                    state = State.POSTLOGIN;
                    userName = params[0];
                    return String.format("Logged in as %s.", userName);
                }
            }
            catch (ResponseException ex){
                return ex.getMessage();
            }
        }

        return "Error wrong format";
    }

    public String login(String... params) throws Exception{
        if (params.length == 2){
            LoginRecord loginRecord = new LoginRecord(params[0], params[1]);
            try {
                currentAuth = server.loginResult(loginRecord);
                if (currentAuth != null) {
                    state = State.POSTLOGIN;
                    userName = params[0];
                    return String.format("Logged in as %s.", userName);
                }
            }
            catch (ResponseException ex){
                return ex.getMessage();
            }
        }
        return "Error wrong format";
    }

    public String logout() throws ResponseException {
        if (state == State.POSTLOGIN){
            try {
                AuthToken authToken = new AuthToken(currentAuth.authToken());
                server.logoutResult(authToken);
                state = State.PRELOGIN;
                return "Logged out";
            }
            catch (ResponseException ex){
                return ex.getMessage();
            }
        }
        return "Login to use";
    }

    public String create(String... params) throws ResponseException {
        if (state == State.POSTLOGIN) {
            try {
                if (params.length == 1) {
                    server.createGameResult(params[0], currentAuth.authToken());
                    return "game created";
                }
            } catch(ResponseException ex){
                return ex.getMessage();
            }
        }
        return "Login to use";
    }

    public String listGames() throws ResponseException {
        if (state == State.POSTLOGIN) {
            try {
                AuthToken authToken = new AuthToken(currentAuth.authToken());
                var games = server.listGames(authToken);
                var result = new StringBuilder();
                server.clearDisplayIdMap();
                for (var game : games) {
                    int gameID = game.gameID();
                    int displayID = server.createDisplayID(gameID);
                    result.append("Game ID: ").append(displayID).
                            append("    White: ").append(game.whiteUsername()).
                            append("    Black: ").append(game.blackUsername()).
                            append("    Game name: ").append(game.gameName()).
                            append("\n");
                }
                return result.toString();
            }
            catch(ResponseException ex) {
                return ex.getMessage();
            }
        }
        return "Login to use";
    }

    public String playGame(String... params) throws ResponseException {
        if (state == State.POSTLOGIN){
            try {
                if (params.length == 2) {
                    ChessGame.TeamColor team;
                    int id;

                    try {
                        id = Integer.parseInt(params[0]);
                        team = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return "Error: Invalid team color or layout, proper format is <gameID> <white/black>.";
                    }

                    int displayID = server.getGameID(id);
                    if(displayID == 0){
                        return "Error: ID does not exist";
                    }

                    JoinGameRecord joinInfo = new JoinGameRecord(team, id, currentAuth.authToken());
                    server.joinGameResult(joinInfo);
                    currentGameID = id;
                    ws.connectToGame(joinInfo.authToken(), joinInfo.gameID());
                    state = State.GAMESTATE;
                    return String.format("playing as %s.", params[1]);
                }
            }
            catch(ResponseException ex) {
                return ex.getMessage();
            }
            return "Error: BadArgument";
        }
        return "Login to use";
    }

    public String observeGame(String... params) throws ResponseException {
        if (state == State.POSTLOGIN) {

            int id;

            try {
                id = Integer.parseInt(params[0]);
            }catch (IllegalArgumentException e) {
                return "Error: Invalid team color or layout, proper format is <gameID> <white/black>.";
            }

            int displayID = server.getGameID(id);
            if(displayID == 0){
                return "Error: ID does not exist";
            }

            JoinGameRecord joinInfo = new JoinGameRecord(null, id, currentAuth.authToken());
            ws.connectToGame(joinInfo.authToken(), joinInfo.gameID());
            state = State.GAMESTATE;

            return String.format("watching game %s", params[0]);
        }
        return "Login to use";
    }

    public String makeMove(String... params){
        if (state == state.GAMESTATE){
            int startRow;
            int endRow;
            int startCol;
            int endCol;
            try {
                startCol = params[0].charAt(0) - 'a' + 1;
                startRow = Integer.parseInt(params[0].substring(1));

                endCol = params[1].charAt(0) - 'a' + 1;
                endRow = Integer.parseInt(params[1].substring(1));


                ChessPosition startPosition = new ChessPosition(startRow, startCol);
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                currentBoard = currentGame.getBoard();
                ChessPiece piece = currentBoard.getPiece(startPosition);
                ChessPiece.PieceType promotion = null;

                if(piece.equals(new ChessPiece(WHITE, ChessPiece.PieceType.PAWN))){
                    if (endRow == 8){
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("Select Promotion Piece \n Queen \n Bishop \n Knight \n Rook \n");
                        String promotionInput = scanner.nextLine();
                        state = state.PROMOTION;
                        promotion = selectPromotionPiece(promotionInput);

                    }
                }

                if(piece.equals(new ChessPiece(BLACK, ChessPiece.PieceType.PAWN))){
                    if (endRow == 1){
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("Select Promotion Piece \n Queen \n Bishop \n Knight \n Rook \n");
                        String promotionInput = scanner.nextLine();
                        state = state.PROMOTION;
                        promotion = selectPromotionPiece(promotionInput);
                    }
                }

                ChessMove move = new ChessMove(startPosition, endPosition, promotion);

                ws.makeMove(currentAuth.authToken(), currentGameID, move);

                return "";


            } catch (ResponseException e) {
                return "invalid argument example for correct argument 'a2' 'a3'";
            }
        }
        else {
            return "must join a game to use this method";
        }
    }

    public String redrawBoard(String ...params){
        if(state == state.GAMESTATE){
            try {
                String team = params[0].toLowerCase();
                drawChessBoard(team, currentGame, null);
                return "";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "must be in a game to use";
    }

    public String viewMoves(String ...params){
        int row;
        int col;
        if(state == state.GAMESTATE){
            try {
                String team = params[0].toLowerCase();
                if(!team.equals("white") && !team.equals("black")){
                    return "invalid team";
                }
                if(params[1].isEmpty()){
                    return "invalid argument";
                }
                col = params[1].charAt(0) - 'a' + 1;
                row = Integer.parseInt(params[1].substring(1));
                currentBoard = currentGame.getBoard();
                ChessPosition highlightPosition = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(highlightPosition);
                if(piece != null) {
                    drawChessBoard(team, currentGame, highlightPosition);
                    return "";
                }
                else{
                    return "no piece found";
                }
            } catch (NumberFormatException e) {
                return "invalid argument";
            }
        }
        return "must be in a game to use";
    }



    public ChessPiece.PieceType selectPromotionPiece(String promotionInput){
        if(state == state.PROMOTION){

            try{
                var selectedPromotion = ChessPiece.PieceType.valueOf(promotionInput.toUpperCase());
                if(selectedPromotion == ChessPiece.PieceType.QUEEN){
                    state = state.GAMESTATE;
                    return selectedPromotion;
                }
                if(selectedPromotion == ChessPiece.PieceType.BISHOP){
                    state = state.GAMESTATE;
                    return selectedPromotion;
                }
                if(selectedPromotion == ChessPiece.PieceType.KNIGHT){
                    state = state.GAMESTATE;
                    return selectedPromotion;
                }
                if(selectedPromotion == ChessPiece.PieceType.ROOK){
                    state = state.GAMESTATE;
                    return selectedPromotion;
                }

            }
            catch (IllegalArgumentException e) {
                state = state.GAMESTATE;
                System.out.print("Not a valid Promotion");
                return null;
            }
        }

        return null;
    }

    public String leaveGame(String ...params){
        if (state == state.GAMESTATE){
            try {
                String leave = params[0].toLowerCase();
                if (leave.equals("leave")) {

                }

                return "did not enter 'leave'";
            } catch (Exception e) {
                return "invalid argument";
            }

        }

        return "must be in game";
    }


    public static void loadGame(ChessGame game){
        currentGame = game;
    }

    public String help() {
        if (state == State.PRELOGIN) {
            return """
                    register <username> <password> <email>
                    login <username> <password>
                    help
                    quit
                    """;
        }

        if (state == State.POSTLOGIN) {
            return """
                    create <gamename>
                    listgames
                    playgame <gameID> <teamColor>
                    observegame <gameID>
                    help
                    logout
                    
                    """;
        }


        else{
            return """
                    makemove <startPosition> <endPosition>
                    redrawboard <white/black> to choose perspective
                    viewmoves <white/black> to choose perspective <<positionOfPiece>>
                    leavegame type 'leave' to exit game
                    """;
        }
    }

}

