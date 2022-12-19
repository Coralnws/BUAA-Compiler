package Pcode;

import java.io.IOException;

/**
 * <BlockItem>
 * <Stmt>
 * if
 * (
 * <Cond>
 * b
 * >=
 * a
 * +
 */
public class Cond extends PcodeGenerator{
    public Cond() throws IOException {
        //System.out.println("Cond , check now is <Cond>:" + currentWord.typeCode);
        pcode.append("cond");
        ignoreParser = true;
        nextWord();

        //System.out.println("Check now is start of Exp :" + currentWord.typeCode);
        CondExp.condNum = 1;
        while(!currentWord.typeCode.equals("CondEnd")){
            CondExp condExp = new CondExp();
            ignoreParser = true;
            nextCondT();
            pcode.append(" " + condT);
            if(!currentWord.typeCode.equals("CondEnd")){
                pcode.append(" " + currentWord.content);
                nextWord();
            }
        }
        ignoreParser = false;
        writer.write(String.valueOf(pcode));
        writer.write("CheckCond");
        //System.out.println("In Cond,Check now is CondEnd :" + currentWord.typeCode);
        nextWord(); //<Stmt>
        //System.out.println("In Cond,Check now is <Stmt> :" + currentWord.typeCode);
        reCondNum();

    }
}
