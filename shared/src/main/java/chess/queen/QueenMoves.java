package chess.queen;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class QueenMoves {
    public static Collection<ChessMove> queen_moves_calculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece) {
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();
        int diagRowUp = currentRow;
        int diagRowDown = currentRow;

        // moves to right
        for (int column = currentColumn + 1; column < 9; column++) {
            if (queenMoves(board, piecePosition, selectedPiece, validMoves, column, currentRow)) {
                break;
            }
        }

        for (int column = currentColumn - 1; column > 0; column--) {
            if (queenMoves(board, piecePosition, selectedPiece, validMoves, column, currentRow)) {
                break;
            }
        }

        // Vertical movement
        for (int row = currentRow + 1; row < 9; row++) {
            if (queenMoves(board, piecePosition, selectedPiece, validMoves, currentColumn, row)) {
                break;
            }
        }

        for (int row = currentRow - 1; row > 0; row--) {
            if (queenMoves(board, piecePosition, selectedPiece, validMoves, currentColumn, row)) {
                break;
            }
        }

        for (int column = currentColumn + 1; column < 9; column++) {
            diagRowUp++;
            if (diagRowUp < 9) {
                if (queenMoves(board, piecePosition, selectedPiece, validMoves, column, diagRowUp)) {
                    break;
                }
            }
        }

        for (int column = currentColumn + 1; column < 9; column++) {
            diagRowDown--;
            if (diagRowDown > 0) {
                if (queenMoves(board, piecePosition, selectedPiece, validMoves, column, diagRowDown)) {
                    break;
                }
            }
        }
        diagRowUp = currentRow;
        diagRowDown = currentRow;

        for (int column = currentColumn - 1; column > 0; column--) {
            diagRowUp++;
            if (diagRowUp < 9) {
                if (queenMoves(board, piecePosition, selectedPiece, validMoves, column, diagRowUp)) {
                    break;
                }
            }
        }

        for (int column = currentColumn - 1; column > 0; column--){
            diagRowDown--;
            if (diagRowDown > 0) {
                if (queenMoves(board, piecePosition, selectedPiece, validMoves, column, diagRowDown)) {
                    break;
                }
            }
        }

        return validMoves;
    }

    private static boolean queenMoves(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> validMoves, int column, int row) {
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
