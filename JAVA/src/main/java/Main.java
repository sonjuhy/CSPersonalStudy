import clone.webFramework.MainApplication;
import clone.webFramework.init.ComponentScan;

public class Main {
    public static void main(String[] args){
        System.out.println("Start!");
//        ComponentScan componentScan = new ComponentScan();
//        componentScan.scan();
        MainApplication mainApplication = new MainApplication();
        mainApplication.init();
    }
}
