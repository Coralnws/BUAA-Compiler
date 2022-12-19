package Pcode;

import Save.lexerWord;

import java.io.IOException;

import static Pcode.Array.ArrayTestExp;
import static Pcode.Return.BlockItemTestExp;

public class ConstDef extends PcodeGenerator{
    char type;
    public ConstDef() throws IOException {
        /**
         * <ConstDef>
         * arr
         * [  ||  =
         */
        //currentWord应该是<ConstDef>
        //System.out.println("Check ConstDef开头 is <ConstDef>:" + currentWord.typeCode);
        ignoreParser = true;
        nextWord();
        pcode.append("const");
        if(currentWord.typeCode.equals("IDENFR")){
            if(!wordAhead.content.equals("[")){
                pcode.append(" v");
                type = 'v';
            } else{
                pcode.append(" a");
                type = 'a';
            }
            if(currentWord.typeCode.equals("IDENFR")){
                pcode.append(" " + currentWord.content);
                nextWord(); // array - [  , var - =
            }

            if(type == 'a'){ //array - currentWord = [
                while(currentWord.content.equals("[")){
                    pcode.append(" " + currentWord.content);  // append [
                    nextWord();
                    //inside DIM
                    //System.out.println("ConstDecl 检查Array DIM 里：" + currentWord.content);
                    if(ArrayTestExp()){
                        //System.out.println("ConstDecl Array DIM 里是Exp");
                        ConstExp constExp = new ConstExp();
                        pcode.append(" " + varT);
                        pcode.append(" "+ currentWord.content);
                        ignoreParser = true;
                        nextWord();
                        ////System.out.println("debug: " + currentWord.content);
                        //这边要确保出来的时候是 [ or leave array ,总之是 ] 的下一个 - 所以需要nextWord()
                    }
                    else{
                        //System.out.println("ConstDecl Array DIM 里没有Exp");
                        pcode.append(" " + currentWord.content);  //append var or value
                        nextWord(); // ]
                        pcode.append(" " + currentWord.content);
                        nextWord(); // [ or leave array
                    }
                }
                //currentWord应该是 =
                ignoreParser = false;
                //System.out.println("Check word is = :" + currentWord.content);
                if(currentWord.content.equals("=")){
                    pcode.append(" " + currentWord.content);
                    nextWord();  // {
                    while(!(currentWord.typeCode.equals("COMMA") && !wordAhead.typeCode.equals("<ConstInitVal>")) && !currentWord.typeCode.equals("SEMICN")){
                        if(currentWord.typeCode.equals("<ConstInitVal>")){
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
                        //System.out.println("ConstDef while loop:" + currentWord.typeCode);
                    }
                }
                //currentWord应该是 ;
                //System.out.println("Check now is ; or , : " + currentWord.typeCode);
                writer.write(String.valueOf(pcode));
                //ignoreParser = false;
                nextWord();
                //System.out.println("Check now skip ; or , : " + currentWord.typeCode);
            }else if(type == 'v'){
                ignoreParser = true;
                if(currentWord.content.equals("=")){
                    pcode.append(" " + currentWord.content);
                    nextWord();  // {
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
                        //System.out.println("ConstDecl while loop:" + currentWord.typeCode);
                    }
                }
                //currentWord应该是 ;
                //System.out.println("Check now is ; or , : " + currentWord.typeCode);
                writer.write(String.valueOf(pcode));
                ignoreParser = false;
                nextWord();
                //System.out.println("Check now skip ; or , : " + currentWord.typeCode);
            }
        }
    }

    public static boolean ConstDeclTestExp(){
        int scanIndex=index-1;
        scanWord(scanIndex);
        while(!scanWord.typeCode.equals("RBRACE") && !scanWord.typeCode.equals("COMMA") && !scanWord.typeCode.equals("SEMICN")) {
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
            if(scanWord.typeCode.equals("LPARENT")){
                return true;
            }
            scanIndex ++;
            scanWord(scanIndex);
        }
        return false;
    }
}
