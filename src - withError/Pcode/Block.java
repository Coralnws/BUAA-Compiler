package Pcode;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Block extends PcodeGenerator {
    String funcName;

    public Block(String funcName) throws IOException {
        this.funcName = funcName;
        //currentWord应该是<Block>
        writer.write("start " + funcName);
        nextWord();
        if (currentWord.typeCode.equals("LBRACE") && isIf(funcName) || isElse(funcName) || funcName.equals("#while")) {
            writer.write("start #block");
        }
        //currentWord : {
        nextWord();

        while (currentWord.typeCode.equals("<BlockItem>")) {
            BlockItem blockItem = new BlockItem();
            //System.out.println("Check is <BlockItem> :" + currentWord.typeCode);
            //这边要确保是 ; 之后
        }

        if (currentWord.typeCode.equals("RBRACE")) {
            //System.out.println("Check now is } ,func end : "+ currentWord.typeCode);
            if (isIf(funcName) || isElse(funcName) || funcName.equals("#while")) {
                writer.write("end #block");
            }
            writer.write("end " + funcName);

            nextWord();
        }
    }

    public static boolean isIf(String str) {
        Pattern pattern = Pattern.compile("#if[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isElse(String str) {
        Pattern pattern = Pattern.compile("#else[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
