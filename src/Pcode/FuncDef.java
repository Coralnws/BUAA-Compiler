package Pcode;

import java.io.IOException;

public class FuncDef extends PcodeGenerator{
    String funcName;

    public FuncDef() throws IOException {
        //currentWord应该是 int/void
        //System.out.println("Check current is int/void:" + currentWord.typeCode);
        pcode.append("func");
        //nextWord(); //int / void
        pcode.append(" " + currentWord.content);
        nextWord();  //funcName
        funcName = currentWord.content;
        //System.out.println("FuncName:" + currentWord.content );
        pcode.append(" " + currentWord.content);
        //func <type> <funcName>
        writer.write(String.valueOf(pcode));

        nextWord(); // (
        nextWord(); // <FuncFParams> or (
        //处理形参
        if(currentWord.typeCode.equals("<FuncFParams>")){
            nextWord(); // <FuncFParam>
            while(currentWord.typeCode.equals("<FuncFParam>")){
                FuncFParam funcFParam = new FuncFParam();
                //System.out.println("In FuncDef while loop:" + currentWord.typeCode);
            }
        }

        if(currentWord.typeCode.equals("RPARENT")){
            nextWord();
        }

        if(currentWord.typeCode.equals("<Block>")) {
            Block block = new Block(funcName);
            //nextWord();
            ////System.out.println("Check now block in funcDef is end : " + currentWord.typeCode);
        }
    }
}
