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
    public static Collection<ChessMove> kingMovesCalculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece){
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();

        int row = currentRow + 1;
        int column = currentColumn + 1;

        // Forward
        if(row < 9){
            ChessPosition newPosition = new ChessPosition(row,currentColumn);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        // Right side
        if(column < 9){
            ChessPosition newPosition = new ChessPosition(currentRow, column);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        // right top corner
        if(row < 9 && column < 9){
            ChessPosition newPosition = new ChessPosition(row,column);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        row = currentRow - 1;
        // bottom right corner
        if(row > 0 && column < 9){
            ChessPosition newPosition = new ChessPosition(row,column);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        //down movement
        if(row > 0){
            ChessPosition newPosition = new ChessPosition(row,currentColumn);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        column = currentColumn - 1;
        // Bottom Left Corner
        if(row > 0 && column > 0){
            ChessPosition newPosition = new ChessPosition(row,column);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        // left move
        if(column > 0){
            ChessPosition newPosition = new ChessPosition(currentRow,column);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }

        row = currentRow + 1;
        // top left move
        if(row < 9 && column > 0){
            ChessPosition newPosition = new ChessPosition(row,column);
            KnightMoves.horse_move(board, piecePosition, selectedPiece, validMoves, newPosition);
        }


        return validMoves;
    }
}
