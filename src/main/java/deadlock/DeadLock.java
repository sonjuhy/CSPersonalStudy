package deadlock;

public class DeadLock {
    static int flag = 2; // 1 : DeadLock, 2 : none DeadLock
    static boolean table = false;
    public void start(){
        CustomThread thread1 = new CustomThread(1);
        CustomThread thread2 = new CustomThread(2);
        try{
            thread1.start();
            Thread.sleep(1000);
            thread2.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    static class CustomThread extends Thread{
        private int flagNum;
        public CustomThread(int flag){
            this.flagNum = flag;
        }
        public void start(){
            Thread thread = new Thread(
                    () -> {
                        while(true){
                            if(table == false){
                                table = true;
                                try{
                                    Thread.sleep(1000);
                                    if(flagNum != flag){
                                        flag = this.flagNum;
                                        table = false;
                                        System.out.println("id : " +flagNum+" is Success");
                                        break;
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                            else{
                                System.out.println("id : "+flagNum+" is waiting... table : "+table + ", flag : " + flag);
                                try{
                                    Thread.sleep(1000);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            );
            thread.start();
        }
    }
}
