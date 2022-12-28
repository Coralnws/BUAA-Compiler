package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Save.Word;
import Save.lexerWord;
import Error.Error;
import Error.ErrorRecord;
//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
public class ConstDef extends SymbAnalyse{
    public ConstDef(TreeNode parent){
        super("<ConstDef>",parent);
        System.out.println("start <ConstDef>");

        //1
        if(sym.typeCode.equals("IDENFR")){
            parserList.add(sym);
            TreeNode IdentNode = new TreeNode(sym);
            IdentNode.addNode(this.node);
            nextSym();
            //2 loop for array's dimensions : [][][]
            while(sym.content.equals("[")){
                parserList.add(sym);
                TreeNode LBrackNode = new TreeNode(sym);
                LBrackNode.addNode(this.node);
                nextSym();
                //2.1 - number in []
                ConstExp constExp = new ConstExp(this.node);
                //2.2
                if(sym.content.equals("]")){
                    parserList.add(sym);
                    TreeNode RBrackNode = new TreeNode(sym);
                    RBrackNode.addNode(this.node);
                    nextSym();
                }else{
                    Word word = save.getSym(listIndex-2);
                    System.out.println("Check word is what:" + word.content);
                    Error error = new Error(word.line,'k');
                    PcodeGenerator.errorRecord.addError(error);
                    word = new lexerWord("RBRACK",",", word.line);
                    TreeNode RBrackNode= new TreeNode(word);
                    RBrackNode.addNode(this.node);

                }
            }
            //3
            if(sym.content.equals("=")){
                parserList.add(sym);
                TreeNode assignNode = new TreeNode(sym);
                assignNode.addNode(this.node);
                nextSym();

                //4
                ConstInitVal constInitVal = new ConstInitVal(this.node);
            }
            else{
                System.out.println("ConstDef Error:Not = ");
            }


            //printout <ConstDef>
            /*
            save.addParserWord(listIndex,this.node.node);
            listIndex++;

             */
            System.out.println("Printout <ConstDef>");
            parserList.add(this.node.node);
        }
    }
}
