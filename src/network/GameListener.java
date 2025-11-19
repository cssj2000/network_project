package network;

public interface GameListener {
    void onArrow(String direcction);
    void onGameStart();
    void onJudge(String judge);
}
