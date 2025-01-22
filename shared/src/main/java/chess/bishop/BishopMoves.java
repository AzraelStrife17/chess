package chess.bishop;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class BishopMoves {
    public static Collection<ChessMove> bishop_moves_calculator(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece) {
        List<ChessMove> valid_moves = new ArrayList<>();
        int current_row = piece_position.getRow();
        int current_column = piece_position.getColumn();
        int row = current_row;

        for (int column = current_column + 1; column < 9; column++) {
            row ++;
            if (row < 9) {
                ChessPosition new_position = new ChessPosition(row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if(another_piece != null) {
                    if(selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    }
                    else{
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
            else{break;}
        }

        row = current_row;

        for (int column = current_column - 1; column > 0; column--) {
            row ++;
            if (row < 9) {
                ChessPosition new_position = new ChessPosition(row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if(another_piece != null) {
                    if(selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    }
                    else{
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
            else{break;}
        }

        row = current_row;

        for (int column = current_column + 1; column < 9; column++) {
            row --;
            if (row > 0) {
                ChessPosition new_position = new ChessPosition(row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if(another_piece != null) {
                    if(selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    }
                    else{
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
            else{break;}
        }

        row = current_row;

        for (int column = current_column - 1; column > 0; column--) {
            row --;
            if (row > 0) {
                ChessPosition new_position = new ChessPosition(row, column);
                ChessPiece another_piece = Board.getPiece(new_position);
                if(another_piece != null) {
                    if(selected_piece.getTeamColor() == another_piece.getTeamColor()) {
                        break;
                    }
                    else{
                        valid_moves.add(new ChessMove(piece_position, new_position, null));
                        break;
                    }
                }
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
            else{break;}
        }

        return valid_moves;
    }
}
