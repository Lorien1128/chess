import gui.Computer;
import gui.MyGui;
import util.Board;
import util.Mode;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        Board board = Board.getBoard();
        board.init();
        MyGui gui = new MyGui(true);
        board.setFrame(gui);
        if (Computer.getMode() == Mode.BOTH_PLAYER) {
            MyGui other = new MyGui(false);
            gui.setOtherPlayer(other);
            other.setOtherPlayer(gui);
        }
    }
}
