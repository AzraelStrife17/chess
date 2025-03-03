package chess.pawn;
import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class PawnMoves {
    public static Collection<ChessMove> pawnMovesCalculator(ChessBoard board, ChessPosition piecePosition,
                                                            ChessPiece selectedPiece){
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();
        // White Pawn
        if (selectedPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            // Initial Pawn Movement
            if (currentRow == 2){
                for (int initialRow = currentRow + 1; initialRow < 5 ; initialRow++){
                    if (initialMovement(board, piecePosition, initialRow, currentColumn, validMoves)) {
                        break;
                    }
                }
                int row = currentRow + 1;
                //attack up left
                whiteAttack(board, piecePosition, selectedPiece, currentColumn, validMoves, row);
            }
            // Basic Pawn movement
            else {
                int row = currentRow + 1;
                ChessPosition newPosition = new ChessPosition(row, currentColumn);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                if (anotherPiece == null) {
                    whitePromotion(piecePosition, validMoves, row, newPosition);
                }
                whiteAttack(board, piecePosition, selectedPiece, currentColumn, validMoves, row);
            }

        }
        // Black Pawn
        else{
            // Initial Pawn Movement
            if (currentRow == 7){
                for (int initialRow = currentRow - 1; initialRow > 4 ; initialRow--){
                    if (initialMovement(board, piecePosition, initialRow, currentColumn, validMoves)) {
                        break;
                    }
                }

                int row = currentRow - 1;
                blackAttack(board, piecePosition, selectedPiece, currentColumn, validMoves, row);
            }

            else {
                int row = currentRow - 1;
                ChessPosition newPosition = new ChessPosition(row, currentColumn);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                if (anotherPiece == null) {
                    blackPromotion(piecePosition, validMoves, row, newPosition);
                }
                blackAttack(board, piecePosition, selectedPiece, currentColumn, validMoves, row);
            }
        }
        return validMoves;
    }

    private static boolean initialMovement(ChessBoard board, ChessPosition piecePosition, int initialRow,
                                           int currentColumn, List<ChessMove> validMoves) {
        ChessPosition newPosition = new ChessPosition(initialRow, currentColumn);
        ChessPiece anotherPiece = board.getPiece(newPosition);
        if (anotherPiece == null){
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
        else{
            return true;
        }
        return false;
    }

    private static void whiteAttack(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                    int currentColumn, List<ChessMove> validMoves, int row) {
        //attack up left
        int columnL = currentColumn - 1;
        if (columnL > 0) {
            enemyPositionWhite(board, piecePosition, selectedPiece, validMoves, row, columnL);
        }
        // attack up right
        int columnR = currentColumn + 1;
        if (columnR < 9) {
            enemyPositionWhite(board, piecePosition, selectedPiece, validMoves, row, columnR);
        }
    }

    private static void blackAttack(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                    int currentColumn, List<ChessMove> validMoves, int row) {
        int columnL = currentColumn - 1;
        if (columnL > 0) {
            enemyPositionBlack(board, piecePosition, selectedPiece, validMoves, row, columnL);
        }

        // attack down right
        int columnR = currentColumn + 1;
        if (columnR < 9) {
            enemyPositionBlack(board, piecePosition, selectedPiece, validMoves, row, columnR);
        }
    }

    private static void blackPromotion(ChessPosition piecePosition, List<ChessMove> validMoves, int row,
                                       ChessPosition newPosition) {
        if (row == 1){
            promotionMoves(piecePosition, validMoves, newPosition);
        }
        else {
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
    }

    private static void promotionMoves(ChessPosition piecePosition, List<ChessMove> validMoves,
                                       ChessPosition newPosition) {
        validMoves.add(new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.KNIGHT));
    }

    private static void whitePromotion(ChessPosition piecePosition, List<ChessMove> validMoves, int row,
                                       ChessPosition newPosition) {
        if (row == 8){
            promotionMoves(piecePosition, validMoves, newPosition);
        }
        else {
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
    }
    private static void enemyPositionWhite(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                           List<ChessMove> validMoves, int row, int column) {
        ChessPosition enemyPositionL = new ChessPosition(row, column);
        ChessPiece enemy = board.getPiece(enemyPositionL);
        if (enemy != null) {
            if (selectedPiece.getTeamColor() != enemy.getTeamColor()) {
                whitePromotion(piecePosition, validMoves, row, enemyPositionL);
            }
        }
    }
    private static void enemyPositionBlack(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                           List<ChessMove> validMoves, int row, int column) {
        ChessPosition enemyPosition = new ChessPosition(row, column);
        ChessPiece enemy = board.getPiece(enemyPosition);
        if (enemy != null) {
            if (selectedPiece.getTeamColor() != enemy.getTeamColor()) {
                blackPromotion(piecePosition, validMoves, row, enemyPosition);
            }
        }
    }
}
