package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BoardSize = 8;
    private static final int SquareSize = 10;
    private static final int PaddedChars = 1;

    private static final String White_King = " ♔ ";
    private static final String White_Queen = " ♕ ";
    private static final String White_Rook = " ♖ ";
    private static final String White_Bishop = " ♗ ";
    private static final String White_Knight = " ♘ ";
    private static final String White_Pawn = " ♙ ";

    private static final String Black_King = " ♚ ";
    private static final String Black_Queen = " ♛ ";
    private static final String Black_Rook = " ♜ ";
    private static final String Black_Bishop = " ♝ ";
    private static final String Black_Knight = " ♞ ";
    private static final String Black_Pawn = " ♟ ";



    public static void main(String[] args) {
        // Create an instance of the DrawBoard class and call the method to draw the chessboard
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawChessBoard("black");

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
            for (int boardCol = 0; boardCol < BoardSize; ++boardCol) {
                drawHeader(topHeaders[boardCol]);
            }
        }
        System.out.println();
    }

    private static void drawHeader(String headerText) {
        printHeaderText(headerText);

    }

    private static void printHeaderText(String player) {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_GREEN);

        System.out.print(player);

        setBlack();
    }

    static String drawChessBoard(String team) {
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
        for (int boardRow = 0; boardRow < BoardSize; ++boardRow) {
            drawHeader(sideHeaders[boardRow]);
            for (int boardCol = 0; boardCol < BoardSize; ++boardCol) {
                setBoard(boardRow, boardCol);

            }
            drawHeader(sideHeaders[boardRow]);

            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print(SET_TEXT_COLOR_WHITE);


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
            System.out.print("\033[37m" + White_Rook + "\033[37m");
        }
        else if (boardRow == 7 && (boardCol == 0 || boardCol == 7)) {
            System.out.print("\033[30m" + Black_Rook + "\033[30m");
        }
        else if (boardRow == 0 && (boardCol == 1 || boardCol == 6)) {
            System.out.print("\033[37m" + White_Knight + "\033[37m");
        }
        else if (boardRow == 7 && (boardCol == 1 || boardCol == 6)) {
            System.out.print("\033[30m" + Black_Knight + "\033[30m");
        }

        else if (boardRow == 0 && (boardCol == 2 || boardCol == 5)) {
            System.out.print("\033[37m" + White_Bishop + "\033[37m");
        }
        else if (boardRow == 7 && (boardCol == 2 || boardCol == 5)) {
            System.out.print("\033[30m" + Black_Bishop + "\033[30m");
        }

        else if (boardRow == 0 && boardCol == 3) {
            System.out.print("\033[37m" + White_Queen + "\033[37m");
        }
        else if (boardRow == 7 && boardCol == 3) {
            System.out.print("\033[30m" + Black_Queen + "\033[30m");
        }

        else if (boardRow == 0) {
            System.out.print("\033[37m" + White_King + "\033[37m");
        }
        else if (boardRow == 7) {
            System.out.print("\033[30m" + Black_King + "\033[30m");
        }

        else if (boardRow == 1){
            System.out.print("\033[37m" + White_Pawn + "\033[37m");
        }

        else if (boardRow == 6){
            System.out.print("\033[30m" + Black_Pawn + "\033[30m");
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