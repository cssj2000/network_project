package network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private GameListener listener; // UI로 이벤트 보내는 인터페이스

    public GameClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // 서버에서 오는 메시지 받기
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {

                        // ARROW
                        if (msg.startsWith("ARROW:")) {
                            String direction = msg.split(":")[1];
                            System.out.println("화면에 화살표 표시: " + direction);

                            if (listener != null)
                                listener.onArrow(direction);
                        }

                        // START
                        else if (msg.equals("START")) {
                            if (listener != null)
                                listener.onGameStart();
                        }

                        // JUDGE
                        else if (msg.startsWith("JUDGE:")) {
                            String judge = msg.split(":")[1];
                            if (listener != null)
                                listener.onJudge(judge);
                        }

                        // 기타 메시지 (필요하면 여기에 추가)
                        else {
                            System.out.println("Unknown msg: " + msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 메시지 서버로 보내기
    public void send(String msg) {
        out.println(msg);
    }

    // UI 담당에게 전달할 listener 등록
    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public static void main(String[] args) {
        GameClient client = new GameClient("127.0.0.1", 5000);

        // 메시지 보내는 테스트
        new Scanner(System.in).forEachRemaining(line -> {
            client.send(line);
        });
    }

}


