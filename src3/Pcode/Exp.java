package Pcode;

/*  要考虑好几种情况
*  1.纯数字 / 变量 calcExp
*  2.单 CallFunc
*  3.CallFunc calcExp
*  4.单 数组
*  5.数组 calcExp
*  6.混搭 calcExp
*/

import ParserAnalyse.UnaryOp;
import Save.lexerWord;
import Save.parserWord;

import java.io.IOException;

/** treeList
 *  1.数组 , parserWord停在 <UnaryExp> 接 funcName
 *  2.变量 , parserWord停在 <LVal> 接 varName
 *  3.除了中途有加减法的 ，都是constExp开始 , 然后 comma or ; 结束
 */
public class Exp extends PcodeGenerator{
    static String var_t;

    public Exp() throws IOException {
        //System.out.println("To Exp :" + currentWord.content);
        //如果符合 while 的停止条件,那么currentWord现在应该是 [  ） or  ,  or  ;  ]
        ignoreParser = true;
        while(!currentWord.typeCode.equals("SEMICN") && !currentWord.typeCode.equals("COMMA") &&!currentWord.typeCode.equals("RBRACE") && !currentWord.typeCode.equals("PARAEND") && !currentWord.typeCode.equals("PRINTEND")
                && !currentWord.typeCode.equals("LSS") && !currentWord.typeCode.equals("LEQ") && !currentWord.typeCode.equals("GRE")&& !currentWord.typeCode.equals("GEQ") && !currentWord.typeCode.equals("EQL") && !currentWord.typeCode.equals("NEQ")
                && !currentWord.typeCode.equals("CondEnd") && !currentWord.typeCode.equals("AND") && !currentWord.typeCode.equals("OR")){
            ignoreParser = true;
            if(scanAhead() == returnType.VAR){
                pcode.append(" " + currentWord.content);
                nextWord();
            }else if(scanAhead() == returnType.NUM){
                pcode.append(" " + currentWord.content);
                nextWord();
            }else if(scanAhead() == returnType.ARR){
                Array array = new Array();
                pcode.append(" "+ varT);
            }else if(scanAhead() == returnType.FUNC){
                Call callFunc = new Call();
                nextTVar();
                writer.write(varT + " = RET");
                pcode.append(" " + varT);
            }else if(scanAhead() == returnType.UnaryOp){
                //System.out.println("Retun UnaryOp, wordAhead is : "+ wordAhead.typeCode);
                nextWord();
                if(currentWord.typeCode.equals("LPARENT")){
                    //System.out.println("After ! is (Exp)");
                    //nextWord();
                    UnaryExp unaryExp = new UnaryExp();
                    String ans = varT;
                    nextTVar();
                    writer.write(varT + " = ! " + ans);
                    pcode.append(" " + varT);
                }else if(currentWord.typeCode.equals("PLUS") || currentWord.typeCode.equals("MINU")) {
                    //System.out.println("After ! is +-+");
                    //nextWord();
                    UnaryPlusMinExp unaryPlusMinExp = new UnaryPlusMinExp();
                    String ans = varT;
                    nextTVar();
                    writer.write(varT + " = ! " + ans);
                    pcode.append(" " + varT);
                }else if(currentWord.typeCode.equals("IDENFR")){
                    //nextWord();
                    if(wordAhead.typeCode.equals("LPARENT")){ //function
                        //System.out.println("!后面是Func");
                        Call callFunc = new Call();
                        nextTVar();
                        writer.write(varT + " = RET");
                        String ans = varT;
                        nextTVar();
                        writer.write(varT + " = ! " + ans);
                        pcode.append(" " + varT);
                    }else if(wordAhead.typeCode.equals("LBRACK")){
                        //System.out.println("!后面是Arr");
                        Array array = new Array();
                        String ans = varT;
                        nextTVar();
                        writer.write(varT + " = ! " + ans);
                        pcode.append(" "+ varT);
                    }else{
                        nextTVar();
                        writer.write(varT + " = ! " + currentWord.content);
                        pcode.append(" " + varT);
                        nextWord();
                    }
                }else{
                    //nextWord();
                    nextTVar();
                    writer.write(varT + " = ! " + currentWord.content);
                    pcode.append(" " + varT);
                    nextWord();
                }
            }else if(currentWord instanceof lexerWord){
                pcode.append(" "+ currentWord.content);
                nextWord();
            }else{
                nextWord();
            }
        }
        if(isMulti()){
            //System.out.println("isMulti");
            nextTVar();
            if(pcode.charAt(0) != ' '){
                pcode.insert(0," ");
            }
            writer.write(varT + " =" + pcode);
        }
       ignoreParser = false;
    }

