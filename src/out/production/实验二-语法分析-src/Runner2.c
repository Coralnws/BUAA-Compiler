int print0(){
	printf("print0\n");
	return 0;
}
int print1(){
	printf("print1\n");
	return 1;
}
int main(){
	if(1 && 0 || 1 && print0()){
		printf("break\n");
	}
	if(0 && 0 && !-36<7+1 || +36>8-1){
		printf("break\n");
	}

}


