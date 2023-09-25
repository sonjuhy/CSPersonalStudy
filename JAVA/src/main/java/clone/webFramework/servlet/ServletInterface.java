package clone.webFramework.servlet;

import java.io.IOException;

public interface ServletInterface {
    public void init();
    public void service(Request request, Response response) throws IOException;
    public void destroy();
}
