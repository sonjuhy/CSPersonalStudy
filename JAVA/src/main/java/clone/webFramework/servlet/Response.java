package clone.webFramework.servlet;

import com.sun.net.httpserver.Headers;

import java.io.OutputStream;

public class Response {
    private OutputStream responseBody;
    private Headers responseHeaders;
    private int responseCode;

    public OutputStream getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(OutputStream responseBody) {
        this.responseBody = responseBody;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Headers responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Response(){}
    public Response(OutputStream body, Headers headers, int code){
        this.responseBody = body;
        this.responseHeaders = headers;
        this.responseCode = code;
    }
}
