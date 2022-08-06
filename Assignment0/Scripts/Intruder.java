import java.util.Scanner;

public class Intruder
{
    int w = 0;

// Constructor
    public Intruder(int w) 
    {
        this.w = w;
    }

    static int position = 0;

// Move forward
    void move_forward()
    {
        position = position + 1;
        System.out.println(position);
    }

// Goal Test Function
    boolean goal_test(int w)
    {
        if (position > w)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public static void main(String[] args) 
    {
        // Test Code
        // Intruder i = new Intruder(5);
        // boolean ans = i.goal_test(5);
        // System.out.println(ans);

    }
}
