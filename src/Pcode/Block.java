package Pcode;

import java.io.IOException;

public class Block extends PcodeGenerator{
    String funcName;
    public Block(String funcName) throws IOException {
        this.funcName = funcName;
        //currentWord应该是<Block>
        writer.write("start " + funcName);
        nextWord();
        //currentWord : {
        nextWord();

        while(currentWord.typeCode.equals("<BlockItem>")){
            BlockItem blockItem = new BlockItem();
            //System.out.println("Check is <BlockItem> :" + currentWord.typeCode);
            //这边要确保是 ; 之后
        }

        if(currentWord.typeCode.equals("RBRACE")){
            //System.out.println("Check now is } ,func end : "+ currentWord.typeCode);
            writer.write("end " + funcName);
            nextWord();
        }
    }
}
