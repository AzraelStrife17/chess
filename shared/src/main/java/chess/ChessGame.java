package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        getBoard();
        getTeamTurn();
    }

    /**
     * @return Which team's turn it is
     */

    private TeamColor currentTeam;

    public TeamColor getTeamTurn() {
        if(currentTeam == null){
            currentTeam = TeamColor.WHITE;
        }
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece selectedPiece = currentBoard.getPiece(startPosition);
        Collection<ChessMove> moves = selectedPiece.pieceMoves(currentBoard, startPosition);

        for (ChessMove move: moves){
            ChessBoard simulationBoard = currentBoard.clone();
            ChessPosition endPosition = move.getEndPosition();
            int startRow = startPosition.getRow();
            int startCol = startPosition.getColumn();
            int endRow = endPosition.getRow();
            int endCol = endPosition.getColumn();
            currentBoard.addPiece(new ChessPosition (startRow, startCol), null);
            currentBoard.addPiece(new ChessPosition (endRow, endCol), selectedPiece);

            if(!(isInCheck(selectedPiece.getTeamColor()))){

                validMoves.add(move);
            }

            currentBoard = simulationBoard;
        }
        return validMoves;


    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor team = getTeamTurn();
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece selectedPiece = currentBoard.getPiece(startPosition);
        if(selectedPiece == null){throw new InvalidMoveException("No selected Piece");}
        if(selectedPiece.getTeamColor() != team){throw new InvalidMoveException("Wrong Team");}
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotion = move.getPromotionPiece();
        ChessBoard simulationBoard = currentBoard.clone();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        int endRow = endPosition.getRow();
        int endCol = endPosition.getColumn();
        Collection<ChessMove> validMoves = validMoves(startPosition);
        for(ChessMove validMove: validMoves){
            if(move.equals(validMove)){
                currentBoard.addPiece(new ChessPosition (startRow, startCol), null);
                if(promotion != null){
                    ChessPiece promotionPiece = new ChessPiece(team, promotion);
                    currentBoard.addPiece(new ChessPosition(endRow,endCol), promotionPiece);
                }
                else {
                    currentBoard.addPiece(new ChessPosition(endRow, endCol), selectedPiece);
                }
            }
        }
        if(simulationBoard.equals(currentBoard)){
            throw new InvalidMoveException("InvalidMove");
        }
        if(team == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for(int row = 1; row < 9; row++){
            for(int col = 1; col < 9; col++){
                ChessPosition checkPosition = new ChessPosition(row, col);
                ChessPiece checkPiece = currentBoard.getPiece(checkPosition);
                if(checkPiece != null){
                    if(checkPiece.getTeamColor() == teamColor && checkPiece.getPieceType() == ChessPiece.PieceType.KING){
                        kingPosition = checkPosition;
                        break;
                    }
                }
            }
        }
        for(int row = 1; row < 9; row++){
            for(int col = 1; col < 9; col++){
                ChessPosition enemyPosition = new ChessPosition(row, col);
                ChessPiece enemyPiece = currentBoard.getPiece(enemyPosition);
                if(enemyPiece != null){
                    if(enemyPiece.getTeamColor() != teamColor){
                        Collection<ChessMove> attackKing = enemyPiece.pieceMoves(currentBoard, enemyPosition);
                        for(ChessMove move: attackKing) {
                            ChessPosition endPosition = move.getEndPosition();
                            if(endPosition.equals(kingPosition)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            Collection<ChessMove> savingKingMoves = new ArrayList<>();
            for(int row = 1; row < 9; row++){
                for(int col = 1; col < 9; col++){
                    ChessPosition allyPosition = new ChessPosition(row, col);
                    ChessPiece allyPiece = currentBoard.getPiece(allyPosition);
                    if(allyPiece != null){
                        if(allyPiece.getTeamColor() == teamColor){
                            Collection<ChessMove> validMoves = validMoves(allyPosition);
                            for(ChessMove move: validMoves){
                                ChessPosition startPosition = move.getStartPosition();
                                ChessPosition endPosition = move.getEndPosition();
                                ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
                                savingKingMoves.add(new ChessMove(startPosition, endPosition, promotionPiece));
                            }
                        }
                    }
                }
            }
            return savingKingMoves.isEmpty();
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!(isInCheck(teamColor))){
            Collection<ChessMove> savingKingMoves = new ArrayList<>();
            for(int row = 1; row < 9; row++){
                for(int col = 1; col < 9; col++){
                    ChessPosition allyPosition = new ChessPosition(row, col);
                    ChessPiece allyPiece = currentBoard.getPiece(allyPosition);
                    if(allyPiece != null){
                        if(allyPiece.getTeamColor() == teamColor){
                            Collection<ChessMove> validMoves = validMoves(allyPosition);
                            for(ChessMove move: validMoves){
                                ChessPosition startPosition = move.getStartPosition();
                                ChessPosition endPosition = move.getEndPosition();
                                ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
                                savingKingMoves.add(new ChessMove(startPosition, endPosition, promotionPiece));
                            }
                        }
                    }
                }
            }
            return savingKingMoves.isEmpty();
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    public ChessGame(TeamColor currentTeam, ChessBoard currentBoard) {
        this.currentTeam = currentTeam;
        this.currentBoard = currentBoard;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */

    public ChessBoard getBoard() {
        if(currentBoard == null){
            currentBoard = new ChessBoard();
            currentBoard.resetBoard();
        }
        return currentBoard;
    }


    private ChessBoard currentBoard;

}
