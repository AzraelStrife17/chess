import chess.*;
import server.Server;



public class Main {
    public static void main(String[] args) {
        var port = 8080;


        var server = new Server();
        server.run(port);


        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
} // here to pass push commit