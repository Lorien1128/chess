import gui.MyGui;
import util.Board;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        Board board = Board.getBoard();
        board.init();
        MyGui gui = new MyGui();
        board.setFrame(gui);
    }
}
