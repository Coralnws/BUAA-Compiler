package Pcode;

import Save.lexerWord;

import java.io.IOException;
import  Error.Error;
import static Pcode.Exp.scanAhead;

public class UnaryPlusMinExp extends PcodeGenerator{
    public UnaryPlusMinExp() throws IOException {
        //System.out.println("To UnaryPlusMinExp, check now is + / - :" + currentWord.content);
        pcode.append(currentWord.content);
        ignoreParser = true;
        nextWord();
        while (!currentWord.typeCode.equals("INTCON")) {
            if(currentWord.typeCode.equals("IDENFR") && checkVarExist(currentWord.content) == null){
                System.out.println("LVal , gotError c");
                Error error = new Error(currentWord.line,'c');
                errorRecord.addError(error);
            }
            pcode.append(" " + currentWord.content);
            nextWord();
        }
        pcode.append(" "+currentWord.content);
        nextTVar();
        if(pcode.charAt(0) != ' '){
            pcode.insert(0," ");
        }
        writer.write(varT + " =" + pcode);
        ignoreParser = false;
    }
}
