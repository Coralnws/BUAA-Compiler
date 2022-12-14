package Pcode;

import java.io.IOException;

/**
 * <BlockItem>
 * <Stmt>
 * return
 * <Exp>
 */

public class Return extends PcodeGenerator{
    public Return() throws IOException {
        System.out.println("Pcode Return:" + currentWord.typeCode);
        pcode.append("ret");  //currentWord = return

        ignoreParser = true;
        nextWord();   //跳过<Exp>
        System.out.println("Check now is Exp or ; : " + currentWord.typeCode);

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

        ignoreParser = false;
        writer.write(String.valueOf(pcode));
        nextWord();
        System.out.println("Before return end ,check now is : "+ currentWord.typeCode);

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
