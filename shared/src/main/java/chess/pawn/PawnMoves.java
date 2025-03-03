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
                    ChessPosition newPosition = new ChessPosition(initialRow, currentColumn);
                    ChessPiece anotherPiece = board.getPiece(newPosition);
                    if (anotherPiece == null){
                        validMoves.add(new ChessMove(piecePosition, newPosition, null));
                    }
                    else{break;}
                }
                int row = currentRow + 1;
                //attack up left
                int columnL = currentColumn - 1;
                if (columnL > 0) {
                    ChessPosition enemyPositionL = new ChessPosition(row, columnL);
                    ChessPiece enemyL = board.getPiece(enemyPositionL);
                    if (enemyL != null) {
                        if (selectedPiece.getTeamColor() != enemyL.getTeamColor()) {
                                validMoves.add(new ChessMove(piecePosition, enemyPositionL, null));
                        }
                    }
                }
                // attack up right
                int columnR = currentColumn + 1;
                if (columnR < 9) {
                    ChessPosition enemyPositionR = new ChessPosition(row, columnR);
                    ChessPiece enemyR = board.getPiece(enemyPositionR);
                    if (enemyR != null) {
                        if (selectedPiece.getTeamColor() != enemyR.getTeamColor()) {
                                validMoves.add(new ChessMove(piecePosition, enemyPositionR, null));
                        }
                    }
                }
            }
            // Basic Pawn movement
            else {
                int row = currentRow + 1;
                ChessPosition newPosition = new ChessPosition(row, currentColumn);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                if (anotherPiece == null) {
                    whitePromotion(piecePosition, validMoves, row, newPosition);
                }
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

        }
        // Black Pawn
        else{
            // Initial Pawn Movement
            if (currentRow == 7){
                for (int initialRow = currentRow - 1; initialRow > 4 ; initialRow--){
                    ChessPosition newPosition = new ChessPosition(initialRow, currentColumn);
                    ChessPiece anotherPiece = board.getPiece(newPosition);
                    if (anotherPiece == null){
                        validMoves.add(new ChessMove(piecePosition, newPosition, null));
                    }
                    else{break;}
                }

                int row = currentRow - 1;
                //attack down left
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

            // Basic Pawn Movement
            else {
                int row = currentRow - 1;
                ChessPosition newPosition = new ChessPosition(row, currentColumn);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                if (anotherPiece == null) {
                    blackPromotion(piecePosition, validMoves, row, newPosition);
                }

                //attack down left
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
        }
        return validMoves;
    }

    private static void blackAttackMoves(){

    }

    private static void blackPromotion(ChessPosition piece_position, List<ChessMove> validMoves, int row,
                                       ChessPosition newPosition) {
        if (row == 1){
            promotionMoves(piece_position, validMoves, newPosition);
        }
        else {
            validMoves.add(new ChessMove(piece_position, newPosition, null));
        }
    }

    private static void promotionMoves(ChessPosition piece_position, List<ChessMove> validMoves,
                                       ChessPosition newPosition) {
        validMoves.add(new ChessMove(piece_position, newPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(piece_position, newPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(piece_position, newPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(piece_position, newPosition, ChessPiece.PieceType.KNIGHT));
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
    private static void enemyPositionWhite(ChessBoard Board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                           List<ChessMove> valid_moves, int row, int column) {
        ChessPosition enemyPositionL = new ChessPosition(row, column);
        ChessPiece enemy = Board.getPiece(enemyPositionL);
        if (enemy != null) {
            if (selectedPiece.getTeamColor() != enemy.getTeamColor()) {
                whitePromotion(piecePosition, valid_moves, row, enemyPositionL);
            }
        }
    }
    private static void enemyPositionBlack(ChessBoard Board, ChessPosition piecePosition, ChessPiece selectedPiece,
                                           List<ChessMove> validMoves, int row, int column) {
        ChessPosition enemyPosition = new ChessPosition(row, column);
        ChessPiece enemy = Board.getPiece(enemyPosition);
        if (enemy != null) {
            if (selectedPiece.getTeamColor() != enemy.getTeamColor()) {
                blackPromotion(piecePosition, validMoves, row, enemyPosition);
            }
        }
    }
}
