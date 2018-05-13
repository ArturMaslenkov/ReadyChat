package lv.arturmaslenkov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    static class Message implements Runnable {
        BufferedReader in;

        Message(Socket input) throws IOException {
           this.in = new BufferedReader(new InputStreamReader(input.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(in.readLine());
                }
            } catch (IOException e) {}
        }
    }

    public static void main(String[] args) throws IOException {
       Socket client = new Socket("127.0.0.1", 8888);

        PrintStream output = new PrintStream(client.getOutputStream());
        new Thread(new Message(client)).start();

        Scanner sc = new Scanner(System.in);
        String msg = "";
        while (true){
            if (sc.hasNextLine()) {
                msg = sc.nextLine();
                if (Objects.equals(msg, "exit")) {
                    break;
                }
                output.println(msg);
            }

        }
        client.close();
        output.close();
    }
}