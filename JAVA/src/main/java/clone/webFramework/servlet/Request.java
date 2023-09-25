package clone.webFramework.servlet;

import com.sun.net.httpserver.Headers;

import java.io.InputStream;
import java.net.URL;

public class Request {
    private InputStream requestBody;
    private Headers requestHeaders;
    private String requestMethod;
    private URL requestURL;

    public InputStream getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(InputStream requestBody) {
        this.requestBody = requestBody;
    }

    public Headers getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Headers requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public URL getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(URL requestURL) {
        this.requestURL = requestURL;
    }

    public Request(){}
    public Request(InputStream body, Headers headers, String method, URL url){
        this.requestBody = body;
        this.requestHeaders = headers;
        this.requestMethod = method;
        this.requestURL = url;
    }
}
