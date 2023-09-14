import deadlock.DeadLock;
import thread.CSThread;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
        DeadLock deadLock = new DeadLock();
        deadLock.start();
    }

}
