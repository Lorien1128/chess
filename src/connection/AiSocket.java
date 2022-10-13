package connection;

import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiSocket {
    private Socket socket;
    private final PyConnect connect;

    public AiSocket() {
        System.out.println("AI socket 建立");
        freePort();
        PyConnect connect = new PyConnect();
        this.connect = connect;
        connect.start();
        socket = null;
        try {
            socket = new Socket("",8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pair<ChessPiece, Point> queryStep(Pair<ChessPiece, Point> choice) throws IOException {
        ChessPiece chessPiece = choice.getKey();
        Point target = choice.getValue();
        char fromX = (char) (chessPiece.getX() + 'a' - 1);
        char fromY = (char) (chessPiece.getY() + '0');
        char toX = (char) (target.getPx() + 'a' - 1);
        char toY = (char) (target.getPy() + '0');
        String request = String.valueOf(new char[]{fromX, fromY, toX, toY});
        System.out.println("发出请求：" + request);
        OutputStream out = socket.getOutputStream();
        out.write(request.getBytes());
        InputStream in = socket.getInputStream();
        byte[] b = new byte[1024];
        int len = in.read(b);
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

    public void freePort() {
        String cmd = "netstat -ano";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(
                    proc.getInputStream(), proc.getErrorStream());
            InputStreamReader reader = new InputStreamReader(sequenceInputStream, "gbk");
            BufferedReader br = new BufferedReader(reader);
            String line;
            ArrayList<String> pid = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("([^ ]+)");
                Matcher matcher = pattern.matcher(line);
                ArrayList<String> elems = new ArrayList<>();
                while (matcher.find()) {
                    elems.add(matcher.group(0));
                }
                if (elems.size() <= 3) {
                    continue;
                }
                if (elems.get(0).equals("TCP") && (elems.get(1).equals("0.0.0.0:8080")
                        || elems.get(1).equals("127.0.0.1:8080"))) {
                    pid.add(elems.get(4));
                }
            }
            proc.destroy();
            for (String id : pid) {
                cmd = "taskkill /t /f /im " + id;
                proc = Runtime.getRuntime().exec(cmd);
                sequenceInputStream = new SequenceInputStream(
                        proc.getInputStream(), proc.getErrorStream());
                reader = new InputStreamReader(sequenceInputStream, "gbk");
                br = new BufferedReader(reader);
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                proc.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect.close();
    }
}
