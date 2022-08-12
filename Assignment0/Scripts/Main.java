import java.util.Arrays;

public class Main {
    
    // Check whether the intruder can move forward
    static boolean can_move_forward(Sensor[][] border)
    {
        return !(border[0][0].state && border[0][1].state && border[0][2].state) && !(border[1][1].state);
    }

    public static void main(String args[])
    {
        // Creating objects of different classes
        int w = Integer.parseInt(args[0]);
        double p = Double.parseDouble(args[1]);
        Clock clock = new Clock();

        Intruder intruder = new Intruder(w);
        Border border = new Border(p, w);

        // Interate until the intruder reaches the defending country
        while(!intruder.goal_test())
        {
            Sensor[][] current_sensor_config = border.get_env(intruder.position);
            if(can_move_forward(current_sensor_config))
            {
                intruder.move_forward();
            }
            clock.increment(10);
        }
        System.out.println(clock.time);
    }
}