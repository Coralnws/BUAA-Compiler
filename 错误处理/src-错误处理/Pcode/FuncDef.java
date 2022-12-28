package Pcode;

import java.io.IOException;
import java.util.HashMap;

import Error.Error;
import Error.ErrorRecord;
import Pcode.Symbol.Func;
import Pcode.Symbol.Symbol;
import Save.SaveContent;
import Save.Word;

public class FuncDef extends PcodeGenerator{
    String funcName;
    int type;
    public FuncDef() throws IOException {
        //currentWord应该是 int/void
        System.out.println("Check current is int/void:" + currentWord.typeCode);
        pcode.append("func");
        //nextWord(); //int / void
        if(currentWord.content.equals("void")){
            type = 0;
        }else{
            type =1;
        }
        pcode.append(" " + currentWord.content);
        nextWord();  //funcName
        funcName = currentWord.content;
        System.out.println("FuncName:" + currentWord.content );
        pcode.append(" " + currentWord.content);
        //func <type> <funcName>
        writer.write(String.valueOf(pcode));

        if(funcList.containsKey(funcName)){
            Error error = new Error(currentWord.line,'b');
            errorRecord.addError(error);
            //}else{
        }
        //为了继续做
        Func func = new Func(funcName,type);
        currentFunc = func;
        System.out.println("DEBUG,CHECK FUNC:" + currentFunc.name);
        HashMap<String, Symbol> symbList = new HashMap<String, Symbol>();
        int floor = symbTable.size();
        //symbTable.put(floor,symbList);
        currentSymbTable = symbList;
        funcList.put(funcName,func);
        System.out.println("funcList size:" + funcList.size());

        nextWord(); // (
        nextWord(); // <FuncFParams> or (
        //处理形参
        if(currentWord.typeCode.equals("<FuncFParams>")){
            nextWord(); // <FuncFParam>
            while(currentWord.typeCode.equals("<FuncFParam>")){
                FuncFParam funcFParam = new FuncFParam();
                System.out.println("In FuncDef while loop:" + currentWord.typeCode);
            }
        }

        if(currentWord.typeCode.equals("RPARENT")){
            nextWord();
        }

        if(currentWord.typeCode.equals("<Block>")) {
            Block block = new Block(funcName);
            //nextWord();

            //System.out.println("Check what after block:" + currentWord.typeCode);
            if(currentFunc.type == 1 && !hasRet){
                int index1 = index-2;
                Word word = SaveContent.getWord(index-2);
                while(!word.typeCode.equals("RBRACE")){
                    index++;
                    word = SaveContent.getWord(index-2);
                }
                System.out.println("Check now block in funcDef is end : " + word.typeCode);
                Error error = new Error(word.line,'g');
                errorRecord.addError(error);
            }else{
                hasRet = false;
            }
        }
    }
}
