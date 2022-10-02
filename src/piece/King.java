package piece;

import util.Point;
import util.Tools;

import java.util.ArrayList;
import java.util.Iterator;

public class King extends ChessPiece {
    public King(int x, int y, boolean white) {
        super(x, y, white);
        if (white) {
            setValue(-900);
        }
        else {
            setValue(900);
        }
    }

    @Override
    public ArrayList<Point> searchAllDrop(boolean ignoreAttack) {
        int x = getX();
        int y = getY();
        ArrayList<Point> result = new ArrayList<>();
        result.add(new Point(x, y + 1));
        result.add(new Point(x, y - 1));
        result.add(new Point(x + 1, y + 1));
        result.add(new Point(x + 1, y));
        result.add(new Point(x + 1, y - 1));
        result.add(new Point(x - 1, y + 1));
        result.add(new Point(x - 1, y));
        result.add(new Point(x - 1, y - 1));
        result.removeIf(point -> point.getPx() < 1 || point.getPx() > 8 ||
                point.getPy() < 1 || point.getPy() > 8 ||
                getBoard().hasChessPiece(point.getPx(), point.getPy(), isWhite()));
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
            if (getBoard().beAbleToCastling(isWhite(), true)) {
                result.add(new Point(3, y));
            }
            if (getBoard().beAbleToCastling(isWhite(), false)) {
                result.add(new Point(7, y));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        if (isWhite()) {
            return "\u2654";
        }
        else {
            return "\u265A";
        }
    }
}
