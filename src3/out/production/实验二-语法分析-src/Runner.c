// B

void read(int a[][3], int n) {
    int i = 0, j = 0;
    while (i < n) {
        j = 0;
        while (j < 3) {
            a[i][j] = getint();
            j = j + 1;
        }
        i = i + 1;
    }
    return ;
}

int getint(){
	int s;
	scanf("%d",&s);
	return s;
} 
int main() {
    int a[5] = {1, 2, 3, 4, 5};
    int c[2][1000-997 + 123 / 145] = {{1, 2, 3}, {6 - 3 * 5 % 7 - 3 / 2, 4, 5}};
    c[1][100-99] = c[1][2] + 3;

    printf("20373318\n");

    int i = 0;
    while (i < 5) {
        printf("a[%d] = %d, ", i, a[i]);
        i = i + 1;
    }
    printf("\n");

    i = 0;
    int j = i + 100, flag = 0;
    /*
    while (i < 7) {
        j = 0;
        while (j < 8) {
            if (b[i][j] == 1) {
                flag = 1;
                break;
            }
            j = j + 1;
        }
        if (flag == 1) break;
        i = i + 1;
    }
    printf("i = %d, j = %d, flag = %d\n", i, j, flag);
    */

    ;

    i = 0;
    j = 123456789;
    while (i < 2) {
        j = 0;
        while (j < 3) {
            printf("c[%d][%d] = %d, ", i, j, c[i][j]);
            j = j + 1;
        }
        printf("\n");
        i = i + 1;
    }

    int a1[3][3];
    read(a1, 3);
    i = 0;
    int sum1 = 0, sum2 = 0;;
    while (i < 3) {
        j = 0;
        while (j < 3) {
            int sum2 = 0;
            sum1 = sum1 + a1[j+i-j][j];
            sum2 = sum1;
            printf("sum1 = %d, sum2 = %d\n", sum1, sum2);
            j = j + 1;
        }
        i = i + 1;
    }
    printf("sum1 = %d, sum2 = %d\n", sum1, sum2);

    int asd[6];
    asd[4] = a1[2][1] + c[0][0];
    printf("asd[4] = %d\n", asd[4]);

    return 0;
}



