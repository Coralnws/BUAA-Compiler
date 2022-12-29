package Pcode;

import Save.lexerWord;

import java.io.IOException;
import Error.Error;
import static Pcode.Exp.scanAhead;

public class CondExp extends PcodeGenerator{
    static int condNum = 1;
    public CondExp() throws IOException {
        //System.out.println("Check now is start of CondExp : " + currentWord.typeCode);

        pcode.append(getCondNum());
        /*
        if(lastOp != null){
            pcode.append(" " + lastOp.content);
        }

         */
        ignoreParser = true;
        while(!currentWord.typeCode.equals("CondEnd") && !currentWord.typeCode.equals("AND") && !currentWord.typeCode.equals("OR")) {
            if(condTestExp()){
                Exp exp = new Exp();
                pcode.append(" " + varT);
            }else{
                if(currentWord instanceof lexerWord){
                    pcode.append(" " + currentWord.content);
                    ignoreParser = true;
                    nextWord();
                }
            }
            ignoreParser = true;

        }
        if(currentWord.typeCode.equals("AND") || currentWord.typeCode.equals("OR")){
            pcode.append(" "+currentWord.content);
        }
        //System.out.println("CondExp end ,check now is && || ): " + currentWord.typeCode);
        writer.write(String.valueOf(pcode));
        ignoreParser = false;
    }

    public String getCondNum(){
        return "#cond" + condNum++;
    }

    public boolean condTestExp(){
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("LSS") && !scanWord.typeCode.equals("LEQ") && !scanWord.typeCode.equals("GRE")&& !scanWord.typeCode.equals("GEQ") && !scanWord.typeCode.equals("EQL") && !scanWord.typeCode.equals("NEQ")
        && !scanWord.typeCode.equals("CondEnd") && !scanWord.typeCode.equals("AND") && !scanWord.typeCode.equals("OR")) {
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
