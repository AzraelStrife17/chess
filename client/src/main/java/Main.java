import chess.*;
import client.Repl;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(0);
        var serverUrl = "http://localhost:" + port;
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}