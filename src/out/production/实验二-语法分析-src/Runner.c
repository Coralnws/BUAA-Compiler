int a,b,c,d,e,i=0;

int func0(int a,int b) {
    int i = 0;
    int c = 128;
    return (a+b)%c;
}

int func1(int a) {
    i = i + 1;
    return i;
}

int func2(int a,int b) {
printf("a=%d , b=%d\n",a,b);
    if (a % b == 0) {
        printf("return 1\n");
        return 1;
    }
    return 0;
}

int func3() {
    printf("glo_i = %d\n",i);
    int tt0,tt1,t2,tt3,tt4,v=1906;
    while (i < 10) {
        int v = a * 4 * 32 * a / a / 32;
        b = func0(b,v);
        tt0 = a*4 + b + c ;
        tt1 = a*4 + b + c + d;
        t2 = a*4 + b + c + d + e;
        tt3 = a*4 + b + c + d + e;
        tt4 = a*4 + b + c + d + e;
        if (func2(i,10)) {
            printf("sum = %d\n", tt0 + tt1 + t2 + tt3 + tt4);
        }
        func1(i);
        printf("i=%d\n",i);
    }
    return tt0 + tt1 + t2 + tt3 + tt4;
}

int getint(){
	int s;
	scanf("%d",&s);
	return s;
} 
int main() {
    int i = 0;
    a = getint();
    b = getint();
    c = getint();
    d = getint();
    e = getint();
    i = getint();
    printf("main_i = %d\n",i);
    printf("a=%d , b=%d\n",a,b);
    printf("%d\n",func3());
    return 0;
}
