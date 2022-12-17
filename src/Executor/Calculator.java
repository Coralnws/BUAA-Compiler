package Executor;

import java.sql.SQLSyntaxErrorException;
import java.util.Stack;

public class Calculator {
    Stack<Operator> opStack = new Stack<Operator>();
    Stack<Operand> numStack = new Stack<Operand>();
    int index = 0;
    String exp;
    char ch;
    Object prevPt;


    public Calculator(String exp){
        this.exp = exp;
        //calc();
    }

    public int calc(){
        nextChar();
        while(ch != '#'){
            if(prevPt != null){
                ////System.out.println("prevPt = "+ prevPt);
            }
            if(ch == '-'){
                ////System.out.println("处理 -");
                if(prevPt == null){  //第一个 ,是正负类型
                    ////System.out.println("prevPt = null");
                    Operator neg = new Operator(ch,opType.signNEG);
                    opStack.push(neg);
                    prevPt = neg;
                }else if(prevPt instanceof Operand){  //前面是数字, 做算数 ,看操作栈有没有
                    ////System.out.println("prevPt = operator");
                    Operator minu = new Operator(ch,opType.opMINU);
                    if(opNotEmpty()){ //前面有其他操作符或者（ ,应该没有正负类型
                        checkOpAndPush(minu);
                    }else{
                        opStack.push(minu);  //如果是空的，直接入栈
                        prevPt = minu;
                    }
                }else if(prevPt instanceof Operator){ //前面是操作符 ，要判断是正负数还是做算术的
                    ////System.out.println("prevPt = operator");
                    if(isOp(((Operator) prevPt).type) || ((Operator) prevPt).content == '('){
                        //前面是操作符，那么current就是正负类型
                        Operator neg = new Operator(ch,opType.signNEG);
                        opStack.push(neg);
                        prevPt = neg;
                    }else if(isSign(((Operator) prevPt).type)){
                        combSign();
                    }else if(((Operator) prevPt).content == ')'){
                        Operator minu = new Operator(ch,opType.opMINU);
                        opStack.push(minu);
                        prevPt = minu;
                    }
                }
                nextChar();
            }
            else if(ch == '+'){
                ////System.out.println("处理 +");
                if(prevPt == null){  //第一个 ,是正负类型
                    ////System.out.println("prevPt = null");
                    Operator pos = new Operator(ch,opType.signPOS);
                    opStack.push(pos);
                    prevPt = pos;
                }else if(prevPt instanceof Operand){  //前面是数字, 做算数 ,看操作栈有没有
                    ////System.out.println("prevPt = number");
                    Operator plus = new Operator(ch,opType.opPLUS);
                    if(opNotEmpty()){ //前面有其他操作符或者（ ,应该没有正负类型
                        checkOpAndPush(plus);
                    }else{
                        opStack.push(plus);  //如果是空的，直接入栈
                        prevPt = plus;
                    }
                }else if(prevPt instanceof Operator){ //前面是操作符 ，要判断是正负数还是做算术的
                    ////System.out.println("prevPt = operator");
                    if(isOp(((Operator) prevPt).type) || ((Operator) prevPt).content == '('){
                        //前面是操作符，那么current就是正负类型
                        Operator pos = new Operator(ch,opType.signPOS);
                        opStack.push(pos);
                        prevPt = pos;
                    }else if(isSign(((Operator) prevPt).type)){
                        combSign();
                    }else if(((Operator) prevPt).content == ')'){
                        Operator plus = new Operator(ch,opType.opPLUS);
                        opStack.push(plus);
                        prevPt = plus;
                    }
                }
                nextChar();
            }
            else if(ch == '*'){
                if(prevPt == null){  //第一个 ,是正负类型
                    //error
                }else if(prevPt instanceof Operand){  //前面是数字, 做算数 ,看操作栈有没有
                    Operator mul = new Operator(ch,opType.opMUL);
                    if(opNotEmpty()){ //前面有其他操作符或者（ ,应该没有正负类型
                        checkOpAndPush(mul);
                    }else{
                        opStack.push(mul);  //如果是空的，直接入栈
                        prevPt = mul;
                    }
                }else if(prevPt instanceof Operator){  //只有可能是 ）
                    if(((Operator) prevPt).content == ')'){
                        Operator mul = new Operator(ch,opType.opMUL);
                        opStack.push(mul);
                        prevPt = mul;
                    }else{
                        //error
                        ////System.out.println("* error1");
                    }
                }else{
                    //error
                    ////System.out.println("* error2");
                }
                nextChar();
            }
            else if(ch == '/'){
                if(prevPt == null){  //第一个 ,是正负类型
                    //error
                }else if(prevPt instanceof Operand){  //前面是数字, 做算数 ,看操作栈有没有
                    Operator div = new Operator(ch,opType.opDIV);
                    if(opNotEmpty()){ //前面有其他操作符或者（ ,应该没有正负类型
                        checkOpAndPush(div);
                    }else{
                        opStack.push(div);  //如果是空的，直接入栈
                        prevPt = div;
                    }
                }else if(prevPt instanceof Operator){  //只有可能是 ）
                    if(((Operator) prevPt).content == ')'){
                        Operator div = new Operator(ch,opType.opDIV);
                        opStack.push(div);
                        prevPt = div;
                    }else{
                        //error
                        ////System.out.println("/ error1");
                    }
                }else{
                    //error
                    ////System.out.println("/ error2");
                }
                nextChar();
            }
            else if(ch == '%'){
                ////System.out.println("处理 %");
                if(prevPt == null){  //第一个 ,是正负类型
                    //error
                }else if(prevPt instanceof Operand){  //前面是数字, 做算数 ,看操作栈有没有
                    Operator mod = new Operator(ch,opType.opMOD);
                    if(opNotEmpty()){ //前面有其他操作符或者（ ,应该没有正负类型
                        checkOpAndPush(mod);
                    }else{
                        opStack.push(mod);  //如果是空的，直接入栈
                        prevPt = mod;
                    }
                }else if(prevPt instanceof Operator){  //只有可能是 ）
                    if(((Operator) prevPt).content == ')'){
                        Operator mod = new Operator(ch,opType.opMOD);
                        opStack.push(mod);
                        prevPt = mod;
                    }else{
                        //error
                        ////System.out.println("% error1");
                    }
                }else{
                    //error
                    ////System.out.println("% error2");
                }
                nextChar();
            }
            else if(isNumeric(ch)){
                saveNum();
            }
            else if(ch == '('){
                Operator lbrac = new Operator(ch,opType.opLBRAC);
                opStack.push(lbrac);
                prevPt = lbrac;
                nextChar();
            }
            else if(ch == ')'){
                while(getOp().type != opType.opLBRAC){
                    getOp();
                }
                opStack.pop();
                numSign();
                prevPt = numStack.peek();
                nextChar();
            }
            ////System.out.println("---------------Check this round-------------------");
            printOpStack();
            printNumStack();
            ////System.out.println("-------------- next round , ch = " + ch +"-------------------");
        }

        if(opNotEmpty()){
            ////System.out.println("opStack is not empty");
            printOpStack();
            while(opNotEmpty()){
                getOp();
            }
        }

        ////System.out.println("Check size of numStack : "+numStack.size());
        int result = numStack.pop().value;
        ////System.out.println("Result : " +  result);

        return result;
    }

