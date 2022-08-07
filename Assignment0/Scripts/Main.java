import java.util.Arrays;

public class Main {
    
    static boolean can_move_forward(boolean[][] border)
    {
        return !(border[0][0] && border[0][1] && border[0][2]) && !(border[1][1]);
    }

    public static void main(String args[])
    {
        int w = Integer.parseInt(args[0]);
        double p = Double.parseDouble(args[1]);
        Clock clock = new Clock();

        Intruder intruder = new Intruder(w);
        Border border = new Border(p, w);

        while(!intruder.goal_test())
        {
            boolean[][] current_sensor_config = border.get_env(intruder.position);
            if(can_move_forward(current_sensor_config))
            {
                intruder.move_forward();
            }
            clock.increment(10);
        }
        System.out.println(clock.time);
    }
}