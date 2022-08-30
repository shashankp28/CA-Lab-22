#include <iostream>
using namespace std;

int check_prime(int n)
{
    if(n==1) return -1;
    if(n==2) return 1;
    int i = 2;
    while(i<n)
    {
        if(n%i==0) return -1;
        i++;
    }
    return 1;
}

int main()
{
	int n = 4;
    cout<<check_prime(n);
	return 0;
}