package piece;

import util.Point;
import util.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class Queen extends ChessPiece {
    private final int[] valueTable = new int[]{
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
        -5,  0,  5,  5,  5,  5,  0, -5,
        0,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20
    };

    public Queen(int x, int y, boolean white) {
        super(x, y, white);
        for (int i = 0; i < 64; i++) {
            if (isWhite()) {
                valueTable[i] += 900;
            }
            else {
                valueTable[i] += 902;
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
        result.addAll(moveStraight(x, y));
        result.addAll(moveSideWays(x, y));
        if (!ignoreAttack) {
            ArrayList<ChessPiece> snapshot = Tools.copy(getBoard().getChessPieces());
            getBoard().setChessPieces(Tools.copy(snapshot));
            Point pre = new Point(getPoint());
            Iterator<Point> iterator = result.iterator();
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
        return result;
    }

    private ArrayList<Point>  moveStraight(int x, int y) {
        ArrayList<Point> res = new ArrayList<>();
        for (int i = x - 1; i >= 1; i--) {
            if (getBoard().hasChessPiece(i, y, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(i, y, !isWhite())) {
                res.add(new Point(i, y));
                break;
            }
            res.add(new Point(i, y));
        }
        for (int i = x + 1; i <= 8; i++) {
            if (getBoard().hasChessPiece(i, y, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(i, y, !isWhite())) {
                res.add(new Point(i, y));
                break;
            }
            res.add(new Point(i, y));
        }
        for (int i = y - 1; i >= 1; i--) {
            if (getBoard().hasChessPiece(x, i, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(x, i, !isWhite())) {
                res.add(new Point(x, i));
                break;
            }
            res.add(new Point(x, i));
        }
        for (int i = y + 1; i <= 8; i++) {
            if (getBoard().hasChessPiece(x, i, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(x, i, !isWhite())) {
                res.add(new Point(x, i));
                break;
            }
            res.add(new Point(x, i));
        }
        return res;
    }

    private ArrayList<Point> moveSideWays(int x, int y) {
        ArrayList<Point> result = new ArrayList<>();
        for (int i = 1; x - i >= 1 && y - i >= 1; i++) {
            if (getBoard().hasChessPiece(x - i, y - i, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(x - i, y - i, !isWhite())) {
                result.add(new Point(x - i, y - i));
                break;
            }
            result.add(new Point(x - i, y - i));
        }
        for (int i = 1; x + i <= 8 && y + i <= 8; i++) {
            if (getBoard().hasChessPiece(x + i, y + i, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(x + i, y + i, !isWhite())) {
                result.add(new Point(x + i, y + i));
                break;
            }
            result.add(new Point(x + i, y + i));
        }
        for (int i = 1; x - i >= 1 && y + i <= 8; i++) {
            if (getBoard().hasChessPiece(x - i, y + i, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(x - i, y + i, !isWhite())) {
                result.add(new Point(x - i, y + i));
                break;
            }
            result.add(new Point(x - i, y + i));
        }
        for (int i = 1; x + i <= 8 && y - i >= 1; i++) {
            if (getBoard().hasChessPiece(x + i, y - i, isWhite())) {
                break;
            }
            else if (getBoard().hasChessPiece(x + i, y - i, !isWhite())) {
                result.add(new Point(x + i, y - i));
                break;
            }
            result.add(new Point(x + i, y - i));
        }
        return result;
    }

    @Override
    public String toString() {
        if (isWhite()) {
            return "\u2655";
        }
        else {
            return "\u265B";
        }
    }
}
