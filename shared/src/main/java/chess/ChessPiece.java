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
public class ChessPiece implements Cloneable {


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;



    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;};

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "piece_color=" + pieceColor +
                ", piece_type=" + pieceType +
                '}';
    }

    @Override
    public ChessPiece clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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
        return pieceColor;
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
        ChessPiece selectedPiece = board.getPiece(myPosition);
        switch (pieceType) {
            case KING:
                return KingMoves.kingMovesCalculator(board, myPosition, selectedPiece);
            case QUEEN:
                return QueenMoves.queenMovesCalculator(board, myPosition, selectedPiece);
            case BISHOP:
                return BishopMoves.bishopMovesCalculator(board, myPosition, selectedPiece);
            case KNIGHT:
                return KnightMoves.knightMovesCalculator(board, myPosition, selectedPiece);
            case ROOK:
                return RookMoves.rookMovesCalculator(board, myPosition, selectedPiece);
            case PAWN:
                return PawnMoves.pawnMovesCalculator(board, myPosition, selectedPiece);
        }

        return new ArrayList<>();
    }
}
