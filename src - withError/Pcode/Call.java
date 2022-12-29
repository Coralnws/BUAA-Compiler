package Pcode;

import java.io.IOException;
import java.util.Arrays;

import Error.Error;
import Error.ErrorRecord;
import Pcode.Symbol.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class Call extends PcodeGenerator{
    String func;
    int paraNum = 0;
    public Call() throws IOException {
        System.out.println("To Call.");
        //currentWord是IDENFR
        ignoreParser = false;
        func = currentWord.content; //记录funcName

        System.out.println("funcName: " + func);
        nextWord(); // (
        nextWord(); //<FuncRParams>
        Symbol symb = null;
        Func function = getFunc(func);
        if(function == null){
            System.out.println("callFunc , gotError c ,line:" + currentWord.line);
            Error error = new Error(currentWord.line,'c');
            errorRecord.addError(error);
        }
        if(currentWord.typeCode.equals("<FuncRParams>")){
            System.out.println("有传参");
            nextWord();
            while(currentWord.typeCode.equals("<Exp>")){
                System.out.println("一个新参数");
                ignoreParser = true;
                nextWord();
                if(CalltestExp()){
                    System.out.println("Check here para is func");
                    if(currentWord.typeCode.equals("IDENFR") && wordAhead.typeCode.equals("LPARENT")){
                        System.out.println("Check here para is func");
                        Func func = funcList.get(currentWord.content);
                        if(func.type == 0){
                            Error error = new Error(currentWord.line,'e');
                            errorRecord.addError(error);
                        }
                    }

                    System.out.println("参数是Exp");
                    currentExp = "";
                    Exp exp = new Exp();
                    pcode.append(" " + varT);

                }else{
                    if(currentWord.typeCode.equals("IDENFR")){  //如果是单变量
                        System.out.println("error:单变量");
                        symb = checkVarExist(currentWord.content);
                        if(symb!=null && symb instanceof Variable){
                            if(function.getSpefPara(paraNum) != null){
                                if(!(function.getSpefPara(paraNum) instanceof paraVar)){
                                    System.out.println("应该是是var变量");
                                    Error error = new Error(currentWord.line,'e');
                                    errorRecord.addError(error);
                                }
                            }
                        }else if(symb!=null && symb instanceof Arr){
                            System.out.println("是arr变量");
                            System.out.println("检查dimension,传过去的dimension:"+symb.dimensionNum + " 参数的dimension:"+function.getSpefPara(paraNum).dimensionNum);
                            if(function.getSpefPara(paraNum) != null) {
                                if (!(function.getSpefPara(paraNum) instanceof paraArr)) {
                                    Error error = new Error(currentWord.line, 'e');
                                    errorRecord.addError(error);
                                } else if (symb.dimensionNum != function.getSpefPara(paraNum).dimensionNum) {  //因为这个是直接整个传，所以dimension应该一样
                                    System.out.println("debug here: ,symb dimension = "+ function.getSpefPara(paraNum).dimensionNum);
                                    Error error = new Error(currentWord.line, 'e');
                                    errorRecord.addError(error);
                                }else{
                                    System.out.println("传整个二维数组过去");
                                    if (function.getSpefPara(paraNum) instanceof paraArr){
                                        if(function.getSpefPara(paraNum).dimension.size() >1){
                                            int paraDim = function.getSpefPara(paraNum).dimension.get(1);
                                            if(symb.dimension.get(1) != paraDim){
                                                Error error = new Error(currentWord.line, 'e');
                                                errorRecord.addError(error);
                                            }
                                        }
                                        //System.out.println("Check here paraDim... " + function.getSpefPara(paraNum).dimension.size());
                                    }
                                }
                            }
                        }else if(symb == null){
                            Error error = new Error(currentWord.line, 'c');
                            errorRecord.addError(error);
                        }
                    }else if(currentWord.typeCode.equals("INTCON")){  //如果是数量
                        if(function.getSpefPara(paraNum) != null) {
                            if (!(function.getSpefPara(paraNum) instanceof paraVar)) {
                                Error error = new Error(currentWord.line, 'e');
                                errorRecord.addError(error);
                            }
                        }
                    }
                    writer.write("push " + currentWord.content);
                    paraNum++;
                    pcode.delete( 0, pcode.length());
                    nextWord();
                }
                //pcode.append(" "+ currentWord.content);
                if(pcode.length() > 0 ){  //a [ ] [ ] 或者 其他exp
                    if(ErrorTestExp()){
                        if(function.getSpefPara(paraNum) != null) {
                            if (!(function.getSpefPara(paraNum) instanceof paraVar)) {
                                Error error = new Error(currentWord.line, 'e');
                                errorRecord.addError(error);
                            }
                        }
                    }else{
                        System.out.println("Check is arr : " + pcode);
                        if(currentExp.length()>1){
                            currentExp = currentExp.substring(1);
                        }

                        String sym[] = currentExp.split(" ");
                        System.out.println(Arrays.toString(sym));
                        symb = checkVarExist(sym[0]);
                        int dimensionNum=0;
                        /*
                        if(function.getSpefPara(paraNum) != null) {
                            if (function.getSpefPara(paraNum) instanceof paraVar) {
                                Error error = new Error(currentWord.line, 'e');
                                errorRecord.addError(error);
                            }
                        }

                         */
                        if(sym.length>4){  //two dimension  如果这边的dimension和自己原本的dimension一样，那就是Int，para那边应该是paraVar，反之如果少过自己原本的dimensionNUm,那么para那边的dimension应该和这个一样
                            dimensionNum = 2;
                            if(symb != null && symb.dimensionNum == dimensionNum){
                                System.out.println("Check here ...");
                                if(function.getSpefPara(paraNum) != null) {
                                    System.out.println(function.getSpefPara(paraNum).getClass());
                                    if (!(function.getSpefPara(paraNum) instanceof paraVar)) {
                                        System.out.println("Check here !!");
                                        Error error = new Error(currentWord.line, 'e');
                                        errorRecord.addError(error);
                                    }
                                }
                            }else if(symb != null && symb.dimensionNum > dimensionNum){
                                if(function.getSpefPara(paraNum) != null) {
                                    if (!(function.getSpefPara(paraNum) instanceof paraArr)) {
                                        Error error = new Error(currentWord.line, 'e');
                                        errorRecord.addError(error);
                                    }else if(function.getSpefPara(paraNum).dimensionNum != dimensionNum){
                                        Error error = new Error(currentWord.line, 'e');
                                        errorRecord.addError(error);
                                    }
                                }
                            }
                        }else{
                            dimensionNum = 1;
                            if(symb != null && symb.dimensionNum == dimensionNum){
                                System.out.println("Check here ...");
                                if(function.getSpefPara(paraNum) != null) {
                                    if (!(function.getSpefPara(paraNum) instanceof paraVar)) {
                                        Error error = new Error(currentWord.line, 'e');
                                        errorRecord.addError(error);
                                    }
                                }
                            }else if(symb != null && symb.dimensionNum > dimensionNum){
                                if(function.getSpefPara(paraNum) != null) {
                                    if (!(function.getSpefPara(paraNum) instanceof paraArr)) {
                                        Error error = new Error(currentWord.line, 'e');
                                        errorRecord.addError(error);
                                    }else if(function.getSpefPara(paraNum).dimensionNum != dimensionNum){
                                        Error error = new Error(currentWord.line, 'e');
                                        errorRecord.addError(error);
                                    }
                                }
                            }
                        }
                    }

                    writer.write("push" + pcode);
                    paraNum++;
                    pcode.delete( 0, pcode.length());
                }
                ignoreParser = false;  //这时候应该是 , 或者 ）
                if(currentWord.typeCode.equals("COMMA")){
                    nextWord();
                }else if (currentWord.typeCode.equals("RPARENT")){
                    break;
                }
            }
        }

        if(function != null && function.paraNum != paraNum) {
            System.out.println("参数数量不符合,函数:" + function.paraNum + " 传参:" + paraNum);
            Error error = new Error(currentWord.line,'d');
            errorRecord.addError(error);
        }

        if(currentWord.typeCode.equals("PARAEND")){
            writer.write("call " + func);
            nextWord();
        }
    }
    public static boolean CalltestExp(){
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("COMMA") && !scanWord.typeCode.equals("PARAEND") ) {
            //hasFunc
            if(scanWord.typeCode.equals("IDENFR") && scanWordAhead.typeCode.equals("LPARENT")){
                return true;
            }
            //hasExp
            if(scanWord.typeCode.equals("PLUS") || scanWord.typeCode.equals("MINU") || scanWord.typeCode.equals("MULT") || scanWord.typeCode.equals("DIV") || scanWord.typeCode.equals("MOD") || scanWord.typeCode.equals("NOT")){
                return true;
            }
            //hasArray
            if(scanWord.typeCode.equals("IDENFR") && scanWordAhead.typeCode.equals("LBRACK")){
                return true;
            }
            scanIndex ++;
            scanWord(scanIndex);
        }
        return false;
    }
    public boolean ErrorTestExp(){
        if(currentExp.contains("+") || currentExp.contains("-") || currentExp.contains("*") || currentExp.contains("/") || currentExp.contains("%") || currentExp.contains("!")){
            return true;
        }
        return false;
    }

    public boolean ErrorTestArr(){
        if(!ErrorTestExp() &&  currentExp.contains("(") && currentExp.contains("(")){
            return true;
        }
        return false;
    }

}
