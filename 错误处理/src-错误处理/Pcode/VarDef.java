package Pcode;

import Pcode.Symbol.Arr;
import Pcode.Symbol.Symbol;
import Pcode.Symbol.Variable;
import Pcode.Symbol.paraArr;
import Save.lexerWord;
import Error.Error;

import java.io.IOException;

import static Pcode.Array.ArrayTestExp;
import static Pcode.ConstDef.ConstDeclTestExp;

/**
 * <Decl>
 * <VarDecl>
 * int
 * <VarDef>
 * a
 * =
 * <InitVal>
 * <Exp>
 * <AddExp>
 * <MulExp>
 */
public class VarDef extends PcodeGenerator{
    char type;
    public VarDef() throws IOException {
        //var v <name> =
        //currentWord应该是<VarDecl>
        System.out.println("VarDecl开头 :" + currentWord.typeCode);
        ignoreParser = true;

        Symbol symb = null;
        if(currentWord.typeCode.equals("<VarDef>")){
            pcode.append("var");
            nextWord(); //name
            System.out.println("Check type of varDecl: " + wordAhead.typeCode);
            if(!wordAhead.content.equals("[")){
                pcode.append(" v");
                type = 'v';
            } else{
                pcode.append(" a");
                type = 'a';
            }
            if(currentWord.typeCode.equals("IDENFR")){
                //error
                System.out.println("DEBUG, check vardef:" + currentWord.content);
                if(checkCurrentSymbExist(currentWord.content)){
                    System.out.println("vardef , gotError b");
                    Error error = new Error(currentWord.line,'b');
                    errorRecord.addError(error);
                }
                if(type == 'v'){
                    symb = new Variable(currentWord.content,1);  //type == 1 就是非常量
                    currentSymbTable.put(currentWord.content,symb);
                    System.out.println("DEBUG, check func:" + currentFunc);
                }else if(type == 'a'){
                    System.out.println("DefVar here");
                    symb = new Arr(currentWord.content,1);
                    currentSymbTable.put(currentWord.content,symb);
                }
                System.out.println("Check line number:" + currentWord.line);
                pcode.append(" " + currentWord.content);
                nextWord(); // array - [  , var - =
            }

            if(type == 'a'){ //array - currentWord = [
                Arr arr = (Arr) symb;
                while(currentWord.content.equals("[")){
                    symb.dimensionNum++;
                    pcode.append(" " + currentWord.content);  // append [
                    nextWord();
                    //inside DIM
                    System.out.println("ConstDecl 检查Array DIM 里：" + currentWord.content);
                    if(ArrayTestExp()){
                        System.out.println("ConstDecl Array DIM 里是Exp");
                        ConstExp constExp = new ConstExp();
                        pcode.append(" " + varT);
                        pcode.append(" "+ currentWord.content);
                        //ignoreParser = true;
                        nextWord();
                        //System.out.println("debug: " + currentWord.content);
                        //这边要确保出来的时候是 [ or leave array ,总之是 ] 的下一个 - 所以需要nextWord()
                    }
                    else{
                        System.out.println("ConstDecl Array DIM 里没有Exp");
                        pcode.append(" " + currentWord.content);  //append var or value
                        if(currentWord.typeCode.equals("INTCON"))
                            arr.addDimension(Integer.parseInt(currentWord.content));
                        nextWord(); // ]
                        pcode.append(" " + currentWord.content);
                        nextWord(); // [ or leave array
                    }
                }
                //currentWord应该是 =
                ignoreParser = false;
                System.out.println("Check word is = :" + currentWord.content);
                if(currentWord.content.equals("=")){
                    pcode.append(" " + currentWord.content);
                    nextWord();  // {
                    while(!(currentWord.typeCode.equals("COMMA") && !wordAhead.typeCode.equals("<InitVal>")) && !currentWord.typeCode.equals("SEMICN")){
                        if(currentWord.typeCode.equals("<InitVal>")){
                            nextWord();
                        }
                        if(currentWord.typeCode.equals("LBRACE") || currentWord.typeCode.equals("RBRACE")){
                            pcode.append(" " + currentWord.content);
                            nextWord();  // intcon/exp/var or {
                        }else if(ConstDeclTestExp()){
                            Exp exp = new Exp();
                            pcode.append(" " + varT);
                            //ignoreParser = true;
                        }else if(currentWord instanceof lexerWord){
                            pcode.append(" "+ currentWord.content);
                            nextWord();
                        } else{
                            nextWord();
                        }
                        System.out.println("ConstDef while loop:" + currentWord.typeCode);
                    }
                }
                //currentWord应该是 ;
                System.out.println("Check now is ; or , : " + currentWord.typeCode);
                writer.write(String.valueOf(pcode));
                //ignoreParser = false;
                nextWord();
                System.out.println("Check now skip ; or , : " + currentWord.typeCode);
            }else if(type == 'v'){
                ignoreParser = true;
                if(currentWord.content.equals("=")){
                    pcode.append(" " + currentWord.content);
                    nextWord();  // var/val/exp
                    while(!currentWord.typeCode.equals("SEMICN") && !currentWord.typeCode.equals("COMMA")){
                        if(ConstDeclTestExp()){
                            Exp exp = new Exp();
                            pcode.append(" " + varT);
                        }else{
                            while(!currentWord.typeCode.equals("SEMICN") && !currentWord.typeCode.equals("COMMA")){
                                pcode.append(" " + currentWord.content);
                                nextWord();  // intcon/exp/var or {
                            }
                        }
                        System.out.println("VarDef while loop:" + currentWord.typeCode);
                    }
                }
                //currentWord应该是 ;
                System.out.println("Check now is ; or , : " + currentWord.typeCode);
                writer.write(String.valueOf(pcode));
                ignoreParser = false;
                nextWord();
                System.out.println("Check now skip ; or , : " + currentWord.typeCode);
            }
        }
        ignoreParser = false;
    }
}
