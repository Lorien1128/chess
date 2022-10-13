package connection;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Point;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PvpClient implements PvpSocket {
    private Socket client;

    public void run(String host, int port) {
        try {
            client = new Socket(host, port);
            System.out.println("成功连接服务端");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Pair<ChessPiece, Point> choice) {
        ChessPiece chessPiece = choice.getKey();
        Point target = choice.getValue();
        char fromX = (char) (chessPiece.getX() + 'a' - 1);
        char fromY = (char) (chessPiece.getY() + '0');
        char toX = (char) (target.getPx() + 'a' - 1);
        char toY = (char) (target.getPy() + '0');
        String request = String.valueOf(new char[]{fromX, fromY, toX, toY});
        System.out.println("客户端发出请求：" + request);
        try {
            OutputStream out = client.getOutputStream();
            out.write(request.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pair<ChessPiece, Point> receive() {
        InputStream in = null;
        byte[] b = new byte[1024];
        int len = 0;
        try {
            in = client.getInputStream();
            len = in.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = new String(b, 0, len);
        System.out.println("获得服务端响应：" + response);
        int retFromX = response.charAt(0) - 'a' + 1;
        int retFromY = response.charAt(1) - '0';
        ChessPiece blackPiece = Board.getBoard().getChess(retFromX, retFromY);
        int retToX = response.charAt(2) - 'a' + 1;
        int retToY = response.charAt(3) - '0';
        Point point = new Point(retToX, retToY);
        return new Pair<>(blackPiece, point);
    }
}
