package chess;

import chess.bishop.BishopMoves;
import chess.king.KingMoves;
import chess.knights.KnightMoves;
import chess.pawn.PawnMoves;
import chess.queen.QueenMoves;
import chess.rook.RookMoves;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.piece_color = pieceColor;
        this.pieceType = type;
    }

    private final ChessGame.TeamColor piece_color;
    private final PieceType pieceType;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return piece_color == that.piece_color && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece_color, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "piece_color=" + piece_color +
                ", piece_type=" + pieceType +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }


    /**
     * @return Which team this chess piece belongs to
     */

    public ChessGame.TeamColor getTeamColor() {
        return piece_color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece selected_piece = board.getPiece(myPosition);
        switch (pieceType) {
            case KING:
                return KingMoves.kingMovesCalculator(board, myPosition, selected_piece);
            case QUEEN:
                return QueenMoves.queen_moves_calculator(board, myPosition, selected_piece);
            case BISHOP:
                return BishopMoves.bishopMovesCalculator(board, myPosition, selected_piece);
            case KNIGHT:
                return KnightMoves.knightMovesCalculator(board, myPosition, selected_piece);
            case ROOK:
                return RookMoves.rook_moves_calculator(board, myPosition, selected_piece);
            case PAWN:
                return PawnMoves.pawn_moves_calculator(board, myPosition, selected_piece);
        }

        return new ArrayList<>();
    }
}
