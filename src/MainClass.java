import gui.MyGUI;
import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Point;
import util.Strategy;

import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Board board = Board.getBoard();
        board.init();
        MyGUI gui = new MyGUI();
        board.setFrame(gui);
    }

    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = Board.getBoard();
        board.init();
        System.out.print(board);
        while (true) {
            System.out.println("请输入要移动棋子的坐标");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (x == 0) {
                break;
            }
            ChessPiece chessPiece = board.getChess(x, y);
            if (chessPiece == null) {
                System.out.println("无棋子，请重新输入");
                continue;
            }
            ArrayList<Point> points = chessPiece.searchAllDrop(false);
            if (points.isEmpty()) {
                System.out.println("该棋子无法移动");
                continue;
            }
            System.out.print("可选移动至：\n");
            for (Point point : points) {
                System.out.print((points.indexOf(point) + 1) + " : " + point);
            }
            System.out.print("请输入你的选择\n");
            int choice = scanner.nextInt();
            board.acMove(chessPiece, points.get(choice - 1));
            Pair<ChessPiece, Point> blackMove = Strategy.decide(4);
            System.out.print("黑方走" + blackMove.getKey().toString() + "到"
                    + blackMove.getValue().toString());
            board.acMove(blackMove.getKey(), blackMove.getValue());
            System.out.print(board);
        }
    }
}
