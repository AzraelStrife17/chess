package chess.bishop;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.rook.RookMoves;

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


    private static boolean bishopMoves(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                       List<ChessMove> validMoves, int column, int row) {
        return RookMoves.addMove(board, piecePosition, selectedPiece, validMoves, column, row);
    }
}


