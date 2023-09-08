import thread.CSThread;

public class Main {
    static String str = "hello?";
    public static void main(String[] args){
        System.out.println("Start!");
    }
    static void threadRegisterAndStack(){
        CSThread csThread = new CSThread();
        csThread.case1();
    }
    static void threadData(){
        Thread thread = new Thread(
                () -> {
                    try {
                        Thread.sleep(3000);
                        str="new hello?";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        thread.start();
        CSThread csThread = new CSThread();
        csThread.start();
    }
}
