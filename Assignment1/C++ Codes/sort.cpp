#include <iostream>
#include <array>
using namespace std;

int main()
{
	int a[10] = {12, 5, 31, 42, 36, 78, 8, 9, 10, 21};
    int len = sizeof(a) / sizeof(a[0]);
    for(int i = len - 1; i>=0; i-- )
    {
        for(int j = 0; j<i-1; j++)
        {
            if(a[j] < a[j+1])
            {
                int temp;
                temp = a[j];
                a[j] = a[j+1];
                a[j+1] = temp;
            }
        }
    }
    cout<<a[0]<<" "<<a[1];
	return 0;
}