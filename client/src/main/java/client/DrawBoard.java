package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


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

        out.print(ERASE_SCREEN);

        drawChessBoard("white", 1);

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

    static String drawChessBoard(String team, int gameID) {
        StringBuilder board = new StringBuilder();
        if(team.equals("black")) {
            drawHeaders(team);
            drawRowOfSquaresBlack();
            drawHeaders(team);
        }
        else{
            drawHeaders(team);
            drawRowOfSquaresWhite();
            drawHeaders(team);
        }


        return board.toString();
    }

    private static void drawRowOfSquaresBlack() {
        String[] sideHeaders = {"1", "2", "3", "4", "5", "6", "7", "8"};
        for (int boardRow = 0; boardRow < BOARDSIZE; ++boardRow) {
            drawHeader(sideHeaders[boardRow]);
            for (int boardCol = 0; boardCol < BOARDSIZE; ++boardCol) {
                setBoard(boardRow, boardCol);

            }
            drawHeader(sideHeaders[boardRow]);

            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print(SET_TEXT_COLOR_WHITE);

            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }



    }

    private static void setBoard(int boardRow, int boardCol) {
        if ((boardRow + boardCol) % 2 == 0) {
            setWhite();
        } else {
            setRed();
        }

        if (boardRow == 0 && (boardCol == 0 || boardCol == 7)) {
            System.out.print("\033[37m" + WHITEROOK + "\033[37m");
        }
        else if (boardRow == 7 && (boardCol == 0 || boardCol == 7)) {
            System.out.print("\033[30m" + BLACKROOK + "\033[30m");
        }
        else if (boardRow == 0 && (boardCol == 1 || boardCol == 6)) {
            System.out.print("\033[37m" + WHITEKNIGHT + "\033[37m");
        }
        else if (boardRow == 7 && (boardCol == 1 || boardCol == 6)) {
            System.out.print("\033[30m" + BLACKKNIGHT + "\033[30m");
        }

        else if (boardRow == 0 && (boardCol == 2 || boardCol == 5)) {
            System.out.print("\033[37m" + WHITEBISHOP + "\033[37m");
        }
        else if (boardRow == 7 && (boardCol == 2 || boardCol == 5)) {
            System.out.print("\033[30m" + BLACKBISHOP + "\033[30m");
        }

        else if (boardRow == 0 && boardCol == 4) {
            System.out.print("\033[37m" + WHITEQUEEN + "\033[37m");
        }
        else if (boardRow == 7 && boardCol == 4) {
            System.out.print("\033[30m" + BLACKQUEEN + "\033[30m");
        }

        else if (boardRow == 0) {
            System.out.print("\033[37m" + WHITEKING + "\033[37m");
        }
        else if (boardRow == 7) {
            System.out.print("\033[30m" + BLACKKING + "\033[30m");
        }

        else if (boardRow == 1){
            System.out.print("\033[37m" + WHITEPAWN + "\033[37m");
        }

        else if (boardRow == 6){
            System.out.print("\033[30m" + BLACKPAWN + "\033[30m");
        }


        else {
            System.out.print(" \u2003 ");
        }
    }

    private static void drawRowOfSquaresWhite() {
        String[] sideHeaders = {"1", "2", "3", "4", "5", "6", "7", "8"};
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            drawHeader(sideHeaders[boardRow]);
            for (int boardCol = 7; boardCol > -1; --boardCol) {
                setBoard(boardRow, boardCol);

            }
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