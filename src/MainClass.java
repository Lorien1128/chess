import gui.MyGui;
import util.Board;

//TODO: 难度选择
public class MainClass {
    public static void main(String[] args) {
        Board board = Board.getBoard();
        board.init();
        MyGui gui = new MyGui();
        board.setFrame(gui);
    }
}
