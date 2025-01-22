package chess.knights;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class KnightMoves {
    public static Collection<ChessMove> knight_moves_calculator(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece){
        List<ChessMove> valid_moves = new ArrayList<>();
        int current_row = piece_position.getRow();
        int current_column = piece_position.getColumn();

        // reverse 7 move
        int row = current_row + 2;
        int column = current_column + 1;
        ChessPosition new_position_1 = new ChessPosition(row, column);
        if(row <= 8 && column < 8){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_1);
        }


        // 7 move
        row = current_row + 2;
        column = current_column - 1;
        ChessPosition new_position_2 = new ChessPosition(row, column);
        if(row <=8  && column >= 1){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_2);
        }


        // right side 7
        row = current_row + 1;
        column = current_column + 2;
        ChessPosition new_position_3 = new ChessPosition(row, column);
        if(row <= 8 && column <=8){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_3);
        }


        // left side 7
        row = current_row + 1;
        column = current_column - 2;
        ChessPosition new_position_4 = new ChessPosition(row, column);
        if(row <= 8 && column >=1){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_4);
        }


        // L move
        row = current_row - 2;
        column = current_column + 1;
        ChessPosition new_position_5 = new ChessPosition(row, column);
        if(row >= 1 && column <=8){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_5);
        }

        // reverse L move
        row = current_row - 2;
        column = current_column - 1;
        ChessPosition new_position_6 = new ChessPosition(row, column);
        if(row >= 1 && column >=1){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_6);
        }


        // right side L
        row = current_row - 1;
        column = current_column + 2;
        ChessPosition new_position_7 = new ChessPosition(row, column);
        if(row >= 1 && column <=8){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_7);
        }


        // left side L
        row = current_row - 1;
        column = current_column - 2;
        ChessPosition new_position_8 = new ChessPosition(row, column);
        if(row >= 1 && column >= 1){
            horse_move(Board, piece_position, selected_piece, valid_moves, new_position_8);
        }



        return valid_moves;
    }

    public static void horse_move(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece, List<ChessMove> valid_moves, ChessPosition new_position_1) {
        ChessPiece another_piece = Board.getPiece(new_position_1);
        if(another_piece != null){
            if(selected_piece.getTeamColor() != another_piece.getTeamColor()){
                valid_moves.add(new ChessMove(piece_position, new_position_1, null));
            }
        }
        else{
            valid_moves.add(new ChessMove(piece_position, new_position_1, null));
        }
    }
}
