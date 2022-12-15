int print0(){
	printf("print0!\n");
	return 0;
}
int print1(){
	printf("print1!\n");
	return 1;
}
int main(){
	if(1 && print0() && print1()){  //print 
		printf("here!\n");
	}
} 
