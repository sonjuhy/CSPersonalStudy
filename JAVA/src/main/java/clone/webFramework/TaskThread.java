package clone.webFramework;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TaskThread implements Runnable{
    private int point;
    private HttpExchange exchange;

    private int runningThread;
    private Thread[] threadPool;
    private boolean[] activatedThread;

    public TaskThread(){}
    public TaskThread(int point, HttpExchange exchange){
        this.point = point;
        this.exchange = exchange;
    }
    public TaskThread(int point, int threadNum, Thread[] threadPool, boolean[] activatedThread, HttpExchange exchange){
        this.point = point;
        this.exchange = exchange;
        this.runningThread = threadNum;
        this.threadPool = threadPool;
        this.activatedThread = activatedThread;
    }
    @Override
    public void run() {
        OutputStream responseBody = exchange.getResponseBody();
        try{
            StringBuilder sb = new StringBuilder();
//                if(exchange.getRequestURI().getPath().equals("/")){
//                    sb.append(mainPage());
//                }
//                else if(exchange.getRequestMethod().equals("GET")){
//                    sb.append(doGet(exchange.getRequestBody()));
//                }
//                else if(exchange.getRequestMethod().equals("POST")){
//                    sb.append(doPost(exchange.getRequestBody()));
//                }
            // UTF-8 인코딩 작업 (위의 html을 그려낸 작업을 인코딩 해주는 것)
            ByteBuffer bb = StandardCharsets.UTF_8.encode(sb.toString());
            int contentLength = bb.limit();
            byte[] content = new byte[contentLength];
            bb.get(content, 0, contentLength);

            /* Response headers 보내는 type에 따라서 다르게 설정해주면 된다.
             * 그려낸 화면이 응답될 때 뜨지 않는다면 이부분의 문제일 경우가 다분하다.
             * 텍스트, js, css를 가져오고 싶다면 text/plain
             * HTML의 경우엔 text/html
             * JSON은 application/json 으로 설정해주면 된다.
             * */
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "text/html;charset=UTF-8");
            headers.add("Content-Length", String.valueOf(contentLength));

            // Send Response headers(상태코드, contentLength)
            exchange.sendResponseHeaders(200, contentLength);

            responseBody.write(content);

            //Response Header을 보낸 후에는 반드시 닫아주어야 한다.
            responseBody.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("exchange closed\n");
            exchange.close();
            runningThread--;
            threadPool[point] = null;
            activatedThread[point] = false;
        }
    }
}
