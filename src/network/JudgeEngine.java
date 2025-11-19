package network;

public class JudgeEngine {

    private static String lastArrow = "NONE";

    public static void setArrow(String arrow) {
        lastArrow = arrow;
    }

    public static String judge(String input) {
        if (input.equals(lastArrow)) {
            return "PERFECT";
        }
        return "MISS";
    }
}
