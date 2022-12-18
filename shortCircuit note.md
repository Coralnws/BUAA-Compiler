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

遇到if 或者 while之后，开始记录cond1 的 pointer位置，遇到cond1就开始记录cond2，以此类推

在遇到checkcond之后才正式地用condList里面的坐标去找cond

在正式做之前先run一轮checkcond，然后记录起来是不是混合，如果是混合的话就要先做&&，否则照常做，然后看shortcircuit



shortcircuit是针对每个&& ||，不是针对整个cond

所以做的时候差不多一样，只是如果是shortcircuit的话，下一个cond不用做

一轮要做两个cond才比较容易控制







做法：

两个记录一开始记录好，两个边做边记录

（1）一开始记录好，一个opStack记录&&|| ， 一个condQueue记录cond1/cond2那些

（2）边做边记录：一个opList，一个condList

（3）一个变量记录上一个读到的op,prevOp

过程：

如果是Mix：

（1）检查当前应该读到的isCond（应该和queue一样），checkCondList有没有东西，如果condList不是空的，做了当前cond之后，用prevOp和两个cond结果做出答案存在condList然后pop opStack pop queue  /  如果condList是空的就做了当前的直接存

（2）读到cond1后面 && 就去看和opStack.peek()一不一样，一样的话，check condList里面的结果，看会不会有shortCircuit，会的话直接pop opStack，pop condQueue ，然后readPcode直到跳过这个cond（例如要跳过cond2那就读到#cond2为止）   ;    不会shortCircuit的话就自然地nextRound继续

，并且判断是不是shortCircuit，是的话opStack pop



检查condList，有的话和opStack.peek 一起看有没有shortcircuit，有的话 pop opStack和 queue 并且readPcode跳过，没有的话做当前pcode的

检查condList，没有的话做了直接存，然后看和当前pcode有没有shortcircuit，有的话pop了之后skip过 



（1）检查当前opStack和queue对应的对不对（应该是对的）

（2）检查condList有没有

- 有的话和opStack.peek()有没有短路
  - 有短路就检查当前peek之外的后面op还有没有mix，
    - 有的话做下去
    - 没有的话就可以整个cond短路 
  - 没有短路的话，如果当前是 || ，包括当前在内的后面有mix，就把||和condList都pop一个出来记录在另一个地方做，然后继续当前pcode，拿opStack.peek()和新作的一个cond和condList pop一个出来做出结果放进condList
- 没有的话做当前pcode，放进condList，然后看有没有短路
  - 有短路就检查当前peek之外的后面op还有没有mix
    - 没有的话整个cond短路
    - 有的话仅跳过下一个cond，pop opStack
  - 没有短路就继续做下去



checkShortCircuit：



