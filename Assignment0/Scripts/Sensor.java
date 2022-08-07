import java.util.Random;

public class Sensor {
    
    boolean state = false;

    public Sensor(double p) {
        this.state = Math.random() < p;
    }

    public boolean get_state() {
        return this.state;
    }
}