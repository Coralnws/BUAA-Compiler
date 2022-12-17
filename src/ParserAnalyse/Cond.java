package ParserAnalyse;

import Save.TreeNode;

//Cond → LOrExp
public class Cond extends SymbAnalyse{
    public Cond(TreeNode parent){
        super("<Cond>",parent);
        ////System.out.println("start <Cond>");

        if(AddExp.scanAhead() != ParserType.ERROR){
            LOrExp lOrExp = new LOrExp(this.node);
        }

        ////System.out.println("Printout <Cond>");
        parserList.add(this.node.node);
    }
}
