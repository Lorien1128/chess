package connection;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Point;

public interface PvpSocket {
    void send(Pair<ChessPiece, Point> choice);

    Pair<ChessPiece, Point> receive();
}
