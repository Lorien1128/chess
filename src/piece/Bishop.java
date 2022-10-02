package piece;

import util.Point;
import util.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class Bishop extends ChessPiece {

    public Bishop(int x, int y, boolean white) {
        super(x, y, white);
        if (white) {
            setValue(-30);
        }
        else {
            setValue(30);
        }
    }

    @Override
    public ArrayList<Point> searchAllDrop(boolean ignoreAttack) {
        int x = getX();
        int y = getY();
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

    @Override
    public String toString() {
        if (isWhite()) {
            return "\u2657";
        }
        else {
            return "\u265D";
        }
    }
}
