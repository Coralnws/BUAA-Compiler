// All Decl's forms exist in global variable decl
const int AZ = 100;
const int AZA1 = 200, AZA2 = 300;

int az;
int aza = 100;
int azaz1, azaz2 = 200;

int getint(){
	int s;
	scanf("%d",&s);
	return s;
} 
// All Block's forms exist in while() in func1, except for (return exp)
void func1() {
    while (1) {
        int i = 0;
        i = 0;
        i * 8;
        { ; }
        if (i != 0) continue;
        if (i != 0); else;
        i = getint();
        printf("hello? ");
        printf("%d ", i);
        printf("%d %d\n", i, i);
        break;
    }
    if (1) {
	}
    return;
}

// All Ident's form and Other forms exist in func2
int func2() {
    int abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 = 43;
    int temp;
    temp = abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890;
    int a;
    int a1b3;
    /*
    This is annotation1
    */
    // This is annotation2
    int d = 0;
    0;
    1234567890;
    return 100;
}

// All Cond's forms exist in func3
void func3(int in) {
    printf("func3 input = %d\n", in);
    int a = 7;
    int b = 8;
    if (!(a + b));
    if ((a + b) < (a + b));
    if ((a + b) > (a + b));
    if ((a + b) <= (a + b));
    if ((a + b) >= (a + b));
    if ((a + b) == (a + b));
    if ((a + b) != (a + b));
    return;
}

// All Exp's forms exist in func4 
int func4(int param1, int param2, int param3) {
    int temp1 = (param1 * param2) / (param2) % (param1) + param3 - param3 * (param1 / param3 / param3);
    printf("temp1=%d\n",temp1); 
    const int temp2 = (AZ + AZA1 - AZA2) * AZ * (18 / 6) - 16 * (0);
    printf("temp2=%d\n",temp2);
    
    int temp3;
    temp3 = AZ * AZA1 % AZA2;
    printf("temp3=%d\n",temp3);
    int a = temp3 + 4;
    int b = (a);
    int c = func2();
    printf("c=%d\n",c);
    int d = - + - +func2();
    printf("d=%d\n",d);
    int e = c * b;
    int f = e / b;
    int g = f % c;
    int h = f + g;
    int i = f - g;
    printf("%d %d %d %d %d %d %d %d %d\n", a, b, c, d, e, f, g, h, i);
    return 1;
}

int main() {
    printf("19373682\n");
    func1();
    printf("func1 done!\n");
    func2();
    printf("func2 done!\n");
    func3(az);
    printf("func3 done!\n");
    func4(1, 2, 3);
    printf("func4 done!\n");
    return 0;
}

