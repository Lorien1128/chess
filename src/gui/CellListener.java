package gui;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.PieceEvent;
import util.Point;
import util.Render;
import util.Strategy;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CellListener extends Thread implements ActionListener {
    private final int posX;
    private final int posY;
    private final JFrame frame;
    private static boolean chooseFrom;
    private static Pair<Integer, Integer> fromWhere;

    public CellListener(int index, JFrame frame) {
        super();
        posX = index % 8 + 1;
        posY = 8 - index / 8;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!chooseFrom) {
            handleNotChosenFrom();
        }
        else {
            handleChosenFrom();
        }
    }

    public void handleNotChosenFrom() {
        Board board = Board.getBoard();
        ChessPiece chessPiece = board.getChess(posX, posY);
        if (chessPiece == null) {
            MyDialog dialog = new MyDialog("该处不存在棋子！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else if (!chessPiece.isWhite()) {
            MyDialog dialog = new MyDialog("请走本方棋子！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else {
            ArrayList<Point> points = chessPiece.searchAllDrop(false);
            if (points.isEmpty()) {
                MyDialog dialog = new MyDialog("该棋子无法移动！", 2, frame);
                Thread thread = new Thread(dialog);
                thread.start();
            }
            else {
                for (ChessCell chessCell : MainPanel.getChess()) {
                    chessCell.setBorder(null);
                }
                for (Point point : points) {
                    ChessCell cell = MainPanel.getChess().get(getIndex(point));
                    cell.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                }
                chooseFrom = true;
                fromWhere = new Pair<>(posX, posY);
            }
        }
    }

    public void handleChosenFrom() {
        Board board = Board.getBoard();
        if (posX == fromWhere.getKey() && posY == fromWhere.getValue()) {
            for (ChessCell chessCell : MainPanel.getChess()) {
                chessCell.setBorder(null);
            }
            chooseFrom = false;
        }
        else if (MainPanel.getChess().get(getIndex(posX, posY)).getBorder() != null) {
            ChessPiece piece = board.getChess(fromWhere.getKey(), fromWhere.getValue());
            PieceEvent whiteEvent = board.acMove(piece, new Point(posX, posY));
            Render render = new Render();
            render.start();
            handleWhiteEvent(whiteEvent, piece);
            for (ChessCell chessCell : MainPanel.getChess()) {
                chessCell.setBorder(null);
            }
            Pair<ChessPiece, Point> blackMove = Strategy.decide(4);
            board.acMove(blackMove.getKey(), blackMove.getValue());
            MainPanel.render();
            chooseFrom = false;
        }
        else if (board.getChess(posX, posY) != null &&
                board.getChess(posX, posY).isWhite()) {
            handleNotChosenFrom();
        }
        else {
            MyDialog dialog = new MyDialog("无法移动到该处！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
    }

    public void handleWhiteEvent(PieceEvent event, ChessPiece piece) {
        if (event == PieceEvent.PAWN_PROMOTION) {
            new MyDialog("请选择兵升变目标", frame, piece);
        }
    }

    public int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }

    public int getIndex(int x, int y) {
        return 8 * (8 - y) + x - 1;
    }
}
