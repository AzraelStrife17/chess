package client;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BoardSize = 8;
    private static final int PaddedChars = 8;

    private static final String White_King = "♔";
    private static final String White_Queen = "♕";
    private static final String White_Rook = "♖";
    private static final String White_Bishop = "♗";
    private static final String White_Knight = "♘";
    private static final String White_Pawn = "♙";

    private static final String Black_King = "♚";
    private static final String Black_Queen = "♛";
    private static final String Black_Rook = "♜";
    private static final String Black_Bishop = "♝";
    private static final String Black_Knight = "♞";
    private static final String Black_Pawn = "♟";



    public static void main(String[] args) {
        // Create an instance of the DrawBoard class and call the method to draw the chessboard
        DrawBoard.drawChessBoard();
    }

    static String drawChessBoard() {
        StringBuilder board = new StringBuilder();
        drawRowOfSquares(System.out);
        return board.toString();
    }

    private static void drawRowOfSquares(PrintStream out) {
        for (int boardRow = 0; boardRow < BoardSize; ++boardRow) {
            for (int boardCol = 0; boardCol < BoardSize; ++boardCol) {
                if (boardRow == 0 && (boardCol == 0 || boardCol == 7)) {
                    out.print(White_Rook);
                }
                else if (boardRow == 7 && (boardCol == 0 || boardCol == 7)) {
                    out.print(Black_Rook);
                }
                else if (boardRow == 0 && (boardCol == 1 || boardCol == 6)) {
                    out.print(White_Knight);
                }
                else if (boardRow == 7 && (boardCol == 1 || boardCol == 6)) {
                    out.print(Black_Knight);
                }

                else if (boardRow == 0 && (boardCol == 2 || boardCol == 5)) {
                    out.print(White_Bishop);
                }
                else if (boardRow == 7 && (boardCol == 2 || boardCol == 5)) {
                    out.print(Black_Bishop);
                }

                else if (boardRow == 0 && boardCol == 3) {
                    out.print(White_Queen);
                }
                else if (boardRow == 7 && boardCol == 3) {
                    out.print(Black_Queen);
                }

                else if (boardRow == 0) {
                    out.print(White_King);
                }
                else if (boardRow == 7) {
                    out.print(Black_King);
                }

                else if (boardRow == 1){
                    out.print(White_Pawn);
                }

                else if (boardRow == 6){
                    out.print(Black_Pawn);
                }


                else {
                    out.print("\u2003");
                }

            }


            out.println();
        }

    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

}