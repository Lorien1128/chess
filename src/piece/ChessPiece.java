package piece;

import util.Board;
import util.Point;

import java.util.ArrayList;

public class ChessPiece implements Cloneable {
    private Point point = new Point();
    private final boolean white;
    private boolean moved = false;
    private int value;

    public ChessPiece(boolean white) {
        this.white = white;
    }

    public ChessPiece(int x, int y, boolean white) {
        point.setPoint(x, y);
        this.white = white;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        if (value > 0) {
            return value - (Math.abs(getX() - 4) + Math.abs(getY() - 4));
        }
        else {
            return value + (Math.abs(getX() - 4) + Math.abs(getY() - 4));
        }
    }

    public ArrayList<Point> searchAllDrop(boolean ignoreAttack) {
        return null;
    }

    public ArrayList<Point> searchAllDrop() {
        return searchAllDrop(false);
    }

    public boolean isAccessible(Point point, boolean ignoreAttack) {
        ArrayList<Point> points = searchAllDrop(ignoreAttack);
        for (Point p : points) {
            if (p.isSame(point)) {
                return true;
            }
        }
        return false;
    }

    public void moveTo(Point point) {
        this.point.setPoint(point);
        moved = true;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean isMoved() {
        return moved;
    }

    public int getX() {
        return point.getPx();
    }

    public int getY() {
        return point.getPy();
    }

    public Board getBoard() {
        return Board.getBoard();
    }

    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clone = (ChessPiece) super.clone();
            clone.point = point.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
