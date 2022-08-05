public class Main {
    
    static boolean can_move_forward(boolean[][] playground)
    {
        return !(playground[0][0] && playground[0][1] && playground[0][2]) && !(playground[1][1])
    }

    public static void main(String args[])
    {
        int w = Integer.parseInt(args[0]);
        double p = Double.parseDouble(args[1]);
        int time = 0;

        Intruder intruder = new Intruder(w);
        Playground playground = new Playground(p);

        while(!intruder.goal_test())
        {
            boolean[][] current_sensor_config = playground.get_env(intruder.poisition);
            if can_move_forward(current_sensor_config)
            {
                intruder.move_forward();
            }
            time += 10
        }
        return time
    }
}