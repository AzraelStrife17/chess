package chess.queen;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class QueenMoves {
    public static Collection<ChessMove> queen_moves_calculator(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece) {
        List<ChessMove> valid_moves = new ArrayList<>();
        int current_row = piece_position.getRow();
        int current_column = piece_position.getColumn();
        int diag_movement_row = current_row;

        // Horizontal Movement
        for (int column = current_column + 1; column < 9; column++) {
            ChessPosition new_position = new ChessPosition(current_row, column);
            ChessPiece another_piece = Board.getPiece(new_position);
            if (another_piece != null) {
                if (another_piece.getTeamColor() == selected_piece.getTeamColor()) {
                    break;
                } else {
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                    break;
                }
            }

            valid_moves.add(new ChessMove(piece_position, new_position, null));
        }

        for (int column = current_column - 1; column > 0; column--) {
            ChessPosition new_position = new ChessPosition(current_row, column);
            ChessPiece another_piece = Board.getPiece(new_position);
            if (another_piece != null) {
                if (another_piece.getTeamColor() == selected_piece.getTeamColor()) {
                    break;
                } else {
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            valid_moves.add(new ChessMove(piece_position, new_position, null));
        }

        // Vertical movement
        for (int row = current_row + 1; row < 9; row++) {
            ChessPosition new_position = new ChessPosition(row, current_column);
            ChessPiece another_piece = Board.getPiece(new_position);
            if (another_piece != null) {
                if (another_piece.getTeamColor() == selected_piece.getTeamColor()) {
                    break;
                } else {
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                    break;
                }
            }


            valid_moves.add(new ChessMove(piece_position, new_position, null));
        }

        for (int row = current_row - 1; row > 0; row--) {
            ChessPosition new_position = new ChessPosition(row, current_column);
            ChessPiece another_piece = Board.getPiece(new_position);
            if (another_piece != null) {
                if (another_piece.getTeamColor() == selected_piece.getTeamColor()) {
                    break;
                } else {
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                    break;
                }
            }
            valid_moves.add(new ChessMove(piece_position, new_position, null));
        }

        for (int column = current_column + 1; column < 9; column++) {
            diag_movement_row++;
            if (diag_movement_row < 9) {
                ChessPosition new_position = new ChessPosition(diag_movement_row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if (another_piece != null) {
                    if (selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    } else {
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            } else {
                break;
            }
        }

        diag_movement_row = current_row;

        for (int column = current_column - 1; column > 0; column--) {
            diag_movement_row++;
            if (diag_movement_row < 9) {
                ChessPosition new_position = new ChessPosition(diag_movement_row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if (another_piece != null) {
                    if (selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    } else {
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            } else {
                break;
            }
        }

        diag_movement_row = current_row;

        for (int column = current_column + 1; column < 9; column++) {
            diag_movement_row--;
            if (diag_movement_row > 0) {
                ChessPosition new_position = new ChessPosition(diag_movement_row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if (another_piece != null) {
                    if (selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    } else {
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            } else {
                break;
            }
        }

        diag_movement_row = current_row;

        for (int column = current_column - 1; column > 0; column--) {
            diag_movement_row--;
            if (diag_movement_row > 0) {
                ChessPosition new_position = new ChessPosition(diag_movement_row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if (another_piece != null) {
                    if (selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    } else {
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            } else {
                break;
            }
        }

        return valid_moves;


    }
}
