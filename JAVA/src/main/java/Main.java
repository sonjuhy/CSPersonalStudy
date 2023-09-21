import deadlock.DeadLock;
import http.Servlet;
import http.WebServer;
import injection.Injection;
import sql.SQL;
import thread.CSThread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
//        Method[] methods = Servlet.class.getDeclaredMethods();
//        for(Method method : methods){
//            System.out.println(method.getAnnotations());
////            if(method.getName().equals("mainPage")){
////                try {
////                    System.out.println(method.invoke(null));
////                } catch (IllegalAccessException e) {
////                    e.printStackTrace();
////                } catch (InvocationTargetException e) {
////                    e.printStackTrace();
////                }
////            }
//        }
        try{
            Servlet servlet = new Servlet();
            servlet.start();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
