package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Save.Word;
import Save.lexerWord;
import Error.Error;

import static Parser.Parser.testError;

// LVal → Ident {'[' Exp ']'}
public class LVal extends SymbAnalyse{
    public LVal(TreeNode parent){
        super("<LVal>",parent);
        System.out.println("Start <lval>");
        //1
        if(sym.typeCode.equals("IDENFR")){
            parserList.add(sym);
            TreeNode IdentNode = new TreeNode(sym);
            IdentNode.addNode(this.node);
            nextSym();
            //2 loop for { [Exp] }
            while(sym.content.equals("[")){
                parserList.add(sym);
                TreeNode LBrackNode = new TreeNode(sym);
                LBrackNode.addNode(this.node);
                nextSym();
                //2.1
                Exp exp = new Exp(this.node);
                //2.2
                if(sym.content.equals("]")){
                    parserList.add(sym);
                    TreeNode RBrackNode = new TreeNode(sym);
                    RBrackNode.addNode(this.node);
                    nextSym();
                }else if(testError){
                    Word word = save.getSym(listIndex-2);
                    System.out.println("Check word is what:" + word.content);
                    Error error = new Error(word.line,'k');
                    PcodeGenerator.errorRecord.addError(error);
                    word = new lexerWord("RBRACK",",", word.line);
                    TreeNode RBrackNode= new TreeNode(word);
                    RBrackNode.addNode(this.node);

                }
            }
            //printout <LVal>
            //save.addParserWord(listIndex,this.node.node);
           // listIndex++;
            System.out.println("Printout <lval>");
            parserList.add(this.node.node);

        }
    }
}
