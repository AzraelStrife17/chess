package chess;

import java.util.Arrays;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {

    private final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
    }

    public int base1Translation(int index) {
        return index - 1;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[base1Translation(position.getRow())][base1Translation(position.getColumn())] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
       return squares[base1Translation(position.getRow())][base1Translation(position.getColumn())];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 0; row < 7; row++) {
            for(int col = 0; col < 7;col ++){
                squares[row][col] = null;
            }
        }

        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        for (int column = 1; column < 9; column++) { //place white pawn
            int row = 2;
            addPiece(new ChessPosition(row, column), whitePawn);
        }

        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int column = 1; column < 9; column++) { //place black pawn
            int row = 7;
            addPiece(new ChessPosition(row, column), blackPawn);
        }

        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(new ChessPosition(1, 1), whiteRook);
        addPiece(new ChessPosition(1, 8), whiteRook);

        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(new ChessPosition(1, 2), whiteKnight);
        addPiece(new ChessPosition(1, 7), whiteKnight);

        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(new ChessPosition(1, 3), whiteBishop);
        addPiece(new ChessPosition(1, 6), whiteBishop);

        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(new ChessPosition(1, 4), whiteQueen);

        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(new ChessPosition(1, 5), whiteKing);

        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(new ChessPosition(8, 1), blackRook);
        addPiece(new ChessPosition(8, 8), blackRook);

        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(new ChessPosition(8, 2), blackKnight);
        addPiece(new ChessPosition(8, 7), blackKnight);

        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(new ChessPosition(8, 3), blackBishop);
        addPiece(new ChessPosition(8, 6), blackBishop);

        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(new ChessPosition(8, 4), blackQueen);

        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(new ChessPosition(8, 5), blackKing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (squares[row][column] != null) {
                    sb.append(squares[row][column].getPieceType().toString().charAt(0));
                }
                else {
                    sb.append(".");
                }
                sb.append(" ");

            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();

            ChessBoard clonedSquares = new ChessBoard();
            for(int row = 1; row < 9; row++){
                for(int col = 1; col < 9; col++){
                    ChessPosition clonePosition = new ChessPosition(row, col);
                    ChessPiece clonedPiece = getPiece(clonePosition);
                    clonedSquares.addPiece(clonePosition,clonedPiece);
                }
            }

            return clonedSquares;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
