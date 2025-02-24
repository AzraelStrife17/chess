package chess.bishop;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class BishopMoves {
    public static Collection<ChessMove> bishopMovesCalculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece) {
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();
        int row = currentRow;

        for (int column = currentColumn + 1; column < 9; column++) {
            row++;
            if(row < 9) {
                if (bishopMoves(board, piecePosition, selectedPiece, validMoves, column, row)) {
                    break;
                }
            }
        }
        row = currentRow;

        for (int column = currentColumn - 1; column > 0; column--) {
            row++;
            if(row < 9) {
                if (bishopMoves(board, piecePosition, selectedPiece, validMoves, column, row)) {
                    break;
                }
            }
        }
        row = currentRow;

        for (int column = currentColumn + 1; column < 9; column++) {
            row --;
            if (row > 0) {
                if (bishopMoves(board, piecePosition, selectedPiece, validMoves, column, row)) {
                    break;
                }
            }
        }
        row = currentRow;

        for (int column = currentColumn - 1; column > 0; column--) {
            row --;
            if (row > 0) {
                if (bishopMoves(board, piecePosition, selectedPiece, validMoves, column, row)) {
                    break;
                }
            }
        }
        return validMoves;
    }


    private static boolean bishopMoves(ChessBoard Board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> validMoves, int column, int row) {
            ChessPosition newPosition = new ChessPosition(row, column);
            ChessPiece anotherPiece = Board.getPiece(newPosition);
            if (anotherPiece != null) {
                if (anotherPiece.getTeamColor() != selectedPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(piecePosition, newPosition, null));
                }
                return true;
            } else {
                validMoves.add(new ChessMove(piecePosition, newPosition, null));
            }
            return false;
    }
}


