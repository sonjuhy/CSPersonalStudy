package clone.webFramework.servlet;

import clone.webFramework.annotation.MappingGet;
import clone.webFramework.annotation.MappingPost;
import clone.webFramework.annotation.PathVariable;
import clone.webFramework.annotation.RestController;
import clone.webFramework.init.Init.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jdk.jfr.ContentType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServletContainer {
    private static int threadLimit = 0;
    private static int runningThread = 0;
    private static boolean[] activatedThread;
    private static Queue<HttpExchange> requestQueue = new LinkedList<>();
    private static Thread[] threadPool;

    public static Bean bean;

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
    public void setBean(Bean bean){
        this.bean = bean;
    }

    public static class ServletHandler implements HttpHandler {

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
            SamplePage samplePage = new SamplePage();
            InvokeMethodInstance method = findMethod(exchange);
            System.out.println("Servlet Handler method : " + method.getMethod());

            if(method.getMethod() == null || method.getClazz() == null){
                try {
                    Class<?> sampleClass = Class.forName(samplePage.getClass().getName());
                    method.setClazz(sampleClass);
                    if(exchange.getRequestURI().toString().equals("/")){
                        Method welcomeMethod = sampleClass.getDeclaredMethod("welcomeMethod");
                        method.setMethod(welcomeMethod);
                    }
                    else {
                        System.out.println("Servlet Handler Error : NotFound method");
                        Method errorMethod = sampleClass.getDeclaredMethod("errorMethod");
                        method.setMethod(errorMethod);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Servlet Handler method name : " + method.getMethod().getName());
            }
            threadPool[point] = new Thread(new TaskThread(point, requestQueue.poll(), method));

            try {
                System.out.println("start Thread");
                threadPool[point].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private InvokeMethodInstance findMethod(HttpExchange exchange){
            System.out.println("findServlet finding start");
            Method returnMethod = null;
            Headers headers = exchange.getRequestHeaders();
            String ContentType = "application/json";


            int mode = -1;
            if(headers.get("Content-type") != null) ContentType = headers.get("Content-type").get(0);
            else if(headers.get("Content-Type") != null) ContentType = headers.get("Content-Type").get(0);
//            else{
//                for(String key : headers.keySet()){ // search Content-type
//                    List<String> value = headers.get(key);
//                    System.out.println(key+" : " + headers.get(key));
//                    if(value.contains("text/html")){
//                        mode = 0;
//                    }
//
//                }
//            }
            System.out.println("findServlet str : " + ContentType);

            Class<?> controllerClass = null;
            String restPath = "";

            switch (ContentType){
                case "text/plain":
                    // return string type
                    mode = 0;
                    break;
                case "text/html":
                    // return html type
                    mode = 1;
                    break;
                case "application/json":
                    // return type : json
                    mode = 2;
                    for(Class<?> clazz : bean.getRestControllerClassList()){
                        System.out.println("Class list : " + clazz);
                        if(clazz.isAnnotationPresent(RestController.class)){
                            RestController restControllerAnnotation = clazz.getAnnotation(RestController.class);
                            if(exchange.getRequestURI().toString().contains(restControllerAnnotation.value())){
                                controllerClass = clazz;
                                restPath = restControllerAnnotation.value();
                            }
                        }
                    }
                    break;
                default:
                    // return 404 page
                    break;
            }
            System.out.println("Controller name : " + controllerClass);
            Object[] args = null;
            if(controllerClass != null) {
                String originPath = exchange.getRequestURI().toString().split(restPath)[1];
                String path = originPath.split("/")[1];
                System.out.println("findServlet path : " + path+", origin path : " + originPath);
                Method tmpMethod = null;
                switch (mode) {
                    case 0: // text/plane
                        break;
                    case 1: // text/html
                        break;
                    case 2: // application/json
                        String restType = "GET";
                        if (exchange.getRequestMethod() != null) restType = exchange.getRequestMethod();
                        switch (restType) {
                            case "GET":
                                // doGET
                                for (Method method : controllerClass.getDeclaredMethods()) {
                                    if (method.isAnnotationPresent(MappingGet.class)) {
                                        System.out.println("findMethod method : " + method.getName());

                                        String AnnotationValue = method.getAnnotation(MappingGet.class).value();
                                        if(AnnotationValue.split("/")[1].equals(path)){
                                            tmpMethod = method;

                                            if(method.getParameterCount() > 0){ // if param exist
                                                int paramAnnotationCount = 0;
                                                for(Parameter param : method.getParameters()){ // check PathVariable param
                                                    if(param.isAnnotationPresent(PathVariable.class)) paramAnnotationCount++;
                                                }

                                                String[] pathVariable = Arrays.copyOfRange(originPath.split("/"),2, originPath.split("/").length);
                                                String[] annotationArr = Arrays.copyOfRange(AnnotationValue.split("/"),2, AnnotationValue.split("/").length);
                                                HashMap<String, Integer> annotationMap = new HashMap<>();

                                                for(int i=0;i<annotationArr.length;i++){ // remove {, } in annotation
                                                    annotationArr[i] = annotationArr[i].replaceAll("\\{","");
                                                    annotationArr[i] = annotationArr[i].replaceAll("\\}","");
                                                    System.out.println(i+" : " + annotationArr[i]);
                                                    annotationMap.put(annotationArr[i], i);
                                                }

                                                System.out.println("pathVariable size : " + pathVariable.length+", parameterCount : " +paramAnnotationCount+", annotationMap : " + annotationMap.size());

                                                if(pathVariable.length == paramAnnotationCount && paramAnnotationCount == annotationArr.length && paramAnnotationCount == method.getParameterCount()){
                                                    Parameter[] params = method.getParameters();
                                                    args = new Object[paramAnnotationCount];

                                                    for(int i=0;i<paramAnnotationCount;i++){
                                                        System.out.println("i : " + i + ", place : " + params[i].getAnnotation(PathVariable.class).value());
                                                        int place = annotationMap.get(params[i].getAnnotation(PathVariable.class).value());
                                                        String paramsType = params[place].getType().toString();

                                                        if("int".equals(paramsType)){
                                                            args[place] = Integer.parseInt(pathVariable[i]);
                                                        }
                                                        else if("float".equals(paramsType)){
                                                            args[place] = Float.parseFloat(pathVariable[i]);
                                                        }
                                                        else if("double".equals(paramsType)){
                                                            args[place] = Double.parseDouble(pathVariable[i]);
                                                        }
                                                        else if("long".equals(paramsType)){
                                                            args[place] = Long.parseLong(pathVariable[i]);
                                                        }
                                                        else if(params[place].getType() == String.class){
                                                            args[place] = pathVariable[i];
                                                        }
                                                        else{
                                                            System.out.println("not support this type");
                                                        }
                                                    }

                                                }
                                                else{
                                                    System.out.println("Parameter Annotation Count and URL parameter Count is wrong");
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                if (tmpMethod != null) returnMethod = tmpMethod;
                                break;
                            case "POST":
                                // doPOST
                                for (Method method : controllerClass.getDeclaredMethods()) {
                                    if (method.isAnnotationPresent(MappingPost.class)) {
                                        String AnnotationValue = method.getAnnotation(MappingPost.class).value();
                                        if (path.length() == AnnotationValue.length()) {
                                            if (path.equals(AnnotationValue)) {
                                                tmpMethod = method;
                                                break;
                                            }
                                        } else if (path.length() > AnnotationValue.length()) {
                                            if (path.contains(AnnotationValue)) tmpMethod = method;
                                        } else {
                                            if (AnnotationValue.contains(path)) tmpMethod = method;
                                        }
                                    }
                                }
                                if (tmpMethod != null) returnMethod = tmpMethod;
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
                        break;
                    default:
                        break;
                }
            }
            return new InvokeMethodInstance(returnMethod, controllerClass, exchange.getRequestURI().toString().split(restPath)[1], exchange.getRequestBody(), args);
        }
    }
    protected static class TaskThread implements Runnable{
        private int point;
        private HttpExchange exchange;
        private InvokeMethodInstance invokeMethodInstance;


        public TaskThread(){}
        public TaskThread(int point, HttpExchange exchange, InvokeMethodInstance invokeMethodInstance){
            this.point = point;
            this.exchange = exchange;
            this.invokeMethodInstance = invokeMethodInstance;
        }
        @Override
        public void run() {
            OutputStream responseBody = exchange.getResponseBody();

            try{
                Object result = invokeMethodInstance.getMethod().invoke(invokeMethodInstance.getClazz().getDeclaredConstructor().newInstance(), invokeMethodInstance.getArgs());
                StringBuilder sb = new StringBuilder();
                sb.append(result);
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
            } catch (InvocationTargetException e) { // invoke exception
                e.printStackTrace();
            } catch (IllegalAccessException e) { // invoke wrong args
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } finally {
                System.out.println("exchange closed\n");
                exchange.close();
                runningThread--;
                threadPool[point] = null;
                activatedThread[point] = false;
            }
        }
    }
}
