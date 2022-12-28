package Pcode;

import java.io.IOException;
import Error.Error;
import Error.ErrorRecord;
public class Decl extends PcodeGenerator{
    public Decl() throws IOException {
        //currentWord应该是<Decl>
        nextWord();

        if(currentWord.typeCode.equals("<ConstDecl>")){
            /**
             * <Decl>
             * <ConstDecl>
             * const
             * int
             * <ConstDef>
             */
            nextWord();  //const
            nextWord();  //int
            nextWord(); //<ConstDef>
            while(currentWord.typeCode.equals("<ConstDef>")){
                ConstDef constDef = new ConstDef();
            }
        }
        else if(currentWord.typeCode.equals("<VarDecl>")){
            /**
             * <Decl>
             * <VarDecl>
             * int
             * <VarDef>
             */
            nextWord(); //int
            nextWord(); //<VarDef>
            while(currentWord.typeCode.equals("<VarDef>")){
                VarDef varDef = new VarDef();
            }
        }
    }
}
