package Executor;

import ParserAnalyse.Exp;
import Pcode.Symbol.*;
import Pcode.Writer;

import javax.sound.midi.SysexMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Executor.Executor.*;


public class Executor {
    static HashMap<Integer, HashMap<String, Symbol>> symbTable = new HashMap<Integer, HashMap<String, Symbol>>();
    static int pointer = 1;
    static int floor = 0;
    static Runner activeRunner;
    static Stack<Runner> runnerStack = new Stack<Runner>();
    static HashMap<String, Integer> publicTempList = new HashMap<String, Integer>(); //temp var
    static HashMap<Integer, Func> funcList = new HashMap<Integer, Func>(); //floor list
    static HashMap<String,Integer> floorList = new HashMap<String,Integer>();
    static ArrayList<String> pcodeList = new ArrayList<String>();
    static int blocknum=1;

    //static Stack<Integer> loopList = new Stack<Integer>(); //1 = if,2=while
    static String pcode; //读着的pcode
    static Func currentFunc;
    static BufferedWriter writer;
    int level = 0;
    boolean hasRet = false;
    static final Scanner scanner = new Scanner(System.in);

    int whilePointer = 0;
    boolean condResult = false;
    boolean skipping = false;
    boolean shortCircuit = false;


    static {
        try {
            writer = new BufferedWriter(new FileWriter("execute.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Executor() throws IOException {
        pcodeList = Writer.getPcodeList();
        writer = new BufferedWriter(new FileWriter("pcoderesult.txt"));
        HashMap<String, Symbol> newSymbList = new HashMap<String, Symbol>();
        symbTable.put(0,newSymbList);
        floorList.put("public",0);
    }

    public void execute() throws IOException {
        String[] sym;
        //从这边开始，好像lexer那样，一行行读
        while (hasPcode()) {
            readPcode();
            sym = pcode.split(" ");


            if(shortCircuit){
                while(!pcode.equals("CheckCond")){
                    readPcode();
                    sym = pcode.split(" ");
                }
                shortCircuit = false;
            }
            while(skipping){ //跳到下一个碰到的end
                if(sym[0].equals("end")){
                    System.out.println("currentIF:" + activeRunner.ifList.peek());
                    if(activeRunner.currentIfEnd(sym[1])){
                        System.out.println("check is end if:"+pcode);
                        activeRunner.ifList.pop();
                        skipping = false;
                        readPcode();
                        sym = pcode.split(" ");
                        System.out.println("Pcode: " + Arrays.toString(sym));
                        break;
                    }
                }
                readPcode();
                //pcode.substring(0,3).equals("str")
                sym = pcode.split(" ");
                //println("Pcode: " + Arrays.toString(sym));
            }
            System.out.println("Pcode: " + Arrays.toString(sym));
            //0-const ,1-normal)
            if(sym[0].equals("var")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                int value = 0;
                int index = 0;
                int arrDim = 0;

                if(sym[1].equals("a")){  //var a a [ 1 ] [ 3 ] = { { } , { } } ;
                    Arr arr = newArr(sym[2],1);
                    System.out.println("New Arr");
                    if((sym.length> 6) && sym[6].equals("[")){  //如果是二维
                        arr.dimensionNum=2;
                        arr.addDimension(senseValue(sym[4]));
                        System.out.println("Two Dimension :" + sym[7]);
                        arr.addDimension(senseValue(sym[7]));
                        arrDim = 2;
                        index = 10;
                    }
                    else{  //一维
                        arr.dimensionNum=1;
                        arr.addDimension(0);
                        arr.addDimension(senseValue(sym[4]));
                        arrDim = 1;
                        index = 7;
                    }
                    arr.setSpace();
                    int valueCount = 0;
                    index+=1; // 挑掉第一个{, 一维的话进数字，二维的话还是 {
                    if(index < sym.length){
                        if(arrDim == 1) {
                            for(;index<sym.length;index++){
                                if(isNumeric(sym[index]) || isVar(sym[index]) || isTemp(sym[index])){
                                    System.out.println("Add value to arr:" + sym[index]);
                                    arr.setValue(valueCount,senseValue(sym[index]));
                                    valueCount++;
                                }
                            }
                        }
                        if(arrDim == 2){
                            index += 1;
                            int dim1 = senseValue(sym[4]);  //2
                            int dim2 = senseValue(sym[7]);  //2
                            //System.out.println(dim1 + " " +dim2);
                            for(int i=0; i < dim1 && index<sym.length;){
                                //System.out.println("i="+ i);
                                for(int j=0;j<dim2 && index<sym.length;){
                                    if(sym[index].equals("{") && j!=0){
                                        //System.out.println("next dim");
                                        i++;
                                        index++;
                                        break;
                                    }else if(isNumeric(sym[index]) || isVar(sym[index]) || isTemp(sym[index])){
                                        System.out.println("index:"+ (i*dim2+j));
                                        System.out.println("Add value to arr:" + sym[index]);
                                        arr.setValue((i*dim2)+j,senseValue(sym[index]));
                                        j++;
                                        if(j==dim2){
                                            i++;
                                        }
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                }
                if(sym[1].equals("v")){//var v a1 = 6t
                    Variable var = newVar(sym[2],1);
                    if(sym.length > 3){ //define value
                        System.out.println("Has define value");
                        System.out.println("sym[4] :" +sym[4]);

                        value = senseValue(sym[4]);

                        var.addValue(value);
                    }
                }
            }
            else if(sym[0].equals("const")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                int value = 0;
                int index = 0;
                if(sym[1].equals("a")){  //const a a [ 4 ] [ 5 ]  =  ....
                    Arr arr = newArr(sym[2],0);

                    if((sym.length> 6) && sym[6].equals("[")){  //如果是二维
                        arr.dimensionNum = 2;
                        arr.addDimension(senseValue(sym[4]));
                        System.out.println("Two Dimension :" + sym[7]);
                        arr.addDimension(senseValue(sym[7]));
                        index = 9;
                    }
                    else{  //一维
                        arr.dimensionNum = 1;
                        arr.addDimension(0);
                        arr.addDimension(senseValue(sym[4]));
                        index = 6;
                    }
                    for(;index<sym.length;index++){
                        System.out.println("Add value to arr:" + sym[index]);
                        if(isNumeric(sym[index]) || isVar(sym[index]) || isTemp(sym[index])){
                            System.out.println("Add value to arr:" + sym[index]);
                            arr.addValue(senseValue(sym[index]));
                        }
                    }
                }
                if(sym[1].equals("v")){//0-const , 1-type(0-const ,1-normal) 2-name ,3-'=' , 4: var/num
                    Variable var = newVar(sym[2],0);
                    System.out.println("sym[4] :" +sym[4]);
                    value =senseValue(sym[4]);
                    var.addValue(value);
                }

            }
            else if(sym[0].equals("upd")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                String name = sym[1];
                int value = 0;

                if(sym[2].equals("[")){ //0:upd 1:arrName [ 2 ] [ 3 ] =
                    System.out.println("Upd arr");
                    int dimension1 = 0;
                    int dimension2 = 0;
                    int index = 0;
                    if(( sym.length>5 ) && sym[2].equals("[") && sym[5].equals("[")){
                        dimension1 = senseValue(sym[3]);
                        dimension2 = senseValue(sym[6]);
                        index = 9;
                    }
                    else if(sym[2].equals("[")){
                        System.out.println("Dimension1:" + sym[3]);
                        dimension1 = 0;
                        dimension2 = senseValue(sym[3]);
                        index = 6;
                    }

                    if(sym[index].equals("getint")){//upd a = getint ( )
                        value = getint();
                    }else{
                        value = senseValue(sym[index]);
                    }

                    System.out.println("Upd : " + value);
                    updateArr(sym[1],dimension1,dimension2,value);
                }
                else if(isVar(sym[1])){  //0-upd 1: varName 2: = 3:exp

                    if(sym[3].equals("getint")){//upd a = getint ( )
                        value = getint();
                    }else{
                        value = senseValue(sym[3]);
                    }
                    System.out.println("Upd : " + value);
                    updateVar(sym[1],value);
                }
            }
            else if(sym[0].equals("para")){
                //para a  a [ ] [ 2 ]
                //para v b
                Para para = newPara(sym);
            }
            else if(sym[0].equals("func")){  //func int main
                int type=0;
                if(sym[1].equals("int")){
                    type=1;
                }
                else if(sym[1].equals("void")){
                    type=0;
                }
                Func func = newFloor(sym[2],type);
                System.out.println("In func, floor = " + floor);
                System.out.println("Current func:" + currentFunc.name);
            }
            else if(sym[0].equals("push")){ //push <name>
                if(activeRunner == null && floor != 0){
                    continue;
                }
                pushPara(sym[1]);
            }
            else if(sym[0].equals("call")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                callFunc(sym[1]);

            }
            else if(sym[0].equals("start")) { //record this pointer as current floor's func's start addr
                System.out.println("Read start");
                if (isIf(sym[1])) {

                } else if (sym[1].equals("#while")) {

                } else if (sym[1].equals("#block")) {
                    if(activeRunner == null && floor != 0){
                        continue;
                    }
                    newBlock();
                }
                else if (isElse(sym[1])){
                    if(activeRunner == null && floor != 0){
                        continue;
                    }
                    System.out.println("start #else, check else :" + activeRunner.isElseList.get(sym[1]));
                    if(activeRunner.isElseList.get(sym[1])){   //碰到start #else的时候，如果isElse是true就代表上一个if cond是 false,这个else要执行
                        //如果是else if,那么这边先把else设成false也没有影响,等if再判断再看是不是isElse
                        activeRunner.isElseList.put(sym[1],false);
                    }else{ //如果这时候isElse false代表上一个if是true,这边就要跳到下一个end
                        skipping = true;
                    }
                    //如果这时候是if已经执行,else if的时候就会跳到end #if,isElse false,所以碰到start #else也是skip
                }else{
                    HashMap<String,Symbol> symbList = new HashMap<String,Symbol>();
                    floor = floorList.size();  //排号
                    System.out.println("In newFloor,floor : " + floor);
                    symbTable.put(floor,symbList);  //创建新符号表
                    funcList.put(floor, currentFunc);  //记录func对象
                    floorList.put(currentFunc.name,floor);  //可以通过funcname找到floor
                    currentFunc.floor = floor;   //往func对象记录floor
                    recordStart();
                }
            }
            else if(sym[0].equals("ret")){  //ret 21t...
                if(activeRunner == null && floor != 0){
                    continue;
                }
                int type = 0;
                int value = 0;
                type = currentFunc.type;
                System.out.println("func name:"+currentFunc.name + "type:"+type);

                if(sym.length > 1){
                    hasRet = true;
                    value = senseValue(sym[1]);
                    returnInt(value);
                }
                else{
                    hasRet = false;
                    returnVoid();
                }
            }
            else if(sym[0].equals("end")){//record this pointer as current floor's func's end addr
                if(isIf(sym[1])){
                    if(activeRunner == null && floor != 0){
                        continue;
                    }
                    String str = Writer.readPcode(pointer);
                    String[] str1 = str.split(" ");
                    if(str1[0].equals("start") && activeRunner.currentIfEnd(sym[1])){

                    }else{
                        activeRunner.ifList.pop();
                    }

                }else if(sym[1].equals("#while")){
                    if(activeRunner == null && floor != 0){
                        continue;
                    }
                    activeRunner.whileEndList.put(activeRunner.getCurrentWhile(),pointer);
                    //System.out.println("Record while end pcode : currentWhile is "+activeRunner.getCurrentWhile()+" endList : " + Writer.readPcode(pointer));
                    //System.out.println("Check return to which while : "+activeRunner.getCurrentWhile());
                    //System.out.println("Check size of whileStartList : "+activeRunner.whileStartList.size());
                    pointer = activeRunner.whileStartList.get(activeRunner.getCurrentWhile());
                    //System.out.println("Return to while,check pcode : " +Writer.readPcode(pointer));
                }else if(sym[1].equals("#block")){
                    if(activeRunner == null && floor != 0){
                        continue;
                    }
                    endBlock();
                }else if(isElse(sym[1])){
                }else{
                    if(activeRunner == null && floor != 0){
                        recordEnd();
                        leaveFloor();
                    }
                    if(activeRunner != null){
                        returnVoid();
                        hasRet = false;
                    }
                }
            }
            else if(isTemp(sym[0])){ //0:1t 1:'=' 2:exp or arr (if arr  3: [ 4:d1 5:] 6:[ 7:d2 8:]
                if(activeRunner == null && floor != 0){
                    continue;
                }
                int value = 0;
                int dimension1=0,dimension2=0;

                if(sym[2].equals("!")){ //后面一定接变量或数字或varT
                    int result = -1;
                   // System.out.println("Execute, varT is !");
                    result = senseValue(sym[3]);

                    //System.out.println("Value sym[3]: " + result);
                    if(result == 0){
                        result = 1;
                    }else{
                        result = 0;
                    }
                    addtempList(sym[0],result);
                    continue;
                } else if(isExpression()){
                    String[] newSym = new String[sym.length-2];  //删掉前面两个符号
                    for(int i=0,j=2;j<sym.length;i++,j++){
                        newSym[i] = sym[j];
                    }
                    //System.out.println("To get exp: " + Arrays.toString(newSym));
                    String exp = getExpression(newSym);
                    //System.out.println("In var,expresson is "+exp);
                    Calculator cal = new Calculator(exp);
                    //Expression cal = new Expression(exp);
                    value = cal.calc();

                    //System.out.println(sym[0] + " value is "+value);
                }

                if(sym[2].equals("GETINT")){
                    value = getint();
                }

                if(isArr(sym,2)){
                    int dimension=0;
                    //1t = a [ 2 ] [ 3 ]
                    if(sym.length>6){
                        dimension=2;
                    }else{
                        dimension=1;
                    }

                    Symbol arr = getArr(sym[2]);
/*
                    if(arr instanceof paraArr){
                        arr = ((paraArr) arr).content;
                    }


 */
                    if(arr.dimensionNum != dimension){
                        //System.out.println("维度不一样");
                        //System.out.println("检查下一句是传参,跳过这句: " + Writer.readPcode(pointer));
                        pointer++;
                        //这里设定是传第几个维度过去
                        if(arr instanceof paraArr){
                            arr = ((paraArr) arr).content;
                        }
                        paraArr arrAsPara = new paraArr(arr.name);  //这边先放一样名字
                        arrAsPara.skipLevel=true;
                        //System.out.println("检查level是:"+arrAsPara.level);
                        arrAsPara.level=senseValue(sym[4]);
                        arrAsPara.content=arr;
                        activeRunner.exeStack.push(arrAsPara);
                    } else{ //正确，这边是传数字
                        if(( sym.length>6 )&& sym[3].equals("[") && sym[6].equals("[")){
                            dimension1 = senseValue(sym[4]);
                            dimension2 = senseValue(sym[7]);
                        } else if(sym[3].equals("[")){
                            dimension1 = 0;
                            dimension2 = senseValue(sym[4]);
                        }

                        value = getArrValue(sym[2],dimension1,dimension2);
                    }
                }

                if(sym[2].equals("RET")){
                    //System.out.println("hasRet is :" + hasRet);
                    if(hasRet){
                        value = (int) activeRunner.exeStack.pop();
                        System.out.println("RET = "+value);
                        hasRet = false;
                    }
                }

                //System.out.println("Value of "+ sym[0] + " : " + value);
                addtempList(sym[0],value);
            }
            else if(isStr(sym[0])){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                String str = getPrintStr();
                pushPrintQueue(sym[0],str);
            }
            else if(isIns(sym[0])){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                pushPrintQueue(sym[0],null);
            }
            else if(isAnw(sym[0])){ //anw-1 = <>
                if(activeRunner == null && floor != 0){
                    continue;
                }
                //System.out.println("IsAnw:"+Arrays.toString(sym));
                int value = 0;
                if(isVar(sym[2])){
                    //System.out.println("Is Var:");
                    value = getVarValue(sym[2]);
                }
                else if(isTemp(sym[2])){
                    //System.out.println("Is Temp");
                    value = getTempValue(sym[2]);
                }
                else if(isNumeric(sym[2])){
                    //System.out.println("Is Num");
                    value = Integer.parseInt(sym[2]);
                }
                //System.out.println("anw : " + value);
                pushPrintQueue(sym[0], String.valueOf(value));
            }
            else if(isLn(sym[0])){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                pushPrintQueue(sym[0],"\n");
            }
            else if(sym[0].equals("PRINTF")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                //System.out.println("NOW RUNNERSTACK:" + runnerStack.size());
                print();
            }
            else if(sym[0].equals("#while")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                activeRunner.whileStartList.put(activeRunner.getWhileNum(), pointer);

                //System.out.println("检查activeRunner是哪个:"+activeRunner.name);
                //System.out.println("Check whileNum when start #while :" +activeRunner.getCurrentWhile());
                //System.out.println("Check while start pointer:" + Writer.readPcode(pointer));
                //whilePointer=pointer+1;
                activeRunner.isWhile = true;
            }
            else if(isIf(sym[0])){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                if(activeRunner != null){
                    activeRunner.ifList.push(sym[0]);
                    System.out.println("push ifList: " + sym[0]);
                    activeRunner.isIf = true;
                }

            }
            else if(isCond(sym[0])){ // == < > <= >=
                if(activeRunner == null && floor != 0){
                    continue;
                }
                ArrayList<Boolean> resultList = new ArrayList<Boolean>();
                boolean currentRes=false;

                if(checkCondGotOp(sym)){
                    int value1,value2;
                    Stack<String> opList = new Stack<String>();
                    Stack<String> operandList = new Stack<String>();
                    for(int i=1;i<sym.length;i++) {
                        if (isOp(sym[i])) {
                            if (opList.size() > 0 && (opPrior(opList.peek()) >= opPrior(sym[i]))) {
                                value2 = senseValue(operandList.pop());
                                value1 = senseValue(operandList.pop());
                                operandList.push(calcCond(value1, value2, opList.pop()));
                            }
                            opList.push(sym[i]);
                        } else if(isTemp(sym[i]) || isVar(sym[i]) || isNumeric(sym[i])){
                            operandList.push(sym[i]);
                        }
                    }
                    while(opList.size() > 0){
                        value2 = senseValue(operandList.pop());
                        value1 = senseValue(operandList.pop());
                        operandList.add(calcCond(value1, value2, opList.pop()));
                    }
                    System.out.println("检查opList size:"+opList.size());
                    System.out.println("检查operandList size:"+operandList.size());
                    System.out.println("检查最后答案:" + operandList.peek());
                    System.out.println(
                            operandList.toString().replaceAll("\\[", "").replaceAll("]", ""));

                    String res = operandList.pop();
                    if(res.equals("0")){
                        resultList.add(false);
                    }else{
                        resultList.add(true);
                    }

/*
                    boolean res = false;
                    for(int i=1,j=1;i<sym.length;i++){
                        if(isOp(sym[i])){
                            value2 = senseValue(sym[i+1]);
                            res = calcCond(value1,value2,sym[i]);
                            resultList.add(calcCond(value1,value2,sym[i]));
                            //if(resultList.contains(false)) condResult =false;
                        }else{
                            if(!res)
                                value1 = senseValue(sym[i]);
                            else{
                                if(res){
                                    value1 = 1;
                                }else{
                                    value1=0;
                                }
                            }
                        }
                    }

 */
                }else{
                    int value = senseValue(sym[1]);
                    if(value==0){
                        System.out.println("result = false");
                        resultList.add(false);
                    }else{ resultList.add(true);
                        System.out.println("result = true");
                    }
                }


//-----------------------------以上是计算一个cond---------------------------
                if(resultList.contains(false)){
                    System.out.println("当前cond:" + pcode + ",result = false");
                    if(sym[sym.length-1].equals("&&")) {  //如果是false + && 有 && 短路
                        while(!sym[0].equals("CheckCond") && !sym[sym.length-1].equals("||")){
                            readPcode();
                            sym = pcode.split(" ");
                        }
                        System.out.println("&&短路");
                        if(sym[0].equals("CheckCond")){
                            System.out.println("skip到CheckCond");
                            activeRunner.condList.push(false);
                            currentRes =false;
                            pointer--;
                        }else{
                            System.out.println("skip到||");
                        }
                    }else if(!sym[sym.length-1].equals("&&") && !sym[sym.length-1].equals("||")){
                        activeRunner.condList.push(false);
                        currentRes =false;
                    }
                }else{
                    System.out.println("当前cond:" + pcode + ",result = true");
                    if(sym[sym.length-1].equals("||")){
                        System.out.println("整个cond短路");
                        activeRunner.condList.push(true);
                        currentRes=true;
                        shortCircuit = true;
                    }else if(!sym[sym.length-1].equals("&&") && !sym[sym.length-1].equals("||")){
                        activeRunner.condList.push(true);
                        currentRes =false;
                    }
                }
//-----------------------------记录当前cond的结果-------------------------------
                /*
                if(sym[sym.length-1].equals("&&")){
                    if(!activeRunner.condList.peek()){  //如果是false
                        shortCircuit=true;
                        continue;
                    }
                }
                if(sym[sym.length-1].equals("||")){
                    if(currentRes){
                        shortCircuit=true;
                        continue;
                    }
                }

                if(activeRunner.condList.size() > 1){
                    System.out.println("检查short circuit , pcode = "+pcode);
                    String currentOp = sym[sym.length-1];
                    boolean cond2 = activeRunner.condList.pop();
                    boolean cond1 = activeRunner.condList.pop();
                    String op = activeRunner.condOpList.pop();
                    System.out.print("currentOp = "+ currentOp);
                    System.out.print(" cond1 = "+ cond1);
                    System.out.print(" cond2 = "+ cond2);
                    System.out.println(" op = "+ op);

                    boolean result = compareCond(cond1,cond2,op);

                    if(op.equals("||") && result){
                        System.out.println("OR short circuit");
                        activeRunner.condList.push(true);
                        shortCircuit = true;
                    }else if(op.equals("&&") && !result){
                        if(currentOp.equals("||") || currentOp.equals("&&")) {
                            if (currentOp.equals("&&")) {
                                System.out.println("AND short circuit");
                                activeRunner.condList.push(false);
                                shortCircuit = true;
                            }
                        }
                        activeRunner.condList.push(false);
                    }else{
                        activeRunner.condList.push(result);
                    }
                }
                String op = sym[sym.length-1];
                if(op.equals("||") || op.equals("&&")){
                    if(op.equals("||") && activeRunner.condList.contains(true)){
                        shortCircuit = true;
                    }
                    if(op.equals("&&") && activeRunner.condList.contains(false)){
                        shortCircuit = true;
                    }
                    else{
                        activeRunner.condOpList.push(sym[sym.length-1]);
                    }
                }

                checkCondList();

 */

            }
            else if(sym[0].equals("CheckCond")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                if(activeRunner.whileStartList.size() == 0 && activeRunner.whileNum > 1) {
                    //System.out.println("Error whileStart = 0");
                }
                //System.out.println("Check condList size is 1 : "+activeRunner.condList.size());
                condResult = activeRunner.condList.pop();
                activeRunner.condList.clear();
                //activeRunner.isElseList.get() = false;
                if(!condResult){
                    //System.out.println("Cond is false");
                    readPcode();
                    sym = pcode.split(" ");
                    if(isIf(sym[1])){
                        //System.out.println("Cond false,skip to else");
                        skipping = true;
                        activeRunner.isElseList.put(ifToElse(sym[1]),true);
                    }else if(sym[1].equals("#while")){
                        //System.out.println("Check cond false,skip while");
                        //System.out.println("Remove whileStart: " + activeRunner.getCurrentWhile());
                        activeRunner.isWhile=false;

                        if(activeRunner.whileEndList.containsKey(activeRunner.getCurrentWhile())){
                            //System.out.println("----------whileEndList contain key--------------");
                            pointer= activeRunner.whileEndList.get(activeRunner.getCurrentWhile());
                        }else{
                            //System.out.println("-----whileEndList not contain, hence skipping-----");
                            String currentWhile = activeRunner.getCurrentWhile();
                            while(true){
                                readPcode();
                                //System.out.println("skipping while, pcode : "+ pcode);
                                sym = pcode.split(" ");
                                if(sym[0].equals("start")){//pcode.equals("start #while")
                                    if(sym[1].equals("#while")){
                                        activeRunner.getWhileNum();

                                    }
                                    /*else if(isIf(sym[1])){
                                        activeRunner.ifList.push(sym[1]);
                                    }
                                     */
                                    //activeRunner.whileStartList.put(activeRunner.getWhileNum(), pointer);
                                }
                                if(sym[0].equals("end")){//pcode.equals("end #while")
                                    if(sym[1].equals("#while")){
                                        //System.out.println("读到while");
                                        if(!activeRunner.getCurrentWhile().equals(currentWhile)){
                                            //activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                                            activeRunner.endWhile();
                                        }else{
                                            break;
                                        }
                                    }
                                    else if(activeRunner.ifList.size()>0 && sym[1].equals(activeRunner.ifList.peek())){
                                        activeRunner.ifList.pop();
                                    }
                                }
                            }
                            //System.out.println("Check now should be end #while , "+pcode);
                            //readPcode();
                        }
                        //System.out.println("remove whileStart : "+activeRunner.getCurrentWhile());
                        activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                        //System.out.println("remove whileEnd : "+activeRunner.getCurrentWhile());
                        activeRunner.whileEndList.remove(activeRunner.getCurrentWhile());
                        activeRunner.endWhile();
                        //System.out.println("Cond is false, skip while and remove whileStart : " + activeRunner.getCurrentWhile());
                        //System.out.println("Check skipStartList size " + activeRunner.whileStartList.size());
                        //System.out.println("Debug: pcode = "+pcode);
                        //skipping = true;
                    }
                }else{
                    readPcode();
                    sym = pcode.split(" ");
                    activeRunner.isElseList.put(ifToElse(sym[1]),false);
                    //System.out.println("Cond true, end at if");
                    //System.out.println("Check else is false:" + activeRunner.isElse);
                }
            }
            else if(sym[0].equals("CONTINUE")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                if(activeRunner.whileStartList.containsKey(activeRunner.getCurrentWhile())){
                    pointer = activeRunner.whileStartList.get(activeRunner.getCurrentWhile());
                    //System.out.println("Continue and Return to while,check pcode : " +Writer.readPcode(pointer));
                }else{
                    getWhileStart();
                }
            }
            else if(sym[0].equals("BREAK")){
                if(activeRunner == null && floor != 0){
                    continue;
                }
                if(activeRunner.whileStartList.containsKey(activeRunner.getCurrentWhile())){
                    //System.out.println("Remove whileStart: " + activeRunner.getCurrentWhile());
                    if(activeRunner.whileEndList.containsKey(activeRunner.getCurrentWhile())){
                        pointer= activeRunner.whileEndList.get(activeRunner.getCurrentWhile());
                    }else{
                        String currentWhile = activeRunner.getCurrentWhile();
                        //System.out.println("没记录whileEnd,寻找 "+currentWhile);
                        while(true){
                            readPcode();
                            //System.out.println("skipping while, pcode : "+ pcode);
                            sym = pcode.split(" ");
                            if(sym[0].equals("start")){//pcode.equals("start #while")
                                if(sym[1].equals("#while")){
                                    activeRunner.getWhileNum();
                                }
                                /*else if(isIf(sym[1])){
                                    activeRunner.ifList.push(sym[1]);
                                }
                                 */
                                //activeRunner.whileStartList.put(activeRunner.getWhileNum(), pointer);
                            }
                            if(sym[0].equals("end")){//pcode.equals("end #while")
                                /*
                                if(activeRunner.ifList.size()>0)
                                    System.out.println("检查当前if:"+activeRunner.ifList.peek());
                                    
                                 */
                                if(sym[1].equals("#while")) {
                                    if (!activeRunner.getCurrentWhile().equals(currentWhile)) {
                                        //activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                                        activeRunner.endWhile();
                                    } else {
                                        break;
                                    }
                                }
                                else if(activeRunner.ifList.size()>0 && sym[1].equals(activeRunner.ifList.peek())){
                                    activeRunner.ifList.pop();
                                }
                            }
                        }
                        System.out.println("Check now should be end #while , "+pcode);
                        //readPcode();
                    }
                    System.out.println("Break, skip while and remove whileStart : " + activeRunner.getCurrentWhile());
                    System.out.println("Check skipStartList size " + activeRunner.whileStartList.size());
                    activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                    activeRunner.whileEndList.remove(activeRunner.getCurrentWhile());
                    activeRunner.endWhile();
                    //skipping = true;
                    //continue;
                }else if((activeRunner instanceof Block)){
                    int pt = getWhileEnd();
                    System.out.println("error whilestartlist = 0");
                }
            }
        }
    }

    public int getWhileStart() {
        Runner readRunner;
        int index = 2;
        int startPointer = 0;
        while (startPointer == 0) { //如果当前runner没有这个变量
            if(activeRunner instanceof Block){
                endBlock();
            }
            if(activeRunner.whileStartList.containsKey(activeRunner.getCurrentWhile())){
                pointer = activeRunner.whileStartList.get(activeRunner.getCurrentWhile());
                startPointer = pointer;
            }
            System.out.println("当前是block且while的开始是在block外面");
        }
        return startPointer;
    }

    public int getWhileEnd() {//如果block里面没有whileStart
        Runner readRunner;
        int index = 2;
        int pt = 0;
        while (pt == 0) { //如果当前runner没有这个变量
            if(activeRunner instanceof Block){
                endBlock();
            }
            pointer = activeRunner.addrPointer;
            if(activeRunner.whileStartList.containsKey(activeRunner.getCurrentWhile())){
                if(activeRunner.whileEndList.containsKey(activeRunner.getCurrentWhile())){
                    pointer = activeRunner.whileEndList.get(activeRunner.getCurrentWhile());
                    pt = pointer;

                }else{
                    String currentWhile = activeRunner.getCurrentWhile();
                    System.out.println("没记录whileEnd,寻找 "+currentWhile);
                    while(true){
                        readPcode();
                        //System.out.println("检查当前的pcode "+ pcode);
                        if(pcode.equals("start #while")){
                            activeRunner.getWhileNum();
                            //activeRunner.whileStartList.put(activeRunner.getWhileNum(), pointer);
                        }
                        if(pcode.equals("end #while")){
                            if(!activeRunner.getCurrentWhile().equals(currentWhile)){
                                //activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                                activeRunner.endWhile();
                            }else{
                                break;
                                //activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                            }
                        }
                    }
                    System.out.println("找到了,检查当前应该找的endWhile: "+pcode);
                }
                activeRunner.whileStartList.remove(activeRunner.getCurrentWhile());
                activeRunner.whileEndList.remove(activeRunner.getCurrentWhile());
                activeRunner.endWhile();
                pt = pointer;
            }
        }
        return pt;
    }

    public void readPcode() {
        pcode = Writer.readPcode(pointer);
        if (activeRunner != null) {
            activeRunner.addrPointer = pointer;
        }
        pointer++;
    }

    public boolean hasPcode() {
        if(pointer <= Writer.pcodeLength()){
            return true;
        }
        else{
            return false;
        }
    }

    public void checkCondList(){
        System.out.println("CheckCondList");
        for(int i=0;i<activeRunner.condList.size();i++){
            System.out.println(activeRunner.condList.get(i));
        }

    }

    public int senseValue(String sym){
        if(isNumeric(sym)){
            return Integer.parseInt(sym);
        }else if(isVar(sym)){
            return getVarValue(sym);
        }else if(isTemp(sym)){
            return getTempValue(sym);
        }
        return 0;
    }

    public void pushPara(String var) {

        int value = 0;
        if(isTemp(var)) {
            value = getTempValue(var);
        }
        else if(isVar(var)){

            Symbol symb = getVar(var);
            System.out.println("pushPara,the symb is " + symb.getClass());
            if(symb instanceof Variable || symb instanceof paraVar){
                System.out.println("Check to getVarValue");
                value = getVarValue(var);
            }
            if(symb instanceof Arr){
                System.out.println("In push para,para is arr ");
                paraArr arrAsPara = new paraArr(symb.name);
                arrAsPara.skipLevel = symb.skipLevel;
                arrAsPara.level = symb.level;
                arrAsPara.content = symb;
                activeRunner.pushExeStack(arrAsPara);
                return;
            }
            if(symb instanceof paraArr){
                paraArr arrAsPara = new paraArr(symb.name);
                arrAsPara.skipLevel = symb.skipLevel;
                arrAsPara.level = symb.level;
                arrAsPara.content = ((paraArr) symb).content;
                activeRunner.pushExeStack(arrAsPara);
                return;
            }
        }
        else if(isNumeric(var)){

            value = Integer.parseInt(var);
        }

        System.out.println("Push para value:" + value);
        activeRunner.pushExeStack(value);
    }

    public Runner newRunner() {
        Runner newRunner = new Runner(pointer, currentFunc, floor,level);
        if(level==0){
            newRunner.name = "main";
            runnerStack.push(newRunner);
            activeRunner = newRunner;
            level++;
            System.out.println("Set main runner:"+ activeRunner.func.name);
        }
        //level++;
        return newRunner;
    }
    public boolean checkCondGotOp(String[] sym){
        for(int i =1;i<sym.length;i++){
            if(isOp(sym[i])){
                return true;
            }
        }
        return false;
    }
    public Func newFloor(String name,int type){
        Func func = new Func(name,type);

        currentFunc = func;  //设成现在正在读取的floor
        symbTable.get(0).put(name,func); //放进全局层

        if(name.equals("main")){
            newRunner();
        }
        return func;
    }

    public void newBlock(){
        //checkStackRunner();
        Block newBlock = new Block(pointer, currentFunc, floor,level);
        newBlock.addrPointer = pointer;
        newBlock.funcFloor = currentFunc.floor;
        System.out.println("newBlock ,currentFunc:"+ currentFunc.name);
        newBlock.func = currentFunc;
        newBlock.name = "block"+blocknum++;
        newBlock.level = level;
        /*
        newBlock.whileStartList = activeRunner.whileStartList;
        newBlock.whileEndList = activeRunner.whileEndList;
        newBlock.whileNum= activeRunner.whileNum;

         */
        level++;

        runnerStack.push(newBlock);
        activeRunner = newBlock;
        checkStackRunner();
        //checkStackRunner();
        //return newBlock;
    }

    public void endBlock(){
        runnerStack.pop();

        activeRunner = runnerStack.peek();
        level--;
        blocknum--;
        System.out.println("endBlock,level="+level);
        checkStackRunner();
    }

    public void recordStart(){
        currentFunc.addrS = pointer;  //避开start
        checkStackRunner();
    }
    public void recordEnd(){
        currentFunc.addrE = pointer-1;  //不用避开end
    }
    public String getExpression(String[] str){
        int value;
        String str2 = "";
        String tmp = "";
        for(int i = 0;i<str.length;i++){
            if(isVar(str[i])){
                //tmp =  getVarValue(str[i]);
                System.out.println("Going to get varvalue");
                value = getVarValue(str[i]);
                str2 += value;
                System.out.println("value is : " + getVarValue(str[i]));
            }
            else if(isTemp(str[i])){
                //str2 += getTempValue(str[i]);
                value = getTempValue(str[i]);
                str2 += value;
            }
            else{
                str2 += str[i];
            }
        }
        System.out.println("getExp result:" + str2);
        return str2;
    }

    public boolean isPlusMin(String ch){
        if(ch.equals("+") || ch.equals("-")){
            return true;
        }
        return false;
    }

    public boolean isMulDiv(char ch){
        if(ch == '*' || ch == '/' || ch == '%' ){
            return true;
        }
        return false;
    }

    public void leaveFloor(){
        floor = 0;
    }

    public void callFunc(String name){
        int index = floorList.get(name);
        Func func = funcList.get(index);
        activeRunner.addrPointer = pointer;
        Runner newRunner = newRunner();
        newRunner.addrPointer = func.addrS;
        newRunner.endAddr = func.addrE;
        newRunner.funcFloor = func.floor;
        newRunner.func = func;
        newRunner.level = level;
        newRunner.name = func.name;
        level++;

        System.out.println("In call func,activeRunner: " + activeRunner + " funcfloor:" + activeRunner.funcFloor);
        System.out.println("The func is : " + func.name);
        int value;
        while(1==1){
            Para para = new Para("");
            para = func.getPara();  //get para from func

            if(para != null){  //if still got para
                System.out.println("Got para");
                if(para instanceof paraVar){  //

                    Variable var = new Variable(para.name,1);
                    var.value = (int) activeRunner.exeStack.pop();

                    //para.value = (int) activeRunner.exeStack.pop();
                    System.out.println("Para in exestack:" + var.value);
                    newRunner.symbList.put(var.name,var);
                }
                else if(para instanceof paraArr){
                    paraArr symb = (paraArr) activeRunner.exeStack.pop();
                    System.out.println("确保是paraArr类型:" + symb.getClass());

                    para.skipLevel = symb.skipLevel;

                    para.level = symb.level;
                    ((paraArr) para).content = symb.content;
                    System.out.println("paraArr id:" + para);
                    System.out.println("检查callfunc时level是:"+para.level);
                    System.out.println("参数是数组类型,检查主要数组存在content :" + ((paraArr) para).content.name);
                    System.out.println("参数是数组,参数名:" + ((paraArr) para).name +" ,真正指向的数组是 :" + ((paraArr) para).content.name);
                    newRunner.symbList.put(para.name,para);  //direct send the arr to function
                }
            }
            else{
                break;
            }
        }

        func.recoverPt();
        runnerStack.push(newRunner);
        activeRunner = newRunner;
        currentFunc = func;

        pointer = newRunner.addrPointer;
    }

    public void returnInt(int value){
        System.out.println("Return : " + value);
        if(runnerStack.size() > 1){  //除了activeRunner还有可以返回的runner
            System.out.println("Pop out runner :" + runnerStack.peek().func.name);
            //checkStackRunner();
            activeRunner.recoverSkipLevel();
            runnerStack.pop();

            Runner runner = runnerStack.peek();
            runner.exeStack.push(Integer.valueOf(value));  //把RET压入运行栈
            pointer = runner.addrPointer;
            activeRunner = runner;
            currentFunc = activeRunner.func;
            level--;
            System.out.println("func ret, check level :" + level);
        }
    }

    public void returnVoid(){
        if(runnerStack.size() > 1){  //除了activeRunner还有可以返回的runner
            activeRunner.recoverSkipLevel();
            runnerStack.pop();
            System.out.println("Pop Runner");
            Runner runner = runnerStack.peek();
            pointer = runner.addrPointer;
            activeRunner = runner;
            currentFunc = activeRunner.func;
            level--;
            System.out.println("Debug, funcName is " + activeRunner.func.name + ", check whileList size : "+ activeRunner.whileStartList.size());
        }
    }

    public String getPrintStr(){ //因为formatstring里面可能有其他空格所以这边传来的一定是完整的pcode
        String newstr = "";
        int i=0;
        while(pcode.charAt(i) != '='){
            i++;
        }
        i++;
        System.out.println("In getPrintStr: ch = " + pcode.charAt(i));
        for(;i<pcode.length();i++){
            newstr += pcode.charAt(i);
        }
        return newstr;
    }

    public void print() throws IOException {
        String str = "";
        System.out.println("In print() ,currentFunc:" + activeRunner.func.name);
        for(String key:activeRunner.printQueue.keySet()){
            str += activeRunner.printQueue.get(key);
        }
        writer.write(str);
        writer.flush();
        activeRunner.printQueue.clear();
    }

    public int getint(){
        int value;
        value = scanner.nextInt();
        //String value = scan.next();
        //return Integer.parseInt(value);

        return value;
    }

    public Variable newVar(String name,int type){
        Variable var = new Variable(name,type);
        symbTable.get(floor).put(name,var);
        if(activeRunner!=null){
            //System.out.println("newVar, activerunner = "+activeRunner.func.name+ " var = " + name);
            activeRunner.symbList.put(name,var);
        }
        return var;
    }

    public String calcCond(int value1,int value2,String op){
        System.out.println("calcCond, value1 = "+value1+",value2="+value2+",op="+op);
        if(op.equals("==")) {
            if(value1 == value2) return "1";
            else return "0";
        }else if(op.equals("!=")){
            if(value1 != value2) return "1";
            else return "0";
        }else if(op.equals("<=")){
            if(value1 <= value2) return "1";
            else return "0";
        }else if(op.equals(">=")){
            if(value1 >= value2) return "1";
            else return "0";
        }else if(op.equals(">")){
            if(value1 > value2) return "1";
            else return "0";
        }else if(op.equals("<")){
            if(value1 < value2) return "1";
            else return "0";
        }
        return "0";
    }

    public boolean compareCond(boolean cond1,boolean cond2,String op){
        if(op.equals("||")) {
            return (cond1 || cond2);
        }else if(op.equals("&&")) {
            return (cond1 && cond2);
        }
        return false;
    }

    public boolean checkCondGotAndOr(String[] sym){
        for(int i=1;i<sym.length;i++){
            if(isAndOr(sym[i])){
                return true;
            }
        }
        return false;
    }

    public boolean isAndOr(String sym){
        if(sym.equals("||") || sym.equals("&&")){
            return true;
        }else {
            return false;
        }
    }



    public Arr newArr(String name,int type){
        Arr arr = new Arr(name,type);

        symbTable.get(floor).put(name,arr);
        if(activeRunner!=null){
            activeRunner.symbList.put(name,arr);

        }
        return arr;
    }

    public String ifToElse(String ifStr){
        char ch = ifStr.charAt(ifStr.length()-1);
        return "#else"+ch;
    }

    public Para newPara(String[] arr){
        //para a  a [ ] [ 2 ]
        //para v b
        //调用之后应该func里面的paralist会存有对象，同时SymbTable里面同层的symbTable也insert了
        int value;
        Para para = null;
        System.out.println("Para name : " + arr[2]);
        if(arr[1].equals("a")){
            String name = arr[2];
            para = new paraArr(name);
            if(!arr[4].equals("]")){//有一维数字
                if(arr.length > 6){
                    para.dimensionNum = 2;
                    ((paraArr) para).addDimension(senseValue(arr[4]));
                    ((paraArr) para).addDimension(senseValue(arr[7]));
                }
                else{
                    para.dimensionNum = 1;
                    ((paraArr) para).addDimension(0);
                    ((paraArr) para).addDimension(senseValue(arr[4]));
                }
            }
            else{ //一维是空的
                //((paraArr) para).addDimension(0);
                System.out.println("ARR LENGTH: " + arr.length);
                if(arr.length > 5){
                    para.dimensionNum = 2;
                    ((paraArr) para).addDimension(0);
                    ((paraArr) para).addDimension(senseValue(arr[6]));
                }
                else{
                    para.dimensionNum = 1;
                    ((paraArr) para).addDimension(0);
                    ((paraArr) para).addDimension(0);
                }
            }
            currentFunc.addPara(para);
            System.out.println("newPara: " + currentFunc.paraList.get(currentFunc.pt).name);
            //addSymb(arr[2],para);  无法确定当前symbTable是func那个，这边加了后面也不会用到，所以不要加
        }
        else if(arr[1].equals("v")){
            para = new paraVar(arr[2]);
            System.out.println("CurrentFunc is :" + currentFunc.name);
            currentFunc.addPara(para);
            System.out.println("Here to put paraVar:" + para.name);
            System.out.println("Check para in currentFunc : " + currentFunc.paraList.get(currentFunc.pt).name);
            //addSymb(arr[2],para);  无法确定当前symbTable是func那个，这边加了后面也不会用到，所以不要加
           //symbTable.get(floor).put(arr[2],para);
        }
        return para;
    }

    public void addSymb(String name, Symbol symbol) {
        HashMap<String, Symbol> map = symbTable.get(floor);  //get symbtable of current floor
        map.put(name, symbol); //put symbol into symbtable according name
    }

    public void pushPrintQueue(String str, String content) {
        System.out.println("PushPrintQueue:" + str + ":" + content);
        if (isAnw(str)) {
            str = switchIns(str);
            if(activeRunner.printQueue.containsKey(str)){
                activeRunner.printQueue.put(str, content);
            }
        }else{
            activeRunner.printQueue.put(str, content);
        }
        //
    }

    public void addtempList(String str, Integer value) {
        if(activeRunner!= null){
            activeRunner.tempList.put(str, value);
        }else{
            publicTempList.put(str,value);
        }
    }

    public boolean isAnw(String str) {
        Pattern pattern = Pattern.compile("anw-[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean isStr(String str) {
        Pattern pattern = Pattern.compile("str-[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean isIns(String str) {
        Pattern pattern = Pattern.compile("ins-[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean isLn(String str) {
        Pattern pattern = Pattern.compile("ln-[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean isTemp(String str) {
        Pattern pattern = Pattern.compile("[0-9][0-9]*t");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean isCond(String str) {
        Pattern pattern = Pattern.compile("#cond[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isIf(String str){
        Pattern pattern = Pattern.compile("#if[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isElse(String str){
        Pattern pattern = Pattern.compile("#else[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public boolean isWhile(String str){
        Pattern pattern = Pattern.compile("#while[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String switchIns(String str) {
        String str2 = "";
        int i=0;
        while(str.charAt(i)!='-'){
            i++;
        }
        i++;
        for(;i<str.length();i++){
            str2 += str.charAt(i);
        }
        System.out.println("switchIns: " + str2);
        return "ins-" + str2;
    }

    public int getTempValue(String name) {
        if(activeRunner!=null){
            System.out.println("GetTempValue , " + name +" = "+ activeRunner.tempList.get(name));
            return activeRunner.tempList.get(name);

        }else{
            System.out.println("GetTempValue , " + name +" = "+ publicTempList.get(name));
            return publicTempList.get(name);
        }


    }

    public int getVarValue(String name) {
        Symbol symb = null;
        Runner readRunner;
        int index = 2;

        symb = getVar(name);

        System.out.println("GetVarValue,"+ name + " :" + symb.value);
        return symb.value;
    }

    public Symbol getVar(String name) {
        Symbol symb = null;
        Runner readRunner;
        int index = 2;
        if(activeRunner != null)
            symb = activeRunner.getSymb(name);
        //checkStackRunner();
        while (symb == null) { //如果当前runner没有这个变量
            System.out.println("activeRunner don have");
            if(activeRunner == null || level<=1){ //if current is public var
                break;
            }
            if(activeRunner instanceof Block){
                System.out.println("getVar,level:" + level);
                readRunner = runnerStack.get(level-index);  //如果这层还是没有


                index++; //如果这层还是没有
                symb = readRunner.getSymb(name);
            }
            else{
                if(index == 2){
                    System.out.println("symb is null and not block");
                    break;
                }else{
                    System.out.println("outside block, inside function");
                    readRunner = runnerStack.get(level-index);
                    index++;
                    symb = readRunner.getSymb(name);
                }
            }
            if(index>level){
                break;
            }

        }
        if(symb==null){
            symb = symbTable.get(0).get(name);
        }
        return symb;
    }

    public int getArrValue(String name, int dimension1, int dimension2) {
        Symbol symb;
        int index;
        int value;
        int dimension;
        System.out.println("symb :" +name);
        int level=0;
        symb = getArr(name);
        boolean skiplevel=false;

        if(symb instanceof paraArr){
            System.out.println("查数组value,是函数的传参数组");
            skiplevel = symb.skipLevel;
            level = symb.level;
            symb = ((paraArr) symb).content;
        }

        System.out.println("getArrValue,symb is :" + symb);
        if(skiplevel){
            System.out.println("skipLevel :" + level);
            dimension = symb.dimension.get(1);
            index = level*dimension + dimension2;
        }else{
            System.out.println("arr dimension value,dim1 = "+symb.dimension.get(0) + " dim2 = "+symb.dimension.get(1));
            System.out.println("dimension1= "+dimension1 + " dimension2=" + dimension2);
            index = symb.dimension.get(1) * dimension1 + dimension2;
        }
        System.out.println("Arr value size:" + symb.valueList.size());
        System.out.println("index = " + index);
        //index = symb.dimension.get(1) * dimension1 + dimension2;
        value = symb.valueList.get(index);
        System.out.println("GetArrValue:" + value);
        return value;
    }

    public Symbol getArr(String name) {
        Symbol symb = null;
        Runner readRunner;
        int index = 2;
        if(activeRunner != null)
            symb = activeRunner.getSymb(name);
        while (symb == null) { //如果当前runner没有这个变量
            System.out.println("activeRunner don have");
            if(activeRunner == null || level <= 1){
                break;
            }
            if(activeRunner instanceof Block){
                System.out.println("garArr,level="+level);
                readRunner = runnerStack.get(level-index);  //如果这层还是没有
                System.out.println("activeRunner is block,level :" + readRunner.level);
                index++; //如果这层还是没有
                symb =(Arr) readRunner.getSymb(name);
            }
            else{
                if(index == 2){
                    System.out.println("symb is null and not block");
                    break;
                }else{
                    System.out.println("outside block, inside function");
                    readRunner = runnerStack.get(level-index);
                    index++;
                    symb = (Arr)readRunner.getSymb(name);
                }
            }
            if(index>level){
                break;
            }
        }
        if(symb==null){
            symb = (Arr)symbTable.get(0).get(name);
        }
        return symb;
    }

    public void updateVar(String name,int newValue){
        Symbol symb;
        Runner readRunner;

        symb = getVar(name);
        symb.value = newValue;
    }

    public void checkStackRunner(){
        System.out.println("检查RunnerStack");
        for(int i=0;i<runnerStack.size();i++){
            System.out.print(runnerStack.get(i).name+" ");
        }
        System.out.println();
    }

    public void updateArr(String name, int dimension1, int dimension2,int newValue) {
        Symbol symb;
        Arr arr;
        int index;
        int value;
        int dimension;

        symb=getArr(name);
        if(symb instanceof paraArr){
            arr = (Arr) ((paraArr) symb).content;
            System.out.println("检查updateArr,是paraArr,实际变值的是:" + arr.name + " paraArr id:" + symb);
        }else{
            arr = (Arr) symb;
        }
        if(symb.skipLevel){
            dimension = arr.dimension.get(1);
            System.out.println("检查level是:" + arr.level);
            index = symb.level*dimension + dimension2;
            System.out.println("检查skip level index是:" + index);
        }else{
            index = arr.dimension.get(1) * dimension1 + dimension2;
            System.out.println("检查普通 index是:" + index);

        }
        arr.valueList.set(index,newValue);
    }

    public boolean isArr(String[] arr,int index){
        if((arr.length - index) > 3){
            if (isVar(arr[index])) {
                if (arr[index + 1].equals("[")) {
                    return true;
                }
                else{
                    return false;
                }
            }
            return false;
        }
        return false;
    }


    public boolean isVar(String str){
        if(str.charAt(0) == '_' || Character.isAlphabetic(str.charAt(0))){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i) != '_' && !Character.isAlphabetic(str.charAt(i)) && !isNumeric(String.valueOf(str.charAt(i)))){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public boolean isOp(String sym){
        if(sym.equals("==") || sym.equals("!=") || sym.equals("<=") || sym.equals(">=") || sym.equals(">")|| sym.equals("<")){
            return true;
        }else{
            return false;
        }
    }

    public int opPrior(String sym){
        if(sym.equals("==") || sym.equals("!=")){
            return 1;
        }else{
            return 2;
        }
    }

    public void write(String content) throws IOException {
        content += '\n';
        writer.write(content);
        writer.flush();
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }

    public boolean isExpression(){
        System.out.println(pcode);
        String[] str = pcode.split(" ");
        boolean isFunc=false;
        for(int i=0;i<str.length-1;i++){
            if(isVar(str[i]) && str[i+1].equals("(")){
                isFunc=true;
            }
        }
        if(pcode.contains("+") || pcode.contains("-") || pcode.contains("*") || pcode.contains("/") || pcode.contains("%") || (pcode.contains("(")&&pcode.contains(")")&& isFunc==false)){
            return true;
        }
        else{
            System.out.println("return false");
            return false;
        }
    }
}

class Runner{
    String name;
    Stack<Object> exeStack = new Stack<Object>();
    HashMap<String,Symbol> symbList = new HashMap<String,Symbol>();
    LinkedHashMap<String,String> printQueue = new LinkedHashMap<String,String>();
    int level; //optional
    int addrPointer;
    int endAddr;
    Func func;
    int funcFloor;
    HashMap<String, Integer> tempList = new HashMap<String, Integer>(); //temp var
    HashMap<String,Integer> whileStartList = new HashMap<String,Integer>();
    HashMap<String,Integer> whileEndList = new HashMap<String,Integer>();
    HashMap<String,Boolean> isElseList = new HashMap<String,Boolean>();

    static HashMap<String,Integer> ifElseList = new HashMap<String,Integer>();
    static Stack<String> condOpList = new Stack<String>();
    static Stack<Boolean> condList = new Stack<Boolean>();
    Stack<String> ifList = new Stack<String>();
    boolean isWhile = false;
    boolean isIf = false;
    boolean isElse = false;
    String currentWhile;
    int whileNum=1;

    public String getWhileNum(){
        return "while"+whileNum++;
    }
    public String getCurrentWhile(){
        return "while"+ (whileNum-1);
    }

    public void endWhile(){
        whileNum--;
    }

    public Runner(int addrPointer,Func func,int funcFloor,int level){
        this.addrPointer = addrPointer;
        this.func = func;
        this.funcFloor = funcFloor;
        this.level = level;
        tempList.putAll(publicTempList);
    }

    public Symbol getSymb(String name){ //get symbol from active runner symbtable
        return symbList.get(name);
    }

    public void pushExeStack(Object obj){
        exeStack.push(obj);
        System.out.println("Push into exe stack,size " + getStackSize());
    }
    public int getStackSize(){
        return exeStack.size();
    }
    public int getSymbSize(){
        return symbList.size();
    }

    public void recoverSkipLevel(){
        Func func = activeRunner.func;
        Symbol symb;
        for(int i=0;i<func.paraList.size();i++){
            symb = func.paraList.get(i);
            if(symb instanceof paraArr){
                if(((paraArr) symb).content instanceof Arr){
                    ((paraArr) symb).content.skipLevel = false;
                    ((paraArr) symb).content.level = 0;
                }
            }
        }
    }

    public boolean currentIfEnd(String str) {
        if (Executor.isIf(str)) {
            System.out.println("currentIfEnd,检查读到的if:"+str);
            if (str.equals(ifList.peek())) {
                System.out.println("是当前的if,peek:"+ifList.peek());
                return true;
            } else {
                return false;
            }
        } else if (Executor.isElse(str)) {
            if (str.charAt(str.length() - 1) == ifList.peek().charAt(ifList.peek().length() - 1)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void checkExeStack(){
        System.out.println("检查ExeStack.");
        for(int i=0;i<activeRunner.exeStack.size();i++){
            System.out.print(activeRunner.exeStack.get(i)+" ");
        }
        System.out.println();

    }
}

/*
class tester{
    public static void main(String[] args) {
        System.out.println(Executor.switchIns("anw-54"));

    }
}

 */

class Block extends Runner{

    public Block(int addrPointer,Func func,int funcFloor,int level) {
        super(addrPointer,func,funcFloor,level);
        //super();
    }
}