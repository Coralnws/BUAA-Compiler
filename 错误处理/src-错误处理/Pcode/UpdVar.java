package Pcode;

import java.io.IOException;

import static Pcode.Array.ArrayTestExp;
import static Pcode.ConstDef.ConstDeclTestExp;
import static Pcode.Return.BlockItemTestExp;
import Error.Error;
import Error.ErrorRecord;
import Pcode.Symbol.Symbol;

public class UpdVar extends PcodeGenerator{
    char type;
    public UpdVar() throws IOException {
        //currentWord应该是<name>
        System.out.println("UpdVar, Check current is <name>:" + currentWord.content);
        ignoreParser = true;
        pcode.append("upd");  //append name
        //check is var or array
        System.out.println("UpdVar check type:" + wordAhead.typeCode);
        if(!wordAhead.content.equals("[")){
            type = 'v';
        } else{
            type = 'a';
        }
        if(currentWord.typeCode.equals("IDENFR")){
            //error
            Symbol symb = checkVarExist(currentWord.content);
            //System.out.println("Check symb name :" + symb.name + " and symb's type:" + symb.type);
            if(symb != null && symb.type == 0){
                Error error = new Error(currentWord.line,'h');
                errorRecord.addError(error);
            }

            pcode.append(" " + currentWord.content); //append name
            System.out.println("Debug1 : " + pcode);
            nextWord(); // array - [  , var - =
        }

        if(type == 'a'){ //array - currentWord = [
            while(currentWord.content.equals("[")){
                pcode.append(" " + currentWord.content);  // append [
                nextWord();

                //inside DIM
                System.out.println("UpdVar 检查Array DIM 里：" + currentWord.content);
                if(ArrayTestExp()){
                    System.out.println("UpdVar Array DIM 里是Exp");
                    ConstExp constExp = new ConstExp();
                    pcode.append(" " + varT);
                    pcode.append(" "+ currentWord.content);
                    nextWord();
                    //System.out.println("debug: " + currentWord.content);
                    //这边要确保出来的时候是 [ or leave array ,总之是 ] 的下一个 - 所以需要nextWord()
                }
                else{
                    System.out.println("UpdVar Array DIM 里没有Exp");
                    pcode.append(" " + currentWord.content);  //append var or value
                    nextWord(); // ]
                    pcode.append(" " + currentWord.content);
                    nextWord(); // [ or leave array
                }
            }
            //currentWord应该是 =
            System.out.println("Check word is = :" + currentWord.content);
            if(currentWord.content.equals("=")){
                pcode.append(" " + currentWord.content);
                nextWord();  // {
                while(!currentWord.typeCode.equals("SEMICN")){
                    if(BlockItemTestExp()){
                        Exp exp = new Exp();
                        pcode.append(" " + varT);
                    }else{
                        pcode.append(" " + currentWord.content);
                        nextWord();  // intcon/exp/var or {
                    }
                    System.out.println("UpdVar while loop:" + currentWord.typeCode);
                }
            }
            //currentWord应该是 ; or ,
            System.out.println("Check now is ; : " + currentWord.typeCode);
            writer.write(String.valueOf(pcode));
            ignoreParser = false;
            nextWord();
            System.out.println("Check now skip ; : " + currentWord.typeCode);
        }else if(type == 'v'){
            if(currentWord.content.equals("=")){
                pcode.append(" " + currentWord.content);
                nextWord();  // var/val/exp
                while(!currentWord.typeCode.equals("SEMICN")){
                    if(BlockItemTestExp()){
                        Exp exp = new Exp();
                        pcode.append(" " + varT);
                    }else{
                        while(!currentWord.typeCode.equals("SEMICN")){
                            pcode.append(" " + currentWord.content);
                            nextWord();  // intcon/exp/var or {
                        }
                    }
                    System.out.println("UpdVar while loop:" + currentWord.typeCode);
                }
            }
            //currentWord应该是 ;
            System.out.println("Check now is ;: " + currentWord.typeCode);
            writer.write(String.valueOf(pcode));
            ignoreParser = false;
            nextWord();
            System.out.println("Check now skip ;: " + currentWord.typeCode);
        }
        ignoreParser = false;

    }
}
