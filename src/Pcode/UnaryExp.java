package Pcode;

import ParserAnalyse.UnaryOp;
import Save.lexerWord;

import java.io.IOException;
import static Pcode.Exp.scanAhead;

public class UnaryExp extends PcodeGenerator{
    int Lbrac = 1,Rbrac = 0;
    public UnaryExp() throws IOException {
        System.out.println("To UnaryExp,check now is ( :" + currentWord.content);
        pcode.append(" " + currentWord.content);
        nextWord();
        //如果符合 while 的停止条件,那么currentWord现在应该是 [  ） or  ,  or  ;  ]
        ignoreParser = true;
        while(!currentWord.typeCode.equals("RPARENT") || ((Lbrac-Rbrac) != 0)){
            ignoreParser = true;
            if(scanAhead() == returnType.VAR){
                pcode.append(" " + currentWord.content);
                nextWord();
            }else if(scanAhead() == returnType.NUM){
                pcode.append(" " + currentWord.content);
                nextWord();
            }else if(scanAhead() == returnType.ARR){
                Array array = new Array();
                pcode.append(" " + varT);
            }else if(scanAhead() == returnType.FUNC){
                Call callFunc = new Call();
                nextTVar();
                writer.write(varT + " = RET");
                pcode.append(" " + varT);
            }else if(scanAhead() == returnType.UnaryOp){
                System.out.println("Retun UnaryOp, wordAhead is : "+ wordAhead.typeCode);
                nextWord();
                if(currentWord.typeCode.equals("LPARENT")){
                    System.out.println("After ! is (Exp) or +-+");
                    //nextWord();
                    UnaryExp unaryExp = new UnaryExp();
                    String ans = varT;
                    nextTVar();
                    writer.write(varT + " = !" + ans);
                    pcode.append(" " + varT);
                }else if(currentWord.typeCode.equals("PLUS") || currentWord.typeCode.equals("MINU")) {
                    //nextWord();
                    UnaryPlusMinExp unaryPlusMinExp = new UnaryPlusMinExp();
                    String ans = varT;
                    nextTVar();
                    writer.write(varT + " = !" + ans);
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
                        writer.write(varT + " = !" + ans);
                        pcode.append(" " + varT);
                    }else if(wordAhead.typeCode.equals("LBRACK")){
                        System.out.println("!后面是Arr");
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
                    writer.write(varT + " = !" + currentWord.content);
                    pcode.append(" " + varT);
                    nextWord();
                }
            }else if(currentWord.typeCode.equals("LPARENT")){
                Lbrac++;
                pcode.append(" "+ currentWord.content);
                nextWord();
            }else if(currentWord.typeCode.equals("RPARENT")){
                Rbrac++;
                pcode.append(" "+ currentWord.content);
                System.out.println("Now Lbrac is : "+Lbrac + "; Rbrac is " + Rbrac);
                if(Lbrac==Rbrac){
                    break;
                }
                nextWord();
            } else if(currentWord instanceof lexerWord){
                pcode.append(" "+ currentWord.content);
                nextWord();
            }else{
                nextWord();
            }
        }
        if(isMulti()){
            System.out.println("isMulti");
            nextTVar();
            writer.write(varT + " =" + pcode);
        }
        ignoreParser = false;
    }
    boolean isMulti(){
        String str = pcode.toString();
        if(str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/") || str.contains("%") || (str.contains("(")&&str.contains(")"))){
            return true;
        }
        else{
            return false;
        }
    }

}
