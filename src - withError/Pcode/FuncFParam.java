package Pcode;
import Pcode.Symbol.*;
import Save.parserWord;


import java.io.IOException;
import java.util.Arrays;

import static Pcode.Exp.ExptestExp;
import Error.Error;
import Error.ErrorRecord;

//para - normal var or array , array才需要考虑ConstExp
public class FuncFParam extends PcodeGenerator{
    //形参要不就是
    char type;
    public FuncFParam() throws IOException {
        //v=var , a=array
        Symbol symb = null;
        pcode.append("para");
        if(currentWord.typeCode.equals("<FuncFParam>")){
            //System.out.println("currentWord.typeCode.equals(\"<FuncFParam>\")");
            nextWord(); //int
            nextWord(); //<name>
            if(!wordAhead.content.equals("[")){
                pcode.append(" v");
                type = 'v';
            } else{
                pcode.append(" a");
                type = 'a';
            }
            if(currentWord.typeCode.equals("IDENFR")){
                if(checkCurrentSymbExist(currentWord.content)){
                    System.out.println("vardef , gotError b");
                    Error error = new Error(currentWord.line,'b');
                    errorRecord.addError(error);
                }
                if(type == 'v'){
                    System.out.println("para is v");
                    symb = new paraVar(currentWord.content);
                }else if(type == 'a'){
                    symb = new paraArr(currentWord.content);

                }
                currentSymbTable.put(currentWord.content,symb);
                pcode.append(" " + currentWord.content);
                currentFunc.addPara((Para) symb);
            }
            paraArr arr = null;
            if(type == 'a'){
                arr = (paraArr) symb;
            }
            nextWord(); //如果是普通变量就是 , 数组是 [
            //如果是普通变量就可以停了，是数组才会有后面的事
            while(!wordAhead.typeCode.equals("<FuncFParam>") && !wordAhead.typeCode.equals("<Block>")){
                //ignoreParser = true;
                if(currentWord.typeCode.equals("LBRACK")){
                    pcode.append(" " + currentWord.content); // [
                    nextWord();
                }
                if(currentWord.typeCode.equals("RBRACK")){
                    pcode.append(" " + currentWord.content); // ]
                    nextWord();
                }

                if(currentWord.typeCode.equals("LBRACK")){ //如果是二维
                    //System.out.println("是二维");
                    pcode.append(" " + currentWord.content); // [
                    nextWord();  //<ConstExp>

                    while(currentWord instanceof parserWord){
                        //跳过ConstExp下面一大堆
                        nextWord();
                    }

                    if(FParamtestExp()){
                        //如果形参array里面有Exp
                        //System.out.println("形参array里面有Exp");
                        ConstExp constExp = new ConstExp();
                        pcode.append(" " + varT);
                        //System.out.println("append varT:" + varT);
                    }else{
                        if(currentWord.typeCode.equals("IDENFR")){
                            if(checkVarExist(currentWord.content) == null){
                                System.out.println("LVal , gotError c");
                                Error error = new Error(currentWord.line,'c');
                                errorRecord.addError(error);
                            }
                        }else{
                            if(currentWord.typeCode.equals("INTCON")){
                                arr.addDimension(Integer.parseInt(currentWord.content));
                                System.out.println("Check here paraDim, " + arr.dimension.size());
                            }

                        }
                        pcode.append(" " + currentWord.content); //var or num
                        nextWord(); // ]
                    }

                }
                //ignoreParser = false;
                //System.out.println("FuncFParam while loop : " + wordAhead.typeCode);
            }
        }
        writer.write(String.valueOf(pcode));
        nextWord();
        String[] sym;
        String code = pcode.toString();
        sym = code.split(" ");
        if(type == 'a'){
            if(sym.length > 5){
                System.out.println(Arrays.toString(sym));
                System.out.println("check here dimensionNum " + symb.name);
                symb.dimensionNum = 2;
            }else{
                symb.dimensionNum = 1;
            }
        }
    }

    public static boolean FParamtestExp(){
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("RBRACK")) {
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
            if(scanWord.typeCode.equals("LPARENT")){
                return true;
            }
            scanIndex ++;
            scanWord(scanIndex);
        }
        return false;
    }
}
