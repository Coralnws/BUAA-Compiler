package Pcode;

import java.io.IOException;
import Error.Error;
import Error.ErrorRecord;
/**
 * <BlockItem>
 * <Stmt>
 * return
 * <Exp>
 */

public class Return extends PcodeGenerator{
    boolean retErr = false;
    public Return() throws IOException {
        hasRet = true;
        System.out.println("Pcode Return:" + currentWord.typeCode);
        pcode.append("ret");  //currentWord = return

        ignoreParser = true;
        nextWord();   //跳过<Exp>
        System.out.println("Check now is Exp or ; : " + currentWord.typeCode);

        if(currentFunc.type == 0){  //不应该有返回值但有
            if(!currentWord.typeCode.equals("SEMICN")) {
                Error error = new Error(currentWord.line,'f');
                errorRecord.addError(error);
            }
        }else if(currentFunc.type == 1){  //应该有返回值但没有
            if(currentWord.typeCode.equals("SEMICN")) {
                Error error = new Error(currentWord.line,'g');
                errorRecord.addError(error);
                //retErr = true;
            }
        }

        /*

            writer.write("ret");
        }

         */
        while(!currentWord.typeCode.equals("SEMICN")){
            if(!BlockItemTestExp()){
                System.out.println("return值没有Exp");
                while(!currentWord.typeCode.equals("SEMICN")){
                    System.out.println("append into pcode: " + currentWord.typeCode);
                    pcode.append(" " + currentWord.content);
                    nextWord();
                }
            }
            else{
                System.out.println("return值有Exp");
                Exp exp = new Exp();
                pcode.append(" " + varT);
            }
        }
        writer.write(String.valueOf(pcode));
        nextWord();
        if(retErr){
            Error error = new Error(currentWord.line,'g');
            errorRecord.addError(error);
        }
        System.out.println("Check return end word:" + currentWord.typeCode);
        ignoreParser = false;
    }

    public static boolean BlockItemTestExp(){
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("SEMICN")) {
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
