package client;

import chess.ChessGame;
import exception.ResponseException;
import websocket.LoadGameHandler;
import websocket.NotificationHandler;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Scanner;

import static client.DrawBoard.drawChessBoard;
import static client.EscapeSequences.*;


public class Repl implements NotificationHandler, LoadGameHandler {
    private final Client client;


    public Repl(String serverUrl) throws ResponseException, DeploymentException, IOException {
        this.client = new Client(serverUrl, this, this);
    }

    public void run() {
        System.out.println("Welcome to Animus Chess, please login or register to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }


    public void notify(String message) {
        System.out.println(RED + message);
        printPrompt();
    }


    public void loadGame(ChessGame game, String role) {
        System.out.println();
        String board = drawChessBoard(role, game);
        System.out.println(board);

    }
}
