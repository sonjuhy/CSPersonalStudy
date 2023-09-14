package injection;

public class Injection {
    private Service service;
    public void start(){
        service = new Service(new MessageService());
        service.calc(1,2);
    }
    static class Service{
        private IService iService;
        public Service(IService iService){
            this.iService = iService;
        }
        public void calc(int a, int b){
            System.out.println("calc result : " + iService.add(a,b));
        }
    }
    interface IService{
        int add(int a, int b);
    }

    static class MessageService implements IService{
        public int add(int a, int b) {
            return a+b;
        }
    }
}
