package piece;

import util.Point;
import util.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class Pawn extends ChessPiece {

    public Pawn(int x, int y, boolean white) {
        super(x, y, white);
        if (white) {
            setValue(-10);
        }
        else {
            setValue(10);
        }
    }

    @Override
    public ArrayList<Point> searchAllDrop(boolean ignoreAttack) {
        ArrayList<Point> result = new ArrayList<>();
        int step;
        int initLoc;
        int x = getX();
        int y = getY();
        if (isWhite()) {
            step = 1;
            initLoc = 2;
        } else {
            step = -1;
            initLoc = 7;
        }
        if (getBoard().noChessPiece(x, y + step)) {
            result.add(new Point(x, y + step));
            if (getY() == initLoc &&
                    getBoard().noChessPiece(x, y + 2 * step)) {
                result.add(new Point(x, y + 2 * step));
            }
        }

        if (x + step >= 1 && x + step <= 8 &&
                getBoard().hasChessPiece(x + step, y + step, !isWhite())) {
            result.add(new Point(x + step, y + step));
        }
        if (x - step >= 1 && x - step <= 8 &&
                getBoard().hasChessPiece(x - step, y + step, !isWhite())) {
            result.add(new Point(x - step, y + step));
        }
        result.removeIf(point -> point.getPx() < 1 || point.getPx() > 8 ||
                point.getPy() < 1 || point.getPy() > 8);
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
            return "\u2659";
        }
        else {
            return "\u265F";
        }
    }
}