    void printOpStack(){
        for(int i=0;i<opStack.size();i++){
            ////System.out.print(opStack.get(i).content + " ");
        }
        ////System.out.println();
    }

    void printNumStack(){
        for(int i=0;i<numStack.size();i++){
            ////System.out.print(numStack.get(i).value + " ");
        }
        ////System.out.println();
    }


    void checkOpAndPush(Operator current){
        ////System.out.println("checkOpAndPush()");
        Operator peek = opStack.peek();
        if(peek.content == '('){
            opStack.push(current);
        }
        if(isOp(peek.type) && isOp(current.type)){
            if(getPrior(peek.content) >= getPrior(ch)) {  //一般算术符类型
                getOp();  //最后一定导向 opCalc
            }
            ////System.out.println("push opStack :" + current.content);
            opStack.push(current);
        }
        if(isSign(peek.type) && isSign(current.type)){  //+-+类型
            combSign();
        }
        prevPt = current;
    }


    Operator getOp(){ //检查OpStack栈顶的是什么类型，操作符类型就调用opCalc,正数负数的话调用negPosCalc，（ 的话什么都不做，这个函数返回做操作的op
        Operator op = null;
        op = opStack.peek();
        if(isOp(op.type)){
            opCalc();
        }else if(isSign(op.type)){
            signCalc();
        }
        return op;
    }

