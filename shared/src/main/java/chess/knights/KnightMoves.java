package chess.knights;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class KnightMoves {
    public static Collection<ChessMove> knightMovesCalculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece){
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();

        // reverse 7 move
        int row = currentRow + 2;
        int column = currentColumn + 1;
        ChessPosition newPosition1 = new ChessPosition(row, column);
        if(row <= 8 && column < 8){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition1);
        }


        // 7 move
        row = currentRow + 2;
        column = currentColumn - 1;
        ChessPosition newPosition2 = new ChessPosition(row, column);
        if(row <=8  && column >= 1){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition2);
        }


        // right side 7
        row = currentRow + 1;
        column = currentColumn + 2;
        ChessPosition newPosition3 = new ChessPosition(row, column);
        if(row <= 8 && column <=8){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition3);
        }


        // left side 7
        row = currentRow + 1;
        column = currentColumn - 2;
        ChessPosition newPosition4 = new ChessPosition(row, column);
        if(row <= 8 && column >=1){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition4);
        }


        // L move
        row = currentRow - 2;
        column = currentColumn + 1;
        ChessPosition newPosition5 = new ChessPosition(row, column);
        if(row >= 1 && column <=8){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition5);
        }

        // reverse L move
        row = currentRow - 2;
        column = currentColumn - 1;
        ChessPosition newPosition6 = new ChessPosition(row, column);
        if(row >= 1 && column >=1){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition6);
        }


        // right side L
        row = currentRow - 1;
        column = currentColumn + 2;
        ChessPosition newPosition7 = new ChessPosition(row, column);
        if(row >= 1 && column <=8){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition7);
        }


        // left side L
        row = currentRow - 1;
        column = currentColumn - 2;
        ChessPosition newPosition8 = new ChessPosition(row, column);
        if(row >= 1 && column >= 1){
            horse_move(board, piecePosition, selectedPiece, validMoves, newPosition8);
        }



        return validMoves;
    }

    public static void horse_move(ChessBoard Board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> validMoves, ChessPosition newPosition) {
        ChessPiece anotherPiece = Board.getPiece(newPosition);
        if(anotherPiece != null){
            if(selectedPiece.getTeamColor() != anotherPiece.getTeamColor()){
                validMoves.add(new ChessMove(piecePosition, newPosition, null));
            }
        }
        else{
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
    }
}
