package clone.webFramework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class ServletContainer {
    private static int threadLimit = 0;
    private static int runningThread = 0;
    private static boolean[] activatedThread;
    private static Queue<HttpExchange> requestQueue = new LinkedList<>();
    private static Thread[] threadPool;

    public ServletContainer(){
        this.threadLimit = 10;
        this.activatedThread = new boolean[10];
        threadPool = new Thread[10];
    }
    public ServletContainer(int num){
        this.threadLimit = num;
        this.activatedThread = new boolean[num];
        threadPool = new Thread[num];
    }
    public void start(){ // WebServer 실행

    }
    private static class ServletHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("handler input");
            System.out.println("runningThread : " + runningThread);
            System.out.println("threadLimit : " + threadLimit);
            System.out.println("getMethod : " + exchange.getRequestMethod());

            while(runningThread >= threadLimit) {
                try {
                    System.out.println("waiting...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            requestQueue.add(exchange);
            runningThread++;
            int point = -1;
            for(int i=0;i<threadLimit;i++){
                if(!activatedThread[i]){
                    point = i;
                    activatedThread[i] = true;
                    break;
                }
            }

            threadPool[point] = new Thread(new TaskThread(point, runningThread, threadPool, activatedThread, requestQueue.poll()));

            try{
                System.out.println("start Thread");
                threadPool[point].start();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
