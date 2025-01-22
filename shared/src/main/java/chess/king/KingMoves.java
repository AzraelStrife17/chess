package chess.king;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        // Right side
        if(column < 9){
            ChessPosition new_position = new ChessPosition(current_row, column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        // right top corner
        if(row < 9 && column < 9){
            ChessPosition new_position = new ChessPosition(row,column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        row = current_row - 1;
        // bottom right corner
        if(row > 0 && column < 9){
            ChessPosition new_position = new ChessPosition(row,column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        //down movement
        if(row > 0){
            ChessPosition new_position = new ChessPosition(row,current_column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        column = current_column - 1;
        // Bottom Left Corner
        if(row > 0 && column > 0){
            ChessPosition new_position = new ChessPosition(row,column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        // left move
        if(column > 0){
            ChessPosition new_position = new ChessPosition(current_row,column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }

        row = current_row + 1;
        // top left move
        if(row < 9 && column > 0){
            ChessPosition new_position = new ChessPosition(row,column);
            ChessPiece another_piece = Board.getPiece(new_position);

            if(another_piece != null){
                if (selected_piece.getTeamColor() != another_piece.getTeamColor()){
                    valid_moves.add(new ChessMove(piece_position, new_position, null));
                }
            }
            else{
                valid_moves.add(new ChessMove(piece_position, new_position, null));
            }
        }


        return valid_moves;
    }
}
