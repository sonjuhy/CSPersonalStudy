package thread;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class CSProcessing {
    static Runtime r;
    static Process p;
    public void init() {
        r = Runtime.getRuntime();
    }
    public void start(){
        try{
            p = r.exec("firefox");
            p.waitFor(10, TimeUnit.SECONDS);
            p.destroy();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
