package http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servlet {
    private static int threadLimit = 0;
    private static int runningThread = 0;
    private static boolean[] activatedThread;
    private static Queue<HttpExchange> requestQueue = new LinkedList<>();
    private static Thread[] threadPool;

    public Servlet(){
        this.threadLimit = 10;
        this.activatedThread = new boolean[10];
        threadPool = new Thread[10];
    }
    public Servlet(int num){
        this.threadLimit = num;
        this.activatedThread = new boolean[num];
        threadPool = new Thread[num];
    }
    public void start(){ // WebServer 실행
        WebServer webServer = null;
        try {
            webServer = new WebServer();
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String mainPage(){ // main page
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("   <head>");
        sb.append("       <meta charset=\"UTF-8\">");
        sb.append("       <meta name=\"author\" content=\"Dochi\">");
        sb.append("       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        sb.append("       <title>Example</title>");
        sb.append("   </head>");
        sb.append("   <body>");
        sb.append("       <h5>Hello, HttpServer!!!</h5>");
        sb.append("   </body>");
        sb.append("</html>");
        str = sb.toString();
        return str;
    }

    public static String doGet(InputStream input){ // GET 으로 들어온 요청 처리
        String str = "";
        StringBuilder sb = new StringBuilder();
        sb.append("doGET\n");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            sb.append(br.readLine());
            Thread.sleep(5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        str = sb.toString();
        return str;
    }

    public static String doPost(InputStream input){ // POST 로 들어온 요청 처리
        String str = "";
        StringBuilder sb = new StringBuilder();
        sb.append("doPOST\n");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            sb.append(br.readLine());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        str = sb.toString();
        return str;
    }

    private static class WebServer { // WebServer 톰캣 대용
        private final String HOSTNAME = "0.0.0.0";
        private final int PORT = 8080;
        private final int BACKLOG = 0;
        private HttpServer server = null;
        public WebServer() throws IOException {
            this.server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
            server.createContext("/", new ServletHandler());
        }
        public void start(){

            try{
                System.out.println(
                        //시작 로그 String 형식을 매핑해줌
                        String.format(
                                "[%s][HTTP SERVER][START]",
                                new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())
                        )
                );
                //서버 생성
                server.start();

                /*Runtime.getRuntime().addShutdownHook() -> 프로그램 종료 시 특정 작업을 수행하는 메소드
                 * 어플리케이션의 안전한 종료 처리
                 * */
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 종료 로그
                        System.out.println(
                                String.format(
                                        "[%s][HTTP SERVER][STOP]",
                                        new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())
                                )
                        );
                    }
                }));

                // Enter 입력 시 종료
                System.out.print("Please press 'Enter' to stop the server");
                System.in.read();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                // 0초 대기 후 종료
                server.stop(0);
            }
        }
    }
    private static class ServletHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("handler input");
            System.out.println("runningThread : " + runningThread);
            System.out.println("threadLimit : " + threadLimit);
            System.out.println("getMethod : " + exchange.getRequestMethod());
            System.out.println("Response Header size : " + exchange.getResponseHeaders().size());
            System.out.println("Request Header size : " + exchange.getRequestHeaders().size());
            exchange.getRequestHeaders().forEach((key, value)->{
                System.out.println("key : " + key);
                for(String str : value){
                    System.out.println("value : " + str);
                }
                System.out.println();
            });


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

            threadPool[point] = new Thread(new WorkThread(point, requestQueue.poll()));

            try{
                System.out.println("start Thread");
                threadPool[point].start();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static class WorkThread implements Runnable{
        private int point;
        private HttpExchange exchange;

        public WorkThread(){}
        public WorkThread(int point, HttpExchange exchange){
            this.point = point;
            this.exchange = exchange;
        }

        @Override
        public void run() {
            OutputStream responseBody = exchange.getResponseBody();
            try{
                StringBuilder sb = new StringBuilder();
                if(exchange.getRequestURI().getPath().equals("/")){
                    sb.append(mainPage());
                }
                else if(exchange.getRequestMethod().equals("GET")){
                    sb.append(doGet(exchange.getRequestBody()));
                }
                else if(exchange.getRequestMethod().equals("POST")){
                    sb.append(doPost(exchange.getRequestBody()));
                }
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
}
