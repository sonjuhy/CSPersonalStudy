import deadlock.DeadLock;
import injection.Injection;
import thread.CSThread;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
//        DeadLock deadLock = new DeadLock();
//        deadLock.start();

        Injection injection = new Injection();
        injection.start();

    }

}
