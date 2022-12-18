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



# 短路

&& 优先级 > ||

做的时候先要看完整个cond，如果没有混合的话就照常坐，有混合的话要另外做

1.pcode的时候记录起来

2.execute的时候记录完了再跳回去

### 混合：

混合的时候，先做&&部分，看作是或运算





新做法：