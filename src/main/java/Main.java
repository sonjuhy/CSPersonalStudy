import deadlock.DeadLock;
import injection.Injection;
import sql.SQL;
import thread.CSThread;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
//        DeadLock deadLock = new DeadLock();
//        deadLock.start();

//        Injection injection = new Injection();
//        injection.start();

        SQL sql = new SQL();
        sql.connect();
        sql.select();
    }

}
