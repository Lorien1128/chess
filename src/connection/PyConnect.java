package connection;

public class PyConnect extends Thread {
    private Process process;

    public void run() {
        String environment = "python";
        String path = System.getProperty("user.dir") + "/outerAI/sunfish-master/sunfish.py";
        String cmd = environment + " " + path;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            setProcess(proc);
            proc.waitFor();
            System.out.println("python AI 终止");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void setProcess(Process process) {
        this.process = process;
    }

    public synchronized void close() {
        process.destroy();
    }
}
