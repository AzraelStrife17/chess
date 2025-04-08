package client;

import websocket.ServerMessageHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static client.EscapeSequences.*;


public class Repl implements ServerMessageHandler {
    private final Client client;


    public Repl(String serverUrl) {
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


    public void notify(ServerMessage.ServerMessageType serverMessageType) {
        System.out.println(RED + serverMessageType);
        printPrompt();
    }
}
