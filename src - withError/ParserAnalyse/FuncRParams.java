package ParserAnalyse;

import Save.TreeNode;

//FuncRParams → Exp { ',' Exp }
public class FuncRParams extends SymbAnalyse {
    public FuncRParams(TreeNode parent){
        super("<FuncRParams>",parent);
        System.out.println("Start <FuncRParams>");
        //1
        if(AddExp.scanAhead() != ParserType.ERROR){
            Exp exp = new Exp(this.node);
            //2
            while(sym.content.equals(",")){
                parserList.add(sym);
                TreeNode commaNode = new TreeNode(sym);
                commaNode.addNode(this.node);
                nextSym();
                //2.1
                exp = new Exp(this.node);
            }
            //printout <FuncRParams>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            //sym.typeCode = "PARAEND";
            System.out.println("Printout <FuncRParams>");
            parserList.add(this.node.node);
        }

    }
}
