package chess.pawn;
import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class PawnMoves {
    public static Collection<ChessMove> pawn_moves_calculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece){
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();
        // White Pawn
        if (selectedPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            // Initial Pawn Movement
            if (currentRow == 2){
                for (int initialRow = currentRow + 1; initialRow < 5 ; initialRow++){
                    ChessPosition new_position = new ChessPosition(initialRow, currentColumn);
                    ChessPiece anotherPiece = board.getPiece(new_position);
                    if (anotherPiece == null){
                        validMoves.add(new ChessMove(piecePosition, new_position, null));
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
                int column_r = currentColumn + 1;
                if (column_r < 9) {
                    enemyPositionWhite(board, piecePosition, selectedPiece, validMoves, row, column_r);
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
                    enemy_position_black(board, piecePosition, selectedPiece, validMoves, row, columnL);
                }

                // attack down right
                int columnR = currentColumn + 1;
                if (columnR < 9) {
                    enemy_position_black(board, piecePosition, selectedPiece, validMoves, row, columnR);
                }
            }

            // Basic Pawn Movement
            else {
                int row = currentRow - 1;
                ChessPosition new_position = new ChessPosition(row, currentColumn);
                ChessPiece another_piece = board.getPiece(new_position);
                if (another_piece == null) {
                    blackPromotion(piecePosition, validMoves, row, new_position);
                }

                //attack down left
                int columnL = currentColumn - 1;
                if (columnL > 0) {
                    enemy_position_black(board, piecePosition, selectedPiece, validMoves, row, columnL);
                }

                // attack down right
                int columnR = currentColumn + 1;
                if (columnR < 9) {
                    enemy_position_black(board, piecePosition, selectedPiece, validMoves, row, columnR);
                }
            }
        }
        return validMoves;
    }

    private static void blackPromotion(ChessPosition piece_position, List<ChessMove> validMoves, int row, ChessPosition new_position) {
        if (row == 1){
            promotionMoves(piece_position, validMoves, new_position);
        }
        else {
            validMoves.add(new ChessMove(piece_position, new_position, null));
        }
    }

    private static void promotionMoves(ChessPosition piece_position, List<ChessMove> validMoves, ChessPosition new_position) {
        validMoves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.KNIGHT));
    }

    private static void whitePromotion(ChessPosition piecePosition, List<ChessMove> validMoves, int row, ChessPosition newPosition) {
        if (row == 8){
            promotionMoves(piecePosition, validMoves, newPosition);
        }
        else {
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
    }
    private static void enemyPositionWhite(ChessBoard Board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> valid_moves, int row, int column) {
        ChessPosition enemyPositionL = new ChessPosition(row, column);
        ChessPiece enemy = Board.getPiece(enemyPositionL);
        if (enemy != null) {
            if (selectedPiece.getTeamColor() != enemy.getTeamColor()) {
                whitePromotion(piecePosition, valid_moves, row, enemyPositionL);
            }
        }
    }
    private static void enemy_position_black(ChessBoard Board, ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> valid_moves, int row, int column) {
        ChessPosition enemyPosition = new ChessPosition(row, column);
        ChessPiece enemy = Board.getPiece(enemyPosition);
        if (enemy != null) {
            if (selectedPiece.getTeamColor() != enemy.getTeamColor()) {
                blackPromotion(piecePosition, valid_moves, row, enemyPosition);
            }
        }
    }
}
