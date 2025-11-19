package network;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {

    private static final int PORT = 5000;
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("Game Server Started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                ClientHandler handler = new ClientHandler(socket, clients);
                clients.add(handler);
                handler.start();   // 스레드 시작

                // 2명 접속 시 게임 시작
                if (clients.size() == 2) {
                    broadcast("START");
                    startArrowGenerator();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler ch : clients) {
                ch.send(message);
            }
        }
    }

    public static void startArrowGenerator() {
        String[] arrows = {"LEFT", "RIGHT", "UP", "DOWN"};
        Random r = new Random();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String arrow = arrows[r.nextInt(arrows.length)];

                // 판정 엔진에 현재 화살표 등록
                JudgeEngine.setArrow(arrow);

                // 클라이언트들에게 전송
                broadcast("ARROW:" + arrow);
            }
        }, 0, 1000);  // 1초마다 화살표 생성
    }
}
