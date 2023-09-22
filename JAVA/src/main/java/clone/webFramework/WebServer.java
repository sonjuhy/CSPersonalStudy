package clone.webFramework;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebServer {// WebServer 톰캣 대용
        private final String HOSTNAME = "0.0.0.0";
        private final int PORT = 8080;
        private final int BACKLOG = 0;
        private HttpServer server = null;
        public WebServer(HttpHandler handler) throws IOException {
            this.server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
            server.createContext("/", handler);
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
