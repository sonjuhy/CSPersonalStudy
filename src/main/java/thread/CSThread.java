package thread;

public class CSThread {
    public void start(){
        CustomThread thread1 = new CustomThread("thread1");
        CustomThread thread2 = new CustomThread("thread2");
        thread1.start();
        thread2.start();
    }

    public void case1(){
        System.out.println("Start!");
        Thread thread1 = new Thread(
                () -> calc(1)
        );
        Thread thread2 = new Thread(
                () -> calc2(2)
        );
        thread1.start();
        thread2.start();
    }
    public static void calc(int id){
        int a = 10;
        a += 5;
        System.out.println(id+", "+a);
    }
    public static void calc2(int id){
        int b = 10;
        b -= 5;
        System.out.println(id+", "+b);
    }

    static class CustomThread extends Thread{
        String name;
        public CustomThread(String name){
            this.name = name;
        }

        @Override
        public void run(){
            for(int i=0;i<5;i++) {
                try {
                    Thread.sleep(1000);
//                    System.out.println(this.name+" : static string : " + Main.str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
