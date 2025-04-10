import chess.*;
import client.Repl;
import exception.ResponseException;

import javax.websocket.DeploymentException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ResponseException, DeploymentException, IOException {

        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}/// need this to push