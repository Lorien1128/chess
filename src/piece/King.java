package piece;

import util.Point;
import util.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class King extends ChessPiece {
    private boolean endOfGame = false;
    private final List<Integer> middleValueTable = Arrays.asList(
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -20,-30,-30,-40,-40,-30,-30,-20,
        -10,-20,-20,-20,-20,-20,-20,-10,
        20, 20,  0,  0,  0,  0, 20, 20,
        20, 30, 10,  0,  0, 10, 30, 20
    );
    private final List<Integer> endValueTable = Arrays.asList(
        -50,-40,-30,-20,-20,-30,-40,-50,
        -30,-20,-10,  0,  0,-10,-20,-30,
        -30,-10, 20, 30, 30, 20,-10,-30,
        -30,-10, 30, 40, 40, 30,-10,-30,
        -30,-10, 30, 40, 40, 30,-10,-30,
        -30,-10, 20, 30, 30, 20,-10,-30,
        -30,-30,  0,  0,  0,  0,-30,-30,
        -50,-30,-30,-30,-30,-30,-30,-50
    );

    public int getValue() {
        List<Integer> valueTable;
        if (endOfGame) {
            valueTable = endValueTable;
        }
        else {
            valueTable = middleValueTable;
        }
        if (isWhite()) {
            return -valueTable.get(8 * (8 - getY()) + getX() - 1);
        }
        else {
            return valueTable.get(8 * (getY() - 1) + getX() - 1);
        }
    }

    public King(int x, int y, boolean white) {
        super(x, y, white);
        for (int i = 0; i < 64; i++) {
            middleValueTable.set(i, middleValueTable.get(i) + 2000);
        }
        for (int i = 0; i < 64; i++) {
            endValueTable.set(i, endValueTable.get(i) + 2000);
        }
    }

    public void setEndOfGame(boolean endOfGame) {
        this.endOfGame = endOfGame;
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
