import thread.CSThread;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
        CSThread csThread = new CSThread();
        csThread.start();
    }

}
