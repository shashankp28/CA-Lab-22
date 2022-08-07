import java.util.Scanner;

public class Border

{

// Global variables declaration
    static Sensor[][] sensors =  new Sensor[3][3];

    static double p = 0;
    static int w = 0;

// Constructor
    public Border(double p, int w) 
    {
        this.p = p;
        this.w = w;
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                sensors[i][j] = new Sensor(p);
            }
        }
    }

// Method to get the configuration of sensors around intruder
    static Sensor[][] get_env(int position)
    {
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                sensors[i][j].set_random_state(p);  // Coin Toss
            }
        }

        if (position == w - 1)
        {
            sensors[0][0].state = false;
            sensors[0][1].state = false;
            sensors[0][2].state = false;
        }
        if (position == -1)
        {
            sensors[1][1].state = false;
        }

        return sensors;
    }

    public static void main(String[] args) {

// Test Code
    //    boolean[][] ans = get_env(5);
    //    System.out.println(ans[0][0]);
    }
}
