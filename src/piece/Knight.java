package piece;

import util.Point;
import util.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class Knight extends ChessPiece {
    private final int[] valueTable = new int[]{
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50,
    };

    public Knight(int x, int y, boolean white) {
        super(x, y, white);
        for (int i = 0; i < 64; i++) {
            if (isWhite()) {
                valueTable[i] += 350;
            }
            else {
                valueTable[i] += 352;
            }
        }
    }

    public int getValue() {
        if (isWhite()) {
            return -valueTable[8 * (8 - getY()) + getX() - 1];
        }
        else {
            return valueTable[8 * (getY() - 1) + getX() - 1];
        }
    }

    @Override
    public ArrayList<Point> searchAllDrop(boolean ignoreAttack) {
        int x = getX();
        int y = getY();
        ArrayList<Point> result = new ArrayList<>();
        result.add(new Point(x - 1, y + 2));
        result.add(new Point(x - 1, y - 2));
        result.add(new Point(x + 1, y + 2));
        result.add(new Point(x + 1, y - 2));
        result.add(new Point(x - 2, y + 1));
        result.add(new Point(x - 2, y - 1));
        result.add(new Point(x + 2, y + 1));
        result.add(new Point(x + 2, y - 1));
        ArrayList<Point> fina = new ArrayList<>();
        for (Point point : result) {
            if (point.getPx() >= 1 && point.getPx() <= 8 &&
                    point.getPy() >= 1 && point.getPy() <= 8 &&
                    !getBoard().hasChessPiece(point.getPx(), point.getPy(), isWhite())) {
                fina.add(point);
            }
        }
        if (!ignoreAttack) {
            ArrayList<ChessPiece> snapshot = Tools.copy(getBoard().getChessPieces());
            getBoard().setChessPieces(Tools.copy(snapshot));
            Point pre = new Point(getPoint());
            Iterator<Point> iterator = fina.iterator();
            while (iterator.hasNext()) {
                Point point = iterator.next();
                getBoard().move(this, point);
                if (getBoard().kingIsAttacked(isWhite())) {
                    iterator.remove();
                }
                getBoard().setChessPieces(Tools.copy(snapshot));
                getPoint().setPoint(pre);
                getBoard().addToList(this);
            }
        }
        return fina;
    }

    @Override
    public String toString() {
        if (isWhite()) {
            return "\u2658";
        }
        else {
            return "\u265E";
        }
    }
}
