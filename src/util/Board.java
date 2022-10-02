package util;

import gui.MyDialog;
import javafx.util.Pair;
import piece.Bishop;
import piece.ChessPiece;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Objects;

public class Board {
    private static final Board BOARD = new Board();
    private ArrayList<ChessPiece> chessPieces = new ArrayList<>();
    private JFrame frame;

    private Board() {
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void init() {
        for (int i = 0; i <= 63; i++) {
            chessPieces.add(null);
        }
        init(true);
        init(false);
    }

    private void init(boolean white) {
        int pawnLine;
        int otherLine;
        if (white) {
            pawnLine = 2;
            otherLine = 1;
        }
        else {
            pawnLine = 7;
            otherLine = 8;
        }
        for (int x = 1; x <= 8; x++) {
            addToList(new Pawn(x, pawnLine, white));
        }
        addToList(new Rook(1, otherLine, white));
        addToList(new Rook(8, otherLine, white));
        addToList(new Knight(2, otherLine, white));
        addToList(new Knight(7, otherLine, white));
        addToList(new Bishop(3, otherLine, white));
        addToList(new Bishop(6, otherLine, white));
        addToList(new Queen(4, otherLine, white));
        addToList(new King(5, otherLine, white));
    }

    public void addToList(ChessPiece chessPiece) {
        int x = chessPiece.getX();
        int y = chessPiece.getY();
        int index = getIndex(x, y);
        chessPieces.set(index, chessPiece);
    }

    private int getIndex(int x, int y) {
        return 8 * (y - 1) + x - 1;
    }

    public static Board getBoard() {
        return BOARD;
    }

    public ArrayList<ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void setChessPieces(ArrayList<ChessPiece> chessPieces) {
        this.chessPieces = chessPieces;
    }

    public boolean noChessPiece(int x, int y) {
        if (x < 1 || x > 8 || y < 1 || y > 8) {
            return false;
        }
        ChessPiece chessPiece = chessPieces.get(getIndex(x, y));
        return chessPiece == null;
    }

    public boolean hasChessPiece(int x, int y, boolean white) {
        if (x < 1 || x > 8 || y < 1 || y > 8) {
            return true;
        }
        ChessPiece chessPiece = chessPieces.get(getIndex(x, y));
        return chessPiece != null && chessPiece.isWhite() == white;
    }

    private ChessPiece findKing(boolean white) {
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece instanceof King
                    && chessPiece.isWhite() == white) {
                return chessPiece;
            }
        }
        return null;
    }

    private ChessPiece findLeftRook(boolean white) {
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece instanceof Rook
                    && chessPiece.isWhite() == white &&
                    chessPiece.getX() == 1 &&
                    !chessPiece.isMoved()) {
                return chessPiece;
            }
        }
        return null;
    }

    private ChessPiece findRightRook(boolean white) {
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece instanceof Rook
                    && chessPiece.isWhite() == white &&
                    chessPiece.getX() == 8 &&
                    !chessPiece.isMoved()) {
                return chessPiece;
            }
        }
        return null;
    }

    public boolean kingIsAttacked(boolean white) {
        King king = (King) findKing(white);
        assert king != null;
        Point kingLoc = king.getPoint();
        for (ChessPiece chessPiece : chessPieces) {
            if (!(chessPiece instanceof King) && chessPiece != null &&
                    (chessPiece.isWhite() ^ white) &&
                    chessPiece.isAccessible(kingLoc, true)) {
                return true;
            }
        }
        return false;
    }

    private boolean kingNotAbleToPass(int x, int y, boolean white) {
        for (ChessPiece chessPiece : chessPieces) {
            if (!(chessPiece instanceof King) && chessPiece != null &&
                    (chessPiece.isWhite() ^ white &&
                    chessPiece.isAccessible(new Point(x, y), true))) {
                return true;
            }
        }
        return false;
    }

    public boolean beAbleToCastling(boolean white, boolean left) {
        King king = (King) findKing(white);
        assert king != null;
        if (king.isMoved()) {
            return false;
        }
        if (left) {
            Rook rook = (Rook) findLeftRook(white);
            if (rook == null) {
                return false;
            }
            if (king.getY() != rook.getY()) {
                return false;
            }
            int y = king.getY();
            if (kingNotAbleToPass(3, y, white) || kingNotAbleToPass(4, y, white) ||
                    kingNotAbleToPass(5, y, white)) {
                return false;
            }
            return noChessPiece(2, y) && noChessPiece(3, y)
                    && noChessPiece(4, y);
        }
        else  {
            Rook rook = (Rook) findRightRook(white);
            if (rook == null) {
                return false;
            }
            if (king.getY() != rook.getY()) {
                return false;
            }
            int y = king.getY();
            if (kingNotAbleToPass(5, y, white) || kingNotAbleToPass(6, y, white) ||
                    kingNotAbleToPass(7, y, white)) {
                return false;
            }
            return noChessPiece(6, y) && noChessPiece(7, y);
        }
    }

    public ChessPiece getChess(int x, int y) {
        return chessPieces.get(getIndex(x, y));
    }

    @Override
    public String toString() {
        ArrayList<String> map = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 3; j++) {
                map.add("\u2F1E");
                map.add("\u2F1E");
            }
        }
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece != null) {
                int x = chessPiece.getX();
                int y = chessPiece.getY();
                map.set(8 * (y - 1) + (x - 1), chessPiece.toString());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            stringBuilder.append(i + 1);
            for (int j = 0; j <= 7; j++) {
                stringBuilder.append(map.get(i * 8 + j));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.append("0一二三四五六七八\n").toString();
    }

    public String[] getElements() {
        String[] strings = new String[64];
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece != null) {
                int x = chessPiece.getX();
                int y = chessPiece.getY();
                strings[8 * (8 - y) + (x - 1)] = chessPiece.toString();
            }
        }
        return strings;
    }

    public ArrayList<Pair<ChessPiece, Point>> getAllChoices(boolean white) {
        ArrayList<Pair<ChessPiece, Point>> choices = new ArrayList<>();
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece != null && chessPiece.isWhite() == white) {
                ArrayList<Point> points = chessPiece.searchAllDrop();
                for (Point point : points) {
                    choices.add(new Pair<>(chessPiece, point));
                }
            }
        }
        return choices;
    }

    public int sumOfValue() {
        int sum = 0;
        for (ChessPiece chessPiece : chessPieces) {
            if (chessPiece != null) {
                sum += chessPiece.getValue();
            }
        }
        return sum;
    }

    private void castling(ChessPiece chessPiece, Point destination) {
        int y = chessPiece.getY();
        boolean white = chessPiece.isWhite();
        if (destination.getPx() > chessPiece.getX()) {
            ChessPiece rightRook = findRightRook(white);
            Objects.requireNonNull(rightRook).
                    moveTo(new Point(6, y));
            chessPieces.set(getIndex(6, y), rightRook);
            chessPieces.set(getIndex(8, y), null);
        }
        else {
            ChessPiece leftRook = findLeftRook(white);
            Objects.requireNonNull(leftRook).
                    moveTo(new Point(4, y));
            chessPieces.set(getIndex(4, y), leftRook);
            chessPieces.set(getIndex(1, y), null);
        }
    }

    public PieceEvent acMove(ChessPiece preChessPiece, Point destination) {
        ChessPiece cp = getChess(destination.getPx(), destination.getPy());
        ChessPiece chessPiece = getChess(preChessPiece.getX(), preChessPiece.getY());
        // 吃子
        if (cp != null) {
            chessPieces.set(getIndex(cp.getX(), cp.getY()), null);
        }
        // 王车易位
        if (chessPiece instanceof King &&
                Math.abs(destination.getPx() - chessPiece.getX()) == 2) {
            castling(chessPiece, destination);
        }
        chessPieces.set(getIndex(chessPiece.getX(), chessPiece.getY()), null);
        chessPieces.set(getIndex(destination.getPx(), destination.getPy()), chessPiece);
        chessPiece.moveTo(destination);
        // 兵升变
        if (chessPiece instanceof Pawn &&
                chessPiece.getY() == 1) {
            int x = chessPiece.getX();
            int y = chessPiece.getY();
            boolean white = chessPiece.isWhite();
            addToList(new Queen(x, y, white));
            MyDialog dialog = new MyDialog("黑方完成了兵升变！", 2, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else if (chessPiece instanceof Pawn &&
                chessPiece.getY() == 8) {
            return PieceEvent.PAWN_PROMOTION;
        }
        boolean enemyIsWhite = !chessPiece.isWhite();
        // 将军，将死，和棋
        boolean attacked;
        boolean noway;
        if (enemyIsWhite) {
            attacked = kingIsAttacked(true);
            noway = getAllChoices(true).isEmpty();
        }
        else {
            attacked = kingIsAttacked(false);
            noway = getAllChoices(false).isEmpty();
        }
        if (attacked && noway) {
            return PieceEvent.CHECKMATED;
        }
        else if (!attacked && noway) {
            return PieceEvent.DRAW;
        }
        else if (attacked) {
            return PieceEvent.IN_CHECK;
        }
        return PieceEvent.NO_EVENT;
    }

    public void pawnPromotion(int num, ChessPiece chessPiece) {
        int x = chessPiece.getX();
        int y = chessPiece.getY();
        boolean white = chessPiece.isWhite();
        switch (num) {
            case 1:
                addToList(new Rook(x, y, white));
                break;
            case 2:
                addToList(new Knight(x, y, white));
                break;
            case 3:
                addToList(new Bishop(x, y, white));
                break;
            default:
                addToList(new Queen(x, y, white));
        }
        acMove(chessPiece, chessPiece.getPoint());
    }

    public void move(Pair<ChessPiece, Point> choice) {
        ChessPiece preChessPiece = choice.getKey();
        ChessPiece chessPiece = getChess(preChessPiece.getX(), preChessPiece.getY());
        Point destination = choice.getValue();
        ChessPiece cp = getChess(destination.getPx(), destination.getPy());
        // 吃子
        if (cp != null) {
            chessPieces.set(getIndex(cp.getX(), cp.getY()), null);
        }
        // 王车易位
        if (chessPiece instanceof King &&
                Math.abs(destination.getPx() - chessPiece.getX()) == 2) {
            int y = chessPiece.getY();
            boolean white = chessPiece.isWhite();
            if (destination.getPx() > chessPiece.getX()) {
                ChessPiece rightRook = findRightRook(white);
                Objects.requireNonNull(rightRook).
                        moveTo(new Point(6, y));
                chessPieces.set(getIndex(6, y), rightRook);
                chessPieces.set(getIndex(8, y), null);
            }
            else {
                ChessPiece leftRook = findLeftRook(white);
                Objects.requireNonNull(leftRook).
                        moveTo(new Point(4, y));
                chessPieces.set(getIndex(4, y), leftRook);
                chessPieces.set(getIndex(1, y), null);
            }
        }
        chessPieces.set(getIndex(destination.getPx(), destination.getPy()), chessPiece);
        chessPieces.set(getIndex(chessPiece.getX(), chessPiece.getY()), null);
        chessPiece.moveTo(destination);
        // 兵升变
        if (chessPiece instanceof Pawn &&
                (chessPiece.getY() == 1 || chessPiece.getY() == 8)) {
            int x = chessPiece.getX();
            int y = chessPiece.getY();
            boolean white = chessPiece.isWhite();
            addToList(new Queen(x, y, white));
        }
    }

    public void move(ChessPiece chessPiece, Point destination) {
        ChessPiece cp = getChess(destination.getPx(), destination.getPy());
        if (cp != null) {
            chessPieces.set(getIndex(cp.getX(), cp.getY()), null);
        }
        chessPieces.set(getIndex(destination.getPx(), destination.getPy()), chessPiece);
        chessPieces.set(getIndex(chessPiece.getX(), chessPiece.getY()), null);
        chessPiece.getPoint().setPoint(destination);
    }
}
