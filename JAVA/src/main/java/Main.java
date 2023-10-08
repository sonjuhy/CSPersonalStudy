import clone.webFramework.MainApplication;
import clone.webFramework.init.ComponentScan;
import injection.example.Car;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
//        MainApplication mainApplication = new MainApplication();
//        mainApplication.init();
        Car car = new Car();
        car.turnOnEngine();
    }
}
