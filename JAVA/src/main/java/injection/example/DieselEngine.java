package injection.example;

public class DieselEngine implements Engine{
    @Override
    public void start() {
         System.out.println("burning diesel");
    }

    @Override
    public void running() {
        System.out.println("Running powerful");
    }

    @Override
    public void on() {
        System.out.println("Diesel Engine is activated");
    }

    @Override
    public void end() {
        System.out.println("Diesel Engine is unActivated");
    }
}
