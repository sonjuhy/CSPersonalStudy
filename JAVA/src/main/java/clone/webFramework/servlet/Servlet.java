package clone.webFramework.servlet;

import java.io.IOException;

public class Servlet implements ServletInterface{

    @Override
    public void init() {

    }

    @Override
    public void service(Request request, Response response) throws IOException{
        if("GET".equals(request.getRequestMethod())){
            doGet(request, response);
        }
        else {
            doPost(request, response);
        }

    }

    @Override
    public void destroy() {

    }

    protected void doGet(Request request, Response response) throws IOException{

    }
    protected void doPost(Request request, Response response) throws  IOException{

    }
}