    public static ExpType ExptestExp(){
        boolean hasFunc = false,hasExp = false,hasArray = false;
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWordAhead.typeCode.equals("<FuncFParam>") && !scanWordAhead.typeCode.equals("<Block>") && !scanWord.typeCode.equals("SEMICN")) {
            if(scanWord.typeCode.equals("IDENFR") && scanWordAhead.typeCode.equals("LPARENT")){
                hasFunc = true;
            }
            //hasExp
            if(scanWord.typeCode.equals("PLUS") || scanWord.typeCode.equals("MINU") || scanWord.typeCode.equals("MULT") || scanWord.typeCode.equals("DIV") || scanWord.typeCode.equals("MOD")){
                hasExp = true;
            }
            //hasArray
            if(scanWord.typeCode.equals("IDENFR") && scanWordAhead.typeCode.equals("LBRACK")){
                hasArray = true;
            }
            scanIndex++;
            scanWord(scanIndex);
        }

        if(hasExp && !hasFunc && !hasArray){
            return ExpType.NumVarExp;
        }
        if(hasExp && hasFunc && !hasArray){
            return ExpType.CallFuncExp;
        }
        if(hasExp && !hasFunc && hasArray){
            return ExpType.ArrayExp;
        }
        if(!hasExp && !hasFunc && hasArray){
            return ExpType.Array;
        }
        if(!hasExp && hasFunc && !hasArray){
            return ExpType.CallFunc;
        }
        if(hasExp && hasFunc && hasArray){
            return ExpType.MixExp;
        }
        return ExpType.NoExp;
    }

    public static returnType scanAhead(){
        ////System.out.println("scanAhead() : " + scan(index-1).typeCode);
        //用来看下一个word是什么
        scanWord(index-1);
        if(scanWord.typeCode.equals("INTCON")){
            //System.out.println("scanAhead() return NUM");
            return returnType.NUM;
        }
        if(scanWord.typeCode.equals("IDENFR")){
            scanWord(index);
            if(scanWord.typeCode.equals("LPARENT")){
                //System.out.println("scanAhead() return FUNC");
                return returnType.FUNC;
            }
            else if(scanWord.typeCode.equals("LBRACK")){
                //System.out.println("scanAhead() return ARR");
                return returnType.ARR;
            } else {
                //System.out.println("scanAhead() return VAR");
                return returnType.VAR;
            }
        }
        if(scanWord.typeCode.equals("NOT")){
            //System.out.println("scanAhead() return NOT");
            return returnType.UnaryOp;
        }
        if(scanWord.typeCode.equals("GETINTTK")){
            //System.out.println("scanAhead() return GETINT");
            return returnType.GETINT;
        }
        return returnType.ERROR;
    }
    boolean isMulti(){
        String str = pcode.toString();
        if(str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/") || str.contains("%") || str.contains("(") && str.contains(")")){
            return true;
        }
        else{
            return false;
        }
    }
}

/*  要考虑好几种情况
 *  1.纯数字 / 变量 calcExp
 *  2.单 Call Func
 *  3.CallFunc calcExp
 *  4.单 数组
 *  5.数组 calcExp
 *  6.混搭 calcExp
 */
enum ExpType{
    NumVarExp,
    CallFunc,
    CallFuncExp,
    Array,
    ArrayExp,
    MixExp,  //mean array+callFunc
    NoExp,
}
enum returnType{
    NUM, //全数字
    FUNC, //有函数
    ARR, //一般变量
    VAR,  //数组
    GETINT,
    ERROR,
    UnaryOp,
}