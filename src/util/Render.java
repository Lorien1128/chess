package util;

import gui.MainPanel;

public class Render extends Thread {
    private final MainPanel panel;

    public Render(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        panel.render();
    }
}
