1.当前是 &&

```
    if(0 || 1 && print()){  //print 
            printf("here!\n");
    }

	if(0 || 0 && print()){  //empty
		printf("here!\n");
	}
	
	if(0 && 1 && print()){  //empty
		printf("here!\n");
	}
	
	if(0 && print() && print()){  //empty 
		printf("here!\n");
	}
	
	if(1 && print0() && print1()){  //print0 
		printf("here!\n");
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////
	if(1 || 0 && print()){  //here 
		printf("here!\n");
	}
	
	if(1 || print() && print()){  //here
		printf("here!\n");
	}
	
	if(0 || 1 || print()){  //here  
		printf("here!\n");
	}
	
	if(0 || 0 || print()){  //print + here 
		printf("here!\n");
	}
	

```

当前是 && ，如果前面是0就short （不是result list有，是当前前面那个）

 当前是 || ，如果前面是1就short