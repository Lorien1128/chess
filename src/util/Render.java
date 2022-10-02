package util;

import gui.MainPanel;

public class Render extends Thread {
    @Override
    public void run() {
        MainPanel.render();
    }
}
