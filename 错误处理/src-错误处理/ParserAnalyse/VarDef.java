package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Error.Error;
import Error.ErrorRecord;
import Save.Word;
import Save.lexerWord;

//VarDef → Ident { '[' ConstExp ']' }  | Ident { '[' ConstExp ']' } '=' InitVal
public class VarDef extends SymbAnalyse{
    public VarDef(TreeNode parent){
        super("<VarDef>",parent);
        System.out.println("start <VarDef>");
        //1 - Ident
        if(sym.typeCode.equals("IDENFR")){
            parserList.add(sym);
            TreeNode IdentNode = new TreeNode(sym);
            IdentNode.addNode(this.node);
            nextSym();

            //2 - for array def
            while(sym.content.equals("[")){
                parserList.add(sym);
                TreeNode LNode = new TreeNode(sym);
                LNode.addNode(this.node);
                nextSym();
                //2.1
                ConstExp constExp = new ConstExp(this.node);
                //2.2
                if(sym.content.equals("]")){
                    parserList.add(sym);
                    TreeNode LBrackNode = new TreeNode(sym);
                    LBrackNode.addNode(this.node);
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
                InitVal initVal = new InitVal(this.node);
            }

            //printout <VarDef>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            System.out.println("Printout <VarDef>");
            parserList.add(this.node.node);
        }
        else{
            System.out.println("VarDef Error: Not ident");
        }
    }
}
