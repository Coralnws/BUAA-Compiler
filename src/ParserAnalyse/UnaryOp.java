package ParserAnalyse;

import Save.TreeNode;

//UnaryOp→ '+' | '−' | '!'
public class UnaryOp extends SymbAnalyse{
    public UnaryOp(TreeNode parent){
        super("<UnaryOp>",parent);
        //System.out.println("start <UnaryOp>");
        //1
        if(sym.content.equals("+") || sym.content.equals("-") || sym.content.equals("!")){
            parserList.add(sym);
            TreeNode unaryOpNode = new TreeNode(sym);
            unaryOpNode.addNode(this.node);
            nextSym();
        }
        else{
            //System.out.println("UnaryOp Error: Not Match.");
        }

        //printout <UnaryOp>
        //save.addParserWord(listIndex,this.node.node);
       // listIndex++;
        //System.out.println("Printout <UnaryOp>");
        parserList.add(this.node.node);
    }
}

