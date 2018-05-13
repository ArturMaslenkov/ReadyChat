package lv.arturmaslenkov;

import org.postgresql.Driver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    List<User> clients = new ArrayList<User>();
    List<String> msgHistory = new ArrayList<String>();
    List<String> nicknameList = new ArrayList<String>();

    private Server(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while(true) {
            User user = new User();
            user.socket = serverSocket.accept();
            clients.add(user);
            new Thread(new ClientHandler(this, user)).start();
        }
    }

    void broadcastMessages(String msg, List<User> clients, User msgAuthor) throws IOException {
        for (User client : clients ) {
            if (msgAuthor.socket != client.socket ) {
                PrintStream streamOut = new PrintStream(client.socket.getOutputStream());
                streamOut.println(msgAuthor.nickname + " - " + msg);
            }
        }
    }

    void sendMessagesHistory(List<String> msgHistory, User user) throws IOException {
        for (String string : msgHistory) {
            PrintStream streamOut = new PrintStream(user.socket.getOutputStream());
            streamOut.println(string);
        }
    }

    void allert(User user, String message) throws IOException {
        PrintStream streamout = new PrintStream((user.socket.getOutputStream()));
        streamout.println(message);
    }

    public static void main(String[] args) throws IOException {
        new Server(8080);
    }
}

class ClientHandler implements Runnable {
    private Server serverSocket;
    private User client;
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    ClientHandler(Server serverSocket, User user) {
        this.serverSocket = serverSocket;
        this.client = user;
    }

    @Override
    public void run() {
        String msg;
        Connection con = null;
        Statement stmnt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting...");
            DriverManager.registerDriver(new Driver());

            con = DriverManager.getConnection("jdbc:postgresql://ec2-79-125-117-53.eu-west-1.compute.amazonaws.com:5432/d1d8vh7p5kpuj9?user=lckrpemjcoirra&password=f26666ee47aeace16c9689b8b4a49d52c07a68b616aea82f1ef8b827a06700b2&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            this.serverSocket.sendMessagesHistory(this.serverSocket.msgHistory, this.client);
            this.serverSocket.allert(this.client, "You successful connected to chat! Please set uo your nickname with command (/nickname)");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Scanner sc = new Scanner(this.client.socket.getInputStream());
            while(sc.hasNextLine()) {
                msg = sc.nextLine();

                //System.out.println(msg);
                if (this.client.nickname == null) {
                    if (msg.contains("/nickname")) {
                        msg = msg.replace("/nickname", "").trim();
                        if (this.serverSocket.nicknameList.contains(msg)) {
                            this.serverSocket.allert(this.client, "This nickname is used, chose another user name");
                        } else {
                            String nickname = msg;
                            this.serverSocket.nicknameList.add(nickname);
                            this.client.nickname = nickname;
                        }
                    } else {
                        this.serverSocket.allert(this.client, "Please set up your nickname with command (/nickname)");
                    }
                } else {
                        stmnt = con.createStatement();
                        msg = msg.replace("'","''");
                        stmnt.execute("insert into chat values ('" + this.client.nickname + "', '" + msg + "')");

                        this.serverSocket.msgHistory.add(this.client.nickname + " - " + msg);
                        this.serverSocket.broadcastMessages(msg, this.serverSocket.clients, this.client);
                    }
                }
            } catch (IOException | SQLException e1) {
            e1.printStackTrace();
        }
    }
}