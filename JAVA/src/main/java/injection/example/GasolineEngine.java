package injection.example;

public class GasolineEngine implements Engine {
    @Override
    public void start() {
        System.out.println("burning gasoline");
    }

    @Override
    public void running() {
        System.out.println("Running smooth");
    }

    @Override
    public void on() {
        System.out.println("Gasoline Engine is activated");
    }

    @Override
    public void end() {
        System.out.println("Gasoline Engine is unActivated");
    }
}
