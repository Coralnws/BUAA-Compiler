int s1_1[3][5]={{25*4,200,300,400,500},{111,222,333,444,555},{99,102,0,123,145}};
int s2_1[3][5]={{100,200,300,400,500},{111,222,333,444,555},{99,102,0,123,145}};
int add[3]={123,666,456},s_2[3]={0,0,0};
int s_3[3]={0,0,0};
const int a1=1,a2=2,a3=3;
const int month[9]={1,2,3,4,5,6,7,8,9};
const int year_1=4,year_2=100;
int checkyear(int year){
    if(year>=0){
    if(year!=+2022||year<=2021){
        if((year%(-year_1*-year_2))==0||(year%year_1)==0&&(year%year_2)!=0){
            printf("run:%d\n",year);
        }
        else{
            printf("not run:%d\n",year);
        }
    }
    else{
        printf("2022!!!\n");
    }
    }
    return year;
}

int getint(){
    int n;
    scanf("%d",&n);
    return n;
}
int main(){

{
	    int aaa,bbb,ccc,ddd,eee,fff;
	int y_1,z_1;
    z_1=getint();

    fff=checkyear(z_1);

}

return 0;
}
