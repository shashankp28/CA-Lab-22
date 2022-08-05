import java.util.Scanner;
import java.util.Random;

public class Playground

{

  static bool[][] grid =  {{false, false, false},
  {false, false, false},
  {false, false, false}};

    public Playground(int p, int w) 
    {
        this.p = p;
    }

    void get_env(int position)
    {
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                grid[i][j] = Math.Random() < p;
            }
        }

        if (position == w - 1)
        {
            grid[0][0] = false;
            grid[0][1] = false;
            grid[0][2] = false;
        }
    }

    public void main(String[] args) {

       get_env();
    }
}
