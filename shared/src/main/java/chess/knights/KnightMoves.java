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
        int row = current_row;
        int column = current_column;

        // reverse 7 move
        row = current_row + 2;
        column = current_column + 1;
        ChessPosition new_position_1 = new ChessPosition(row, column);
        if(row <= 8 && column < 8){
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


        // 7 move
        row = current_row + 2;
        column = current_column - 1;
        ChessPosition new_position_2 = new ChessPosition(row, column);
        if(row <=8  && column >= 1){
            ChessPiece another_piece_2 = Board.getPiece(new_position_2);
            if(another_piece_2 != null){
                if(selected_piece.getTeamColor() != another_piece_2.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_2, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_2, null));
            }
        }


        // right side 7
        row = current_row + 1;
        column = current_column + 2;
        ChessPosition new_position_3 = new ChessPosition(row, column);
        if(row <= 8 && column <=8){
            ChessPiece another_piece_3 = Board.getPiece(new_position_3);
            if(another_piece_3 != null){
                if(selected_piece.getTeamColor() != another_piece_3.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_3, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_3, null));
            }
        }


        // left side 7
        row = current_row + 1;
        column = current_column - 2;
        ChessPosition new_position_4 = new ChessPosition(row, column);
        if(row <= 8 && column >=1){
            ChessPiece another_piece_4 = Board.getPiece(new_position_4);
            if(another_piece_4 != null){
                if(selected_piece.getTeamColor() != another_piece_4.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_4, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_4, null));
            }
        }


        // L move
        row = current_row - 2;
        column = current_column + 1;
        ChessPosition new_position_5 = new ChessPosition(row, column);
        if(row >= 1 && column <=8){
            ChessPiece another_piece_5 = Board.getPiece(new_position_5);
            if(another_piece_5 != null){
                if(selected_piece.getTeamColor() != another_piece_5.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_5, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_5, null));
            }
        }

        // reverse L move
        row = current_row - 2;
        column = current_column - 1;
        ChessPosition new_position_6 = new ChessPosition(row, column);
        if(row >= 1 && column >=1){
            ChessPiece another_piece_6 = Board.getPiece(new_position_6);
            if(another_piece_6 != null){
                if(selected_piece.getTeamColor() != another_piece_6.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_6, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_6, null));
            }
        }


        // right side L
        row = current_row - 1;
        column = current_column + 2;
        ChessPosition new_position_7 = new ChessPosition(row, column);
        if(row >= 1 && column <=8){
            ChessPiece another_piece_7 = Board.getPiece(new_position_7);
            if(another_piece_7 != null){
                if(selected_piece.getTeamColor() != another_piece_7.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_7, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_7, null));
            }
        }


        // left side L
        row = current_row - 1;
        column = current_column - 2;
        ChessPosition new_position_8 = new ChessPosition(row, column);
        if(row >= 1 && column >= 1){
            ChessPiece another_piece_8 = Board.getPiece(new_position_8);
            if(another_piece_8 != null){
                if(selected_piece.getTeamColor() != another_piece_8.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position_8, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position_8, null));
            }
        }



        return valid_moves;
    }
}
