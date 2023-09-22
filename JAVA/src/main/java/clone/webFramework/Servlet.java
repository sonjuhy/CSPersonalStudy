package clone.webFramework;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class Servlet {
    private HttpExchange exchange;
    public Servlet(HttpExchange exchange){
        this.exchange = exchange;
    }
    public void service(){
        // header check
        Headers headers = exchange.getRequestHeaders();
        String ContentType = "404";
        if(headers.get("Content-type").get(0) != null) ContentType = headers.get("Content-type").get(0);

        switch (ContentType){
            case "text/plain":
                // return string type
                break;
            case "text/html":
                // return html type
                break;
            case "application/json":
                // return type : json
                break;
            default:
                // return 404 page
                break;
        }

        String restType = "GET";
        if(exchange.getRequestMethod() != null) restType = exchange.getRequestMethod();
        switch(restType){
            case "GET":
                // doGET
                break;
            case "POST":
                // doPOST
                break;
            case "UPDATE":
                // doUPDATE
                break;
            case "DELETE":
                // doDELETE
                break;
            default:
                // return error
                break;
        }
    }
}
