package chess.bishop;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class BishopMoves {
    public static Collection<ChessMove> bishopMovesCalculator(ChessBoard board, ChessPosition piecePosition, ChessPiece selectedPiece) {
        List<ChessMove> validMoves = new ArrayList<>();
        int currentRow = piecePosition.getRow();
        int currentColumn = piecePosition.getColumn();
        int row = currentRow;

        for (int column = currentColumn + 1; column < 9; column++) {
            row ++;
            if(row < 9) {
                ChessPosition newPosition = new ChessPosition(row, column);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                checkForOtherPieces(piecePosition, selectedPiece, validMoves, newPosition, anotherPiece);
                if (anotherPiece != null) {
                    break;
                }
            }
        }

        row = currentRow;

        for (int column = currentColumn - 1; column > 0; column--) {
            row ++;
            if(row < 9) {
                ChessPosition newPosition = new ChessPosition(row, column);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                checkForOtherPieces(piecePosition, selectedPiece, validMoves, newPosition, anotherPiece);
                if (anotherPiece != null) {
                    break;
                }
            }
        }

        row = currentRow;

        for (int column = currentColumn + 1; column < 9; column++) {
            row --;
            if (row > 0) {
                ChessPosition newPosition = new ChessPosition(row, column);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                checkForOtherPieces(piecePosition, selectedPiece, validMoves, newPosition, anotherPiece);
                if (anotherPiece != null) {
                    break;
                }
            }
        }

        row = currentRow;

        for (int column = currentColumn - 1; column > 0; column--) {
            row --;
            if (row > 0) {
                ChessPosition newPosition = new ChessPosition(row, column);
                ChessPiece anotherPiece = board.getPiece(newPosition);
                checkForOtherPieces(piecePosition, selectedPiece, validMoves, newPosition, anotherPiece);
                if (anotherPiece != null) {
                    break;
                }
            }
        }
        return validMoves;
    }

    private static void checkForOtherPieces(ChessPosition piecePosition, ChessPiece selectedPiece, List<ChessMove> validMoves, ChessPosition newPosition, ChessPiece anotherPiece) {
        if(anotherPiece != null) {
            if(selectedPiece.getTeamColor() != anotherPiece.getTeamColor()) {
                validMoves.add(new ChessMove(piecePosition, newPosition, null));
            }
        }
        else {
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
        }
    }}


