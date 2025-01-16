package chess;

import java.util.Arrays;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    public int Base_1_Translation(int index) {
        return index - 1;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[Base_1_Translation(position.getRow())][Base_1_Translation(position.getColumn())] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
       return squares[Base_1_Translation(position.getRow())][Base_1_Translation(position.getColumn())];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                squares[row][column] = null;
            }
        }

        ChessPiece white_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        for (int column = 1; column < 9; column++) { //place white pawn
            int row = 2;
            addPiece(new ChessPosition(row, column), white_pawn);
        }

        ChessPiece black_pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int column = 1; column < 9; column++) { //place black pawn
            int row = 7;
            addPiece(new ChessPosition(row, column), black_pawn);
        }

        ChessPiece white_rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(new ChessPosition(1, 1), white_rook);
        addPiece(new ChessPosition(1, 8), white_rook);

        ChessPiece white_knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(new ChessPosition(1, 2), white_knight);
        addPiece(new ChessPosition(1, 7), white_knight);

        ChessPiece white_bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(new ChessPosition(1, 3), white_bishop);
        addPiece(new ChessPosition(1, 6), white_bishop);

        ChessPiece white_queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(new ChessPosition(1, 4), white_queen);

        ChessPiece white_king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(new ChessPosition(1, 5), white_king);

        ChessPiece black_rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(new ChessPosition(8, 1), black_rook);
        addPiece(new ChessPosition(8, 8), black_rook);

        ChessPiece black_knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(new ChessPosition(8, 2), black_knight);
        addPiece(new ChessPosition(8, 7), black_knight);

        ChessPiece black_bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(new ChessPosition(8, 3), black_bishop);
        addPiece(new ChessPosition(8, 6), black_bishop);

        ChessPiece black_queen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(new ChessPosition(8, 4), black_queen);

        ChessPiece black_king = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(new ChessPosition(8, 5), black_king);
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
}
