package util;

public class Point implements Cloneable {
    private int px;
    private int py;

    public Point(int x, int y) {
        px = x;
        py = y;
    }

    public Point(Point point) {
        px = point.px;
        py = point.py;
    }

    public Point() {}

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }

    public void add(Point point) {
        px += point.px;
        py += point.py;
    }

    public void setPoint(int x, int y) {
        px = x;
        py = y;
    }

    public void setPoint(Point point) {
        px = point.px;
        py = point.py;
    }

    public boolean isSame(Point point) {
        return px == point.px && py == point.py;
    }

    @Override
    public String toString() {
        return "x : " + px + ", y : " + py + "\n";
    }

    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
