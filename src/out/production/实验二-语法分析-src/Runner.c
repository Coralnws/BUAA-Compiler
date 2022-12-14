
int array[2][2] = {{1, 2}, {2, 4}};

void testfunc(int arr[]){
    printf("test func:%d\n",arr[0]);
}
void printarrrr(int arr[][1+1])
{
    printf("first: %d", arr[0][0]);
    return;
}

void printarr(int arr[], int arr2[])
{
    printf("first : %d", arr[0]);
    testfunc(arr);
}

int main()
{
    printarr(array[1], array[0]);
    printarrrr(array);
    return 0;
}
