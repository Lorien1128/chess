package gui;

import piece.ChessPiece;
import util.Board;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private static final ArrayList<ChessCell> chess = new ArrayList<>();

    public MainPanel(JFrame frame) {
        setLayout(new GridLayout(9, 9, 0, 0));
        for (int i = 0; i < 64; i++) {
            ChessCell chessCell = new ChessCell(i, frame);
            chessCell.setBorder(null);
            add(chessCell);
            chess.add(chessCell);
        }
    }

    public static ArrayList<ChessCell> getChess() {
        return chess;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    public static void render() {
        for (ChessCell chessCell : chess) {
            int x = chessCell.getPosX();
            int y = chessCell.getPosY();
            Board board = Board.getBoard();
            ChessPiece chessPiece = board.getChess(x, y);
            if (chessPiece != null) {
                chessCell.setText(chessPiece.toString());
            }
            else {
                chessCell.setText(null);
            }
        }
    }
}
