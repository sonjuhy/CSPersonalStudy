package injection.example;

public class Car {
    private Engine engine;
    public Car(){
        engine = new GasolineEngine();
    }
    public void turnOnEngine(){
        engine.start();
    }
    public void turnOffEngine(){
        engine.end();
    }
    public void go(){
        engine.running();
    }
    public void stop(){
        engine.on();
    }
}
