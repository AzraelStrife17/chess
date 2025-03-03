package chess.queen;
import chess.rook.RookMoves;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import static chess.rook.RookMoves.validMove;

public class QueenMoves {
    public static Collection<ChessMove> queen_moves_calculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece) {
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();
        int diagRowUp = currentRow;
        int diagRowDown = currentRow;

        // moves to right
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

        for (int column = currentColumn + 1; column < 9; column++) {
            diagRowUp++;
            if (diagRowUp < 9) {
                if (validMove(board, piecePosition, selectedPiece, validMoves, column, diagRowUp)) {
                    break;
                }
            }
        }

        for (int column = currentColumn + 1; column < 9; column++) {
            diagRowDown--;
            if (diagRowDown > 0) {
                if (validMove(board, piecePosition, selectedPiece, validMoves, column, diagRowDown)) {
                    break;
                }
            }
        }
        diagRowUp = currentRow;
        diagRowDown = currentRow;

        for (int column = currentColumn - 1; column > 0; column--) {
            diagRowUp++;
            if (diagRowUp < 9) {
                if (validMove(board, piecePosition, selectedPiece, validMoves, column, diagRowUp)) {
                    break;
                }
            }
        }

        for (int column = currentColumn - 1; column > 0; column--){
            diagRowDown--;
            if (diagRowDown > 0) {
                if (validMove(board, piecePosition, selectedPiece, validMoves, column, diagRowDown)) {
                    break;
                }
            }
        }

        return validMoves;
    }

}
