package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

//서버 안 플레이어 관리
public class ClientHandler extends Thread{

    private Socket socket;
    private List<ClientHandler> clients;
    private PrintWriter out;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run(){
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String msg;

            while((msg = in.readLine())!= null) {
                System.out.println("Received: " + msg);

                if (msg.startsWith("INPUT:")) {
                    String dir = msg.split(":")[1]; // LEFT, RIGHT ...
                    String judge = JudgeEngine.judge(dir); // 판정 수행
                    GameServer.broadcast("JUDGE:" + judge);

                    continue;
                }

                //다른 모든 클라들에게 전달
                GameServer.broadcast(msg);
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

}
