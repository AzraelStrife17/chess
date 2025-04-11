package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType;
import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARDSIZE = 8;

    private static final String WHITEKING = " ♔ ";
    private static final String WHITEQUEEN = " ♕ ";
    private static final String WHITEROOK = " ♖ ";
    private static final String WHITEBISHOP = " ♗ ";
    private static final String WHITEKNIGHT = " ♘ ";
    private static final String WHITEPAWN = " ♙ ";

    private static final String BLACKKING = " ♚ ";
    private static final String BLACKQUEEN = " ♛ ";
    private static final String BLACKROOK = " ♜ ";
    private static final String BLACKBISHOP = " ♝ ";
    private static final String BLACKKNIGHT = " ♞ ";
    private static final String BLACKPAWN = " ♟ ";



    public static void main(String[] args) {
        // Create an instance of the DrawBoard class and call the method to draw the chessboard
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ChessGame testGame = new ChessGame();

        out.print(ERASE_SCREEN);

        drawChessBoard("black", testGame);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }




    private static void drawHeaders(String team) {

        setBlack();

        String[] topHeaders = { " Ａ ", " Ｂ ", " Ｃ ", " Ｄ ", " Ｅ ", " Ｆ ", " Ｇ ", " Ｈ " };
        if(team.equals("black")) {
            System.out.print(" ");
            for (int boardCol = 7; boardCol > -1; --boardCol) {
                drawHeader(topHeaders[boardCol]);
            }
        }
        else{
            System.out.print(" ");
            for (int boardCol = 0; boardCol < BOARDSIZE; ++boardCol) {
                drawHeader(topHeaders[boardCol]);
            }
        }
        System.out.println();
    }

    private static void drawHeader(String headerText) {
        printHeaderText(headerText);

    }

    private static void printHeaderText(String player) {
        System.out.print(SET_TEXT_COLOR_GREEN);

        System.out.print(RESET_BG_COLOR);
        System.out.print(player);

    }

    static String drawChessBoard(String team, ChessGame game) {
        ChessBoard extractedBoard = game.getBoard();
        StringBuilder board = new StringBuilder();
        if(team.equals("black")) {
            drawHeaders(team);
            drawRowOfSquaresBlack(extractedBoard);
            drawHeaders(team);
        }
        else{
            drawHeaders(team);
            drawRowOfSquaresWhite(extractedBoard);
            drawHeaders(team);
        }


        return board.toString();
    }

    private static void drawRowOfSquaresBlack(ChessBoard board) {
        String[] sideHeaders = {"1", "2", "3", "4", "5", "6", "7", "8"};
        for (int boardRow = 0; boardRow < BOARDSIZE; boardRow++) {
            drawHeader(sideHeaders[boardRow]);
            setBoardBlack(boardRow, board);
            drawHeader(sideHeaders[boardRow]);


            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print(SET_TEXT_COLOR_WHITE);

            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }




    }

    private static void setBoardWhite(int boardRow, ChessBoard board) {

            for(int col = 1; col < 9; col++){
                if ((boardRow + col) % 2 == 0) {
                    setWhite();
                } else {
                    setRed();
                }

                ChessPosition piecePosition = new ChessPosition(boardRow + 1, col);
                ChessPiece checkForPiece = board.getPiece(piecePosition);
                if(checkForPiece != null){
                    if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.ROOK))){
                        System.out.print("\033[37m" + WHITEROOK + "\033[37m");
                    }

                    else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.ROOK))){
                        System.out.print("\033[30m" + BLACKROOK + "\033[30m");

                    }

                    else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.KNIGHT))){
                        System.out.print("\033[37m" + WHITEKNIGHT + "\033[37m");
                    }

                    else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.KNIGHT))){
                        System.out.print("\033[30m" + BLACKKNIGHT + "\033[30m");
                    }

                    else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.BISHOP))){
                        System.out.print("\033[37m" + WHITEBISHOP + "\033[37m");
                    }

                    else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.BISHOP))){
                        System.out.print("\033[30m" + BLACKBISHOP + "\033[30m");
                    }

                    else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.QUEEN))){
                        System.out.print("\033[37m" + WHITEQUEEN + "\033[37m");
                    }

                    else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.QUEEN))){
                        System.out.print("\033[30m" + BLACKQUEEN + "\033[30m");
                    }

                    else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.KING))){
                        System.out.print("\033[37m" + WHITEKING + "\033[37m");
                    }

                    else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.KING))){
                        System.out.print("\033[30m" + BLACKKING + "\033[30m");
                    }

                    else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.PAWN))){
                        System.out.print("\033[37m" + WHITEPAWN + "\033[37m");
                    }

                    else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.PAWN))){
                        System.out.print("\033[30m" + BLACKPAWN + "\033[30m");
                    }

                    else {
                        System.out.print(" \u2003 ");
                    }
                }

                else {
                    System.out.print(" \u2003 ");
                }
            }


    }

    private static void setBoardBlack(int boardRow, ChessBoard board) {

        for(int col = 8; col > 0; col--){
            if ((boardRow + col) % 2 == 0) {
                setWhite();
            } else {
                setRed();
            }

            ChessPosition piecePosition = new ChessPosition(boardRow + 1, col);
            ChessPiece checkForPiece = board.getPiece(piecePosition);
            if(checkForPiece != null){
                if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.ROOK))){
                    System.out.print("\033[37m" + WHITEROOK + "\033[37m");
                }

                else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.ROOK))){
                    System.out.print("\033[30m" + BLACKROOK + "\033[30m");

                }

                else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.KNIGHT))){
                    System.out.print("\033[37m" + WHITEKNIGHT + "\033[37m");
                }

                else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.KNIGHT))){
                    System.out.print("\033[30m" + BLACKKNIGHT + "\033[30m");
                }

                else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.BISHOP))){
                    System.out.print("\033[37m" + WHITEBISHOP + "\033[37m");
                }

                else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.BISHOP))){
                    System.out.print("\033[30m" + BLACKBISHOP + "\033[30m");
                }

                else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.QUEEN))){
                    System.out.print("\033[37m" + WHITEQUEEN + "\033[37m");
                }

                else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.QUEEN))){
                    System.out.print("\033[30m" + BLACKQUEEN + "\033[30m");
                }

                else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.KING))){
                    System.out.print("\033[37m" + WHITEKING + "\033[37m");
                }

                else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.KING))){
                    System.out.print("\033[30m" + BLACKKING + "\033[30m");
                }

                else if (checkForPiece.equals(new ChessPiece(WHITE, PieceType.PAWN))){
                    System.out.print("\033[37m" + WHITEPAWN + "\033[37m");
                }

                else if(checkForPiece.equals(new ChessPiece(BLACK, PieceType.PAWN))){
                    System.out.print("\033[30m" + BLACKPAWN + "\033[30m");
                }

                else {
                    System.out.print(" \u2003 ");
                }
            }

            else {
                System.out.print(" \u2003 ");
            }
        }


    }

    private static void drawRowOfSquaresWhite(ChessBoard board) {
        String[] sideHeaders = {"1", "2", "3", "4", "5", "6", "7", "8"};
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            drawHeader(sideHeaders[boardRow]);
            setBoardWhite(boardRow, board);

            drawHeader(sideHeaders[boardRow]);

            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print(SET_TEXT_COLOR_WHITE);

            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }
    }

    private static void setWhite() {
        System.out.print(SET_BG_COLOR_WHITE);
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack() {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setRed() {
        System.out.print(SET_BG_COLOR_RED);
        System.out.print(SET_TEXT_COLOR_RED);
    }

}