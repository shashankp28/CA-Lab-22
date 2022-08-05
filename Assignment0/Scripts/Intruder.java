import java.util.Scanner;

public class Intruder
{

    public Intruder(int w) 
    {
        this.w = w;
    }

    static int position = 0;

    void move_forward()
    {
        position = position + 1;
        System.out.println(position);
    }

    void goal_test()
    {
        position = position + 1;
        System.out.println(position);
    }


    public static void main(String[] args) 
    {
        move_forward();
    }
}
