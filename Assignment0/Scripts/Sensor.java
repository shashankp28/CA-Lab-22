import java.util.Random;

public class Sensor {
    
    boolean state = false;

    // Constructor of a sensor, initially set to OFF
    public Sensor(double p) {
        this.state = false;
    }

    // Set a random state based on probability p.
    public void set_random_state(double p) {
        this.state = Math.random() < p;
    }

    // Get the current state of the sensor
    public boolean get_state() {
        return this.state;
    }
}