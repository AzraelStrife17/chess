package client;

import exception.ResponseException;
import websocket.NotificationHandler;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Scanner;

import static client.DrawBoard.drawChessBoard;
import static client.EscapeSequences.*;


public class Repl implements NotificationHandler {
    private final Client client;


    public Repl(String serverUrl) throws ResponseException, DeploymentException, IOException {
        this.client = new Client(serverUrl, this);
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
        if(message.startsWith("Loaded White")){
            String[] parts = message.split(" ");
            String gameIDStr = parts[2];
            int gameID = Integer.parseInt(gameIDStr);

            System.out.println();
            String board = drawChessBoard("white", gameID);
            System.out.println(board);
        }
        System.out.println(RED + message);
        printPrompt();
    }
}
