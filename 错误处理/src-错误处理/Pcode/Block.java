package Pcode;

import java.io.IOException;
import java.util.HashMap;

import Error.Error;
import Error.ErrorRecord;
import Pcode.Symbol.Func;
import Pcode.Symbol.Symbol;

public class Block extends PcodeGenerator{
    String funcName;
    public Block(String funcName) throws IOException {
        this.funcName = funcName;
        //currentWord应该是<Block>
        writer.write("start " + funcName);
        nextWord();
        //currentWord : {
        nextWord();

        if(funcName.equals("#block") || funcName.equals("#while") || funcName.equals("#if")){
            HashMap<String, Symbol> symbList = new HashMap<String, Symbol>();
            symbTable.push(symbList);
            currentSymbTable = symbList;
            blockNum++;
        }else{
            symbTable.push(currentSymbTable);
        }


        while(currentWord.typeCode.equals("<BlockItem>")){
            BlockItem blockItem = new BlockItem();
            System.out.println("Check is <BlockItem> :" + currentWord.typeCode);
            //这边要确保是 ; 之后
        }

        if(currentWord.typeCode.equals("RBRACE")){
            System.out.println("Check now is } ,func end : "+ currentWord.typeCode);
            writer.write("end " + funcName);
            nextWord();
        }

        System.out.println("Check symbtable push,size:" + symbTable.size());
        symbTable.pop();
        if(symbTable.size() > 0)
            currentSymbTable = symbTable.peek();
        if((funcName.equals("#block") || funcName.equals("#while") || funcName.equals("#if"))){
            blockNum++;
        }
    }
}
