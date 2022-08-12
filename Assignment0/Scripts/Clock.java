public class Clock {

    // Declaring time attribute, starting from 0
    static int time = 0;

    // Method to increment time
    static public void increment(int delta) {
        time += delta;
    }

}