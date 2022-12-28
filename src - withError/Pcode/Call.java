package Pcode;

import java.io.IOException;

public class Call extends PcodeGenerator{
    String func;
    public Call() throws IOException {
        //System.out.println("To Call.");
        //currentWord是IDENFR
        ignoreParser = false;
        func = currentWord.content; //记录funcName
        //System.out.println("funcName: " + func);
        nextWord(); // (
        nextWord(); //<FuncRParams>
        if(currentWord.typeCode.equals("<FuncRParams>")){
            //System.out.println("有传参");
            nextWord();
            while(currentWord.typeCode.equals("<Exp>")){
                //System.out.println("一个新参数");
                ignoreParser = true;
                nextWord();
                if(CalltestExp()){
                    //System.out.println("参数是Exp");
                    Exp exp = new Exp();
                    pcode.append(" " + varT);
                }else{
                    writer.write("push " + currentWord.content);
                    pcode.delete( 0, pcode.length());
                    nextWord();
                }
                //pcode.append(" "+ currentWord.content);
                if(pcode.length() > 0 ){
                    writer.write("push" + pcode);
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
}
