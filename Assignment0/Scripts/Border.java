import java.util.Scanner;
import java.util.Random;

public class Border

{

// Global variables declaration
  static boolean[][] grid =  {{false, false, false},
  {false, false, false},
  {false, false, false}};

    static double p = 0;
    static int w = 0;

// Constructor
    public Border(double p, int w) 
    {
        this.p = p;
        this.w = w;
    }

// Method to get the configuration of sensors around intruder
    static boolean[][] get_env(int position)
    {
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                grid[i][j] = Math.random() < p;  // Coin Toss
            }
        }

        if (position == w - 1)
        {
            grid[0][0] = false;
            grid[0][1] = false;
            grid[0][2] = false;
        }
        if (position == -1)
        {
            grid[1][1] = false;
        }

        return grid;
    }

    public static void main(String[] args) {

// Test Code
    //    boolean[][] ans = get_env(5);
    //    System.out.println(ans[0][0]);
    }
}
