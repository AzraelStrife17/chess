package chess.pawn;
import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class PawnMoves {
    public static Collection<ChessMove> pawn_moves_calculator(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece){
        List<ChessMove> valid_moves = new ArrayList<>();
        int current_row = piece_position.getRow();
        int current_column = piece_position.getColumn();
        // White Pawn
        if (selected_piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            // Initial Pawn Movement
            if (current_row == 2){
                for (int initial_row = current_row + 1; initial_row < 5 ; initial_row++){
                    ChessPosition new_position = new ChessPosition(initial_row, current_column);
                    ChessPiece another_piece = Board.getPiece(new_position);
                    if (another_piece == null){
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                    }
                    else{break;}
                }
                int row = current_row + 1;
                //attack up left
                int column_l = current_column - 1;
                if (column_l > 0) {
                    ChessPosition enemy_position_l = new ChessPosition(row, column_l);
                    ChessPiece enemy_l = Board.getPiece(enemy_position_l);
                    if (enemy_l != null) {
                        if (selected_piece.getTeamColor() != enemy_l.getTeamColor()) {
                                valid_moves.add(new ChessMove(piece_position, enemy_position_l, null));
                        }
                    }
                }
                // attack up right
                int column_r = current_column + 1;
                if (column_r < 9) {
                    ChessPosition enemy_position_r = new ChessPosition(row, column_r);
                    ChessPiece enemy_r = Board.getPiece(enemy_position_r);
                    if (enemy_r != null) {
                        if (selected_piece.getTeamColor() != enemy_r.getTeamColor()) {
                                valid_moves.add(new ChessMove(piece_position, enemy_position_r, null));
                        }
                    }
                }
            }
            // Basic Pawn movement
            else {
                int row = current_row + 1;
                ChessPosition new_position = new ChessPosition(row, current_column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if (another_piece == null) {
                    white_promotion(piece_position, valid_moves, row, new_position);
                }
                //attack up left
                int column_l = current_column - 1;
                if (column_l > 0) {
                    enemy_position_white(Board, piece_position, selected_piece, valid_moves, row, column_l);
                }
                // attack up right
                int column_r = current_column + 1;
                if (column_r < 9) {
                    enemy_position_white(Board, piece_position, selected_piece, valid_moves, row, column_r);
                }
            }

        }
        // Black Pawn
        else{
            // Initial Pawn Movement
            if (current_row == 7){
                for (int initial_row = current_row - 1; initial_row > 4 ; initial_row--){
                    ChessPosition new_position = new ChessPosition(initial_row, current_column);
                    ChessPiece another_piece = Board.getPiece(new_position);
                    if (another_piece == null){
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                    }
                    else{break;}
                }

                int row = current_row - 1;
                //attack down left
                int column_l = current_column - 1;
                if (column_l > 0) {
                    enemy_position_black(Board, piece_position, selected_piece, valid_moves, row, column_l);
                }

                // attack down right
                int column_r = current_column + 1;
                if (column_r < 9) {
                    enemy_position_black(Board, piece_position, selected_piece, valid_moves, row, column_l);
                }
            }

            // Basic Pawn Movement
            else {
                int row = current_row - 1;
                ChessPosition new_position = new ChessPosition(row, current_column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if (another_piece == null) {
                    black_promotion(piece_position, valid_moves, row, new_position);
                }

                //attack down left
                int column_l = current_column - 1;
                if (column_l > 0) {
                    enemy_position_black(Board, piece_position, selected_piece, valid_moves, row, column_l);
                }

                // attack down right
                int column_r = current_column + 1;
                if (column_r < 9) {
                    enemy_position_black(Board, piece_position, selected_piece, valid_moves, row, column_r);
                }
            }
        }
        return valid_moves;
    }

    private static void black_promotion(ChessPosition piece_position, List<ChessMove> valid_moves, int row, ChessPosition new_position) {
        if (row == 1){
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.QUEEN));
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.BISHOP));
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.ROOK));
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.KNIGHT));
        }
        else {
            valid_moves.add(new ChessMove(piece_position, new_position, null));
        }
    }

    private static void white_promotion(ChessPosition piece_position, List<ChessMove> valid_moves, int row, ChessPosition new_position) {
        if (row == 8){
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.QUEEN));
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.BISHOP));
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.ROOK));
            valid_moves.add(new ChessMove(piece_position, new_position, ChessPiece.PieceType.KNIGHT));
        }
        else {
            valid_moves.add(new ChessMove(piece_position, new_position, null));
        }
    }
    private static void enemy_position_white(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece, List<ChessMove> valid_moves, int row, int column) {
        ChessPosition enemy_position_l = new ChessPosition(row, column);
        ChessPiece enemy = Board.getPiece(enemy_position_l);
        if (enemy != null) {
            if (selected_piece.getTeamColor() != enemy.getTeamColor()) {
                white_promotion(piece_position, valid_moves, row, enemy_position_l);
            }
        }
    }
    private static void enemy_position_black(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece, List<ChessMove> valid_moves, int row, int column) {
        ChessPosition enemy_position = new ChessPosition(row, column);
        ChessPiece enemy = Board.getPiece(enemy_position);
        if (enemy != null) {
            if (selected_piece.getTeamColor() != enemy.getTeamColor()) {
                black_promotion(piece_position, valid_moves, row, enemy_position);
            }
        }
    }
}
