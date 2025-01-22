package chess.king;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.knights.KnightMoves;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class KingMoves {
    public static Collection<ChessMove> king_moves_calculator(ChessBoard Board, ChessPosition piece_position, ChessPiece selected_piece){
        List<ChessMove> valid_moves = new ArrayList<>();
        int current_row = piece_position.getRow();
        int current_column = piece_position.getColumn();

        int row = current_row + 1;
        int column = current_column + 1;

        // Forward
        if(row < 9){
            ChessPosition new_position = new ChessPosition(row,current_column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        // Right side
        if(column < 9){
            ChessPosition new_position = new ChessPosition(current_row, column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        // right top corner
        if(row < 9 && column < 9){
            ChessPosition new_position = new ChessPosition(row,column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        row = current_row - 1;
        // bottom right corner
        if(row > 0 && column < 9){
            ChessPosition new_position = new ChessPosition(row,column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        //down movement
        if(row > 0){
            ChessPosition new_position = new ChessPosition(row,current_column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        column = current_column - 1;
        // Bottom Left Corner
        if(row > 0 && column > 0){
            ChessPosition new_position = new ChessPosition(row,column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        // left move
        if(column > 0){
            ChessPosition new_position = new ChessPosition(current_row,column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }

        row = current_row + 1;
        // top left move
        if(row < 9 && column > 0){
            ChessPosition new_position = new ChessPosition(row,column);
            KnightMoves.horse_move(Board, piece_position, selected_piece, valid_moves, new_position);
        }


        return valid_moves;
    }
}
