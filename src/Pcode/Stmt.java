package Pcode;

import java.io.IOException;
/*
Stmt → LVal '=' Exp ';' (Done)
| [Exp] ';' (Done)
| Block
| 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
| 'while' '(' Cond ')' Stmt
| 'break' ';' | 'continue' ';'
| 'return' [Exp] ';' (Done)
| LVal '=' 'getint''('')'';' (Done)
| 'printf''('FormatString{','Exp}')'';' (Done)
 */
public class Stmt extends PcodeGenerator{
    int elseNum;
    public Stmt() throws IOException {
        //System.out.println("Pcode Stmt,check now is <Stmt>: " + currentWord.typeCode);
        nextWord();

        if(currentWord.typeCode.equals("<LVal>")){  //updVar or getint
            /**
             * <Stmt>
             * <LVal>
             * c
             * =
             * <Exp>  // getint(lexer)
             */
            nextWord();  // <name>
            nextWord(); // =
            if(wordAhead.typeCode.equals("<Exp>")){
                index -= 2;
                //prevWord();
                //prevWord();
                nextWord();
                //System.out.println("Debug : " + currentWord.typeCode);
                UpdVar updVar = new UpdVar();
            }else if(wordAhead.content.equals("getint")){
                index -= 2;
                nextWord();
                nextTVar();
                writer.write(varT + " = GETINT");
                writer.write("upd " + currentWord.content + " = " + varT);
                while(!currentWord.typeCode.equals("SEMICN")){
                    nextWord();
                }
                nextWord();  //确保是在 ; 之后
            }
        }
        else if(currentWord.typeCode.equals("RETURNTK")){
            /**
             * <BlockItem>
             * <Stmt>
             * return
             * <Exp>
             */
            Return ret = new Return();
        }
        else if(currentWord.typeCode.equals("PRINTFTK")){
            Print print = new Print();
        }
        else if(currentWord.typeCode.equals("<Exp>")){
            nextWord();
            Exp exp = new Exp();
            nextWord();
        }
        else if(currentWord.typeCode.equals("SEMICN")){
            //System.out.println("Stmt is ; ,skip");
            nextWord();
        }
        else if(currentWord.typeCode.equals("IFTK")){
            /**
             * <Stmt>
             * if
             * (
             * <Cond>
             */
            nextIf();
            elseNum = ifNum();
            writer.write(getIfNum());
            nextWord();
            nextWord();
            Cond cond = new Cond();
            if(currentWord.typeCode.equals("<Stmt>")){
                if(wordAhead.typeCode.equals("<Block>")){
                    nextWord(); // <Block>
                    Block block = new Block(getIfNum());
                }else{
                    writer.write("start "+getIfNum());
                    Stmt stmt = new Stmt();
                    writer.write("end "+getIfNum());
                }
                if(currentWord.typeCode.equals("ELSETK")){
                    nextWord(); //<Stmt>
                    if(currentWord.typeCode.equals("<Stmt>")){
                        if(wordAhead.typeCode.equals("<Block>")) {
                            nextWord(); //<Block>
                            Block block = new Block("#else"+elseNum);
                        }else{
                            writer.write("start #else"+elseNum);
                            Stmt stmt = new Stmt();
                            writer.write("end #else"+elseNum);
                        }
                    }
                }
            }
        }
        else if(currentWord.typeCode.equals("WHILETK")){
            /**
             * <Stmt>
             * while
             * (
             * <Cond>
             */

            writer.write("#while");
            nextWord();  // (
            nextWord(); // <Cond>
            Cond cond = new Cond();
            if(currentWord.typeCode.equals("<Stmt>")) {
                if (wordAhead.typeCode.equals("<Block>")) {
                    nextWord(); //<Block>
                    Block block = new Block("#while");
                }else{
                    writer.write("start #while");
                    Stmt stmt = new Stmt();
                    writer.write("end #while");
                }
            }
        }
        else if(currentWord.typeCode.equals("CONTINUETK")){
            writer.write("CONTINUE");
            nextWord(); // ;
            nextWord();
        }
        else if(currentWord.typeCode.equals("BREAKTK")){
            writer.write("BREAK");
            nextWord(); // ;
            nextWord();
        }
        else if(currentWord.typeCode.equals("<Block>")){
            Block block = new Block("#block");
        }

        //System.out.println("End of stmt ,now is: "+ currentWord.typeCode);
    }
}
