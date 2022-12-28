package Pcode;

import Save.lexerWord;
import Save.parserWord;
import Error.Error;
import Error.ErrorRecord;
import java.io.IOException;

import static Pcode.Exp.scanAhead;
import static Pcode.Exp.var_t;

//for array里面的Exp
public class ConstExp extends PcodeGenerator{
    public ConstExp() throws IOException {
        System.out.println("In ConstExp");
        ignoreParser = true;
        while(!currentWord.typeCode.equals("RBRACK")){
            ignoreParser = true;
            if(scanAhead() == returnType.VAR){
                if(checkVarExist(currentWord.content) == null){
                    System.out.println("LVal , gotError c");
                    Error error = new Error(currentWord.line,'c');
                    errorRecord.addError(error);
                }
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
                System.out.println("Retun UnaryOp, wordAhead is : "+ wordAhead.typeCode);
                nextWord();
                if(currentWord.typeCode.equals("LPARENT")){
                    System.out.println("After ! is (Exp)");
                    //nextWord();
                    UnaryExp unaryExp = new UnaryExp();
                    String ans = varT;
                    nextTVar();
                    writer.write(varT + " = ! " + ans);
                    pcode.append(" " + varT);
                }else if(currentWord.typeCode.equals("PLUS") || currentWord.typeCode.equals("MINU")) {
                    System.out.println("After ! is +-+");
                    //nextWord();
                    UnaryPlusMinExp unaryPlusMinExp = new UnaryPlusMinExp();
                    String ans = varT;
                    nextTVar();
                    writer.write(varT + " = ! " + ans);
                    pcode.append(" " + varT);
                }else if(currentWord.typeCode.equals("IDENFR")){
                    //nextWord();
                    if(wordAhead.typeCode.equals("LPARENT")){ //function
                        System.out.println("!后面是Func");
                        Call callFunc = new Call();
                        nextTVar();
                        writer.write(varT + " = RET");
                        String ans = varT;
                        nextTVar();
                        writer.write(varT + " = ! " + ans);
                        pcode.append(" " + varT);
                    }else if(wordAhead.typeCode.equals("LBRACK")){
                        System.out.println("!后面是Arr");
                        Array array = new Array();
                        String ans = varT;
                        nextTVar();
                        writer.write(varT + " = ! " + ans);
                        pcode.append(" "+ varT);
                    }else{
                        if(checkVarExist(currentWord.content) == null){
                            System.out.println("LVal , gotError c");
                            Error error = new Error(currentWord.line,'c');
                            errorRecord.addError(error);
                        }
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
            nextTVar();
            if(pcode.charAt(0) != ' '){
                pcode.insert(0," ");
            }
            writer.write(varT + " =" + pcode);
        }
        ignoreParser = false;
    }

    boolean isMulti(){
        String str = pcode.toString();
        if(str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/") || str.contains("%")){
            return true;
        }
        else{
            return false;
        }
    }
}
