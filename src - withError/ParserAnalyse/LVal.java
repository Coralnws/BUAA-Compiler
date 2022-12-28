package ParserAnalyse;

import Save.TreeNode;

// LVal → Ident {'[' Exp ']'}
public class LVal extends SymbAnalyse{
    public LVal(TreeNode parent){
        super("<LVal>",parent);
        //System.out.println("Start <lval>");
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
                }
            }
            //printout <LVal>
            //save.addParserWord(listIndex,this.node.node);
           // listIndex++;
            //System.out.println("Printout <lval>");
            parserList.add(this.node.node);

        }
    }
}
