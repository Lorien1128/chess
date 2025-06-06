package gui;

import connection.AiSocket;
import connection.PvpClient;
import connection.PvpServer;
import connection.PvpSocket;
import javafx.util.Pair;
import piece.ChessPiece;
import util.Board;
import util.Mode;
import util.PieceEvent;
import util.Point;
import util.Strategy;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Computer extends Thread {
    private final Lock lock;
    private final Condition condition;
    private final MyGui frame;
    private int count = 0;
    private final AiSocket aiSocket;
    private static Mode mode;
    private static String host;
    private static int port;
    private final PvpServer server = new PvpServer();
    private final PvpClient client = new PvpClient();
    private PvpSocket pvpSocket;
    private boolean begin = true;

    public Computer(MyGui frame, Lock lock, Condition condition) throws IOException {
        this.lock = lock;
        this.condition = condition;
        this.frame = frame;
        if (mode == Mode.OUTER_AI) {
            aiSocket = new AiSocket();
        }
        else {
            aiSocket = null;
        }
    }

    public void init() {
        count = 0;
    }

    public static void setMode(Mode mode) {
        Computer.mode = mode;
    }

    public static Mode getMode() {
        return mode;
    }

    public void countReduce() {
        count--;
    }

    public Board getBoard() {
        return Board.getBoard();
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println("computer is awake");
            if (getBoard().isCurMoveWhite() != frame.isWhite()) {
                Pair<ChessPiece, Point> blackMove = new Pair<>(null, null);
                long startTime = System.currentTimeMillis();
                if (mode == Mode.INNER_AI) {
                    if (count % 80 == 0) {
                        blackMove = Strategy.randomDecide();
                    } else {
                        blackMove = Strategy.decide(4);
                    }
                    if (getBoard().isCurMoveWhite()) {
                        blackMove = Strategy.monteCarloDecide(2, 2);
                    }
                }
                else if (mode == Mode.OUTER_AI) {
                    try {
                        assert aiSocket != null;
                        blackMove = aiSocket.queryStep(getBoard().getLastMove());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (mode == Mode.PVP_SERVER || mode == Mode.PVP_CLIENT) {
                    if (!begin || mode == Mode.PVP_SERVER) {
                        pvpSocket.send(getBoard().getLastMove());
                    }
                    blackMove = pvpSocket.receive();
                    begin = false;
                    frame.setState(frame.isWhite());
                }
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 2000) {
                    try {
                        sleep(2000 - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ChessPiece piece = blackMove.getKey();
                Point target = blackMove.getValue();

                ChessCell cell = frame.getPanel().getChess().get(getIndex(piece.getPoint()));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                cell = frame.getPanel().getChess().get(getIndex(target));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                PieceEvent blackEvent = getBoard().acMove(piece, target);
                count++;
                handleBlackEvent(blackEvent);

                frame.getPanel().render();
                Board.getBoard().setCurMoveWhite(frame.isWhite());
            }
            lock.unlock();
        }
    }

    public void handleBlackEvent(PieceEvent event) {
        if (event == PieceEvent.IN_CHECK) {
            frame.getPanel().showCheck(frame.isWhite());
        }
        else if (event == PieceEvent.CHECKMATED) {
            new MyDialog("你被将死了！",frame, true);
            if (mode == Mode.OUTER_AI) {
                assert aiSocket != null;
                aiSocket.close();
            }
        }
        else if (event == PieceEvent.DRAW) {
            new MyDialog("你将对方逼和了！",frame, true);
            if (mode == Mode.OUTER_AI) {
                assert aiSocket != null;
                aiSocket.close();
            }
        }
    }

    public int getIndex(Point point) {
        return 8 * (8 - point.getPy()) + point.getPx() - 1;
    }

    public static void setHost(String host) {
        Computer.host = host;
    }

    public static void setPort(int port) {
        Computer.port = port;
    }

    public void launchSocket() {
        if (mode == Mode.PVP_SERVER) {
            server.run(port);
            pvpSocket = server;
            MyDialog dialog = new MyDialog("黑方连接成功！", 4, frame);
            Thread thread = new Thread(dialog);
            thread.start();
        }
        else if (mode == Mode.PVP_CLIENT) {
            client.run(host, port);
            pvpSocket = client;
            wakeUp();
        }
    }

    public void wakeUp() {
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
