package Pcode;

import java.io.IOException;

public class BlockItem extends PcodeGenerator{
    public BlockItem() throws IOException {
        //currentWord应该是<BlockItem>
        nextWord();

        if(currentWord.typeCode.equals("<Decl>")){
            Decl decl = new Decl();
        }
        else if(currentWord.typeCode.equals("<Stmt>")){
            Stmt stmt = new Stmt();
        }
    }
}
