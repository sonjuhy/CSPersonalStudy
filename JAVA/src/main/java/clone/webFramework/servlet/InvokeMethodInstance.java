package clone.webFramework.servlet;

import java.io.InputStream;
import java.lang.reflect.Method;

public class InvokeMethodInstance {
    private Method method;
    private Class<?> clazz;

    private String path;
    private InputStream requestBody;
    private Object[] args;

    public InvokeMethodInstance(Method method, Class<?> clazz, String path, InputStream requestBody, Object[] args){
        this.method = method;
        this.clazz = clazz;
        this.path = path;
        this.requestBody = requestBody;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public InputStream getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(InputStream requestBody) {
        this.requestBody = requestBody;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
