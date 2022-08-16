#include <iostream>
using namespace std;

int main()
{
    int num = 1241;
    int *digits = new int;
    int num_digits = 0;
    int i = 0;
    while(num>0)
    {
        int temp;
        temp = num%10;
        digits[i] = temp;
        i++;
        num = num/10;
    }
   // cout<<digits[1];
    num_digits = i;
    int front = 0;
    int back = i-1;
    int count = 0;
  //  cout<<num_digits;
    for(int k=0; k<num_digits; k++)
    {
        cout<<" "<<digits[k]<<" ";

    }

    while(digits[front]==digits[back] && front<=back)
    {
        front++;
        back--;
        count++;
    }

    if(count == num_digits/2 + 1)
    {
        cout<<"Yes"; //flag = 1
    }
	
	return 0;
}