package chess.rook;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class RookMoves {
    public static Collection<ChessMove> rookMovesCalculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece){
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();

        horizontalVerticalMoves(board, piecePosition, selectedPiece, currentColumn, validMoves, currentRow);
        return validMoves;
    }

    public static void horizontalVerticalMoves(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                               int currentColumn, List<ChessMove> validMoves, int currentRow) {
        // Horizontal Movement
        for (int column = currentColumn + 1; column < 9; column++) {
            if (validMove(board, piecePosition, selectedPiece, validMoves, column, currentRow)) {
                break;
            }
        }

        for (int column = currentColumn - 1; column > 0; column--) {
            if (validMove(board, piecePosition, selectedPiece, validMoves, column, currentRow)) {
                break;
            }
        }

        // Vertical movement
        for (int row = currentRow + 1; row < 9; row++) {
            if (validMove(board, piecePosition, selectedPiece, validMoves, currentColumn, row)) {
                break;
            }
        }

        for (int row = currentRow - 1; row > 0; row--) {
            if (validMove(board, piecePosition, selectedPiece, validMoves, currentColumn, row)) {
                break;
            }
        }
    }

    public static boolean validMove(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> validMoves, int column, int row) {
        return e(board, piecePosition, selectedPiece, validMoves, column, row);
    }

    public static boolean e(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> validMoves, int column, int row) {
        ChessPosition newPosition = new ChessPosition(row, column);
        ChessPiece anotherPiece = board.getPiece(newPosition);
        if (anotherPiece != null) {
            if (anotherPiece.getTeamColor() != selectedPiece.getTeamColor()) {
                validMoves.add(new ChessMove(piecePosition, newPosition, null));
            }
            return true;
        }
        else {
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
        return false;
    }
}
