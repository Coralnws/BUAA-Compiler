package Pcode;

import Save.lexerWord;

import java.io.IOException;

import static Pcode.Exp.scanAhead;

public class UnaryPlusMinExp extends PcodeGenerator{
    public UnaryPlusMinExp() throws IOException {
        System.out.println("To UnaryPlusMinExp, check now is + / - :" + currentWord.content);
        pcode.append(currentWord.content);
        ignoreParser = true;
        nextWord();
        while (!currentWord.typeCode.equals("INTCON")) {
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