    void opCalc(){
        Operator op = opStack.pop();
        Operand num1 = numStack.pop();
        Operand num2 = numStack.pop();
        Operand res = new Operand();
        switch(op.content){
            case '*':
                res.setValue(num2.value * num1.value);
                break;
            case '/':
                res.setValue(num2.value / num1.value);
                break;
            case '%':
                res.setValue(num2.value % num1.value);
                ////System.out.println("% , result = " + res.value);
                break;
            case '-':
                res.setValue(num2.value - num1.value);
                break;
            case '+':
                res.setValue(num2.value + num1.value);
                break;
        }
        numStack.push(res);
    }

    void signCalc(){
        Operator sign = opStack.pop();
        Operand num = numStack.peek();
        if(sign.type == opType.signNEG){
            num.setValue(0-num.value);
        }else if(sign.type == opType.signPOS){
            num.setValue(0+num.value);
        }
    }

    boolean opNotEmpty(){
        return opStack.size() > 0 ;
    }

    boolean numNotEmpty(){
        return numStack.size() > 0;
    }

    boolean isOp(opType type){
        if(type == opType.opPLUS || type == opType.opMINU || type == opType.opMUL || type == opType.opDIV ||type == opType.opMOD){
            return true;
        }else{
            return false;
        }
    }

    boolean isSign(opType type){
        if(type == opType.signNEG || type == opType.signPOS){
            return true;
        }else{
            return false;
        }
    }

    int getPrior(char oper){
        if(oper == '*' || oper == '/' || oper == '%'){ //优先级最高的
            return 1;
        }else if(oper == '+' || oper == '-'){
            return 0;
        }else {
            return -1;  //错误的运算符
        }
    }


    int saveNum(){  //遇到数字之后可调用,直接读取当前的整个数字串，并且判断有没有符号
        String num = "";
        while(isNumeric(ch)){
            num += ch;
            nextChar();
        }
        int res = Integer.parseInt(num);
        Operand operand = new Operand();
        operand.setValue(res);
        numStack.push(operand);
        prevPt = operand;
        numSign();
        return res;
    }

    void numSign(){
        if(opNotEmpty() && isSign(opStack.peek().type)){
            signCalc();
        }
    }

    void combSign(){
        ////System.out.println("前后都是 + - ,combSign");
        if(opNotEmpty()){
            Operator sign = opStack.peek();
            if(isSign(sign.type)){
                if(sign.content != ch){
                    ////System.out.println("- 进栈 ,当前opStack size:" + opStack.size());
                    opStack.pop();
                    Operator newSign = new Operator('-',opType.signNEG);
                    opStack.push(newSign);
                    prevPt = newSign;
                }else if(sign.content == ch){
                    ////System.out.println("+ 进栈,当前opStack size:" + opStack.size());
                    opStack.pop();
                    Operator newSign = new Operator('+',opType.signPOS);
                    opStack.push(newSign);
                    prevPt = newSign;
                }
            }
        }
    }

    void nextChar(){
        if(index<exp.length()){
            ch = exp.charAt(index++);
            while(ch==' '){
                ch = exp.charAt(index++);
            }
        }else{
            ch = '#';
        }
        //////System.out.println("ch:" + ch);
    }

    boolean isNumeric(char ch){
        if(ch >= '0' && ch <= '9'){
            return true;
        }
        else
            return false;
    }
}

class Operand{
    int value;

    public void setValue(int value){
        this.value = value;
    }
}

class Operator{
    char content;
    opType type;  //opType 是一个enum

    public Operator(char ch,opType type){
        this.content = ch;
        this.type = type;
    }
}

enum opType{
    opPLUS,
    opMINU,
    opMUL,
    opDIV,
    opMOD,
    opLBRAC,
    opRBRAC,
    signPOS, //正数
    signNEG, //负数
}

class Tester{
    public static void main(String[] args) {
        String exp = "+-+12 + -(-(3*4)) + 4";
        Calculator cal = new Calculator(exp);
    }

}