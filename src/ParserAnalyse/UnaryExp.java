package ParserAnalyse;

import Save.TreeNode;

//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')'  | UnaryOp UnaryExp
public class UnaryExp extends SymbAnalyse{
    public UnaryExp(TreeNode parent){
        super("<UnaryExp>",parent);
        System.out.println("start <UnaryExp>");
        //1
        ParserType type = AddExp.scanAhead();
        if(type == ParserType.PrimaryExp || type == ParserType.LVal){
            PrimaryExp primaryExp = new PrimaryExp(this.node);
        }
        else if(type ==  ParserType.UnaryOp){
            UnaryOp unaryOp = new UnaryOp(this.node);
            UnaryExp unaryExp = new UnaryExp(this.node);
        }
        else if(type ==  ParserType.UnaryExp){
            if(sym.typeCode.equals("IDENFR")){
                parserList.add(sym);
                TreeNode IdentNode = new TreeNode(sym);
                IdentNode.addNode(this.node);
                nextSym();

                if(sym.content.equals("(")){
                    parserList.add(sym);
                    TreeNode LParentNode = new TreeNode(sym);
                    LParentNode.addNode(this.node);
                    nextSym();
                }
                if(!sym.content.equals(")")){
                    FuncRParams funcRParams = new FuncRParams(this.node);
                }

                if(sym.content.equals(")")){
                    parserList.add(sym);
                    sym.typeCode = "PARAEND";
                    TreeNode RParentNode = new TreeNode(sym);
                    RParentNode.addNode(this.node);
                    nextSym();
                }
            }
        }

        //printout <UnaryExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        System.out.println("Printout <UnaryExp>");
        parserList.add(this.node.node);
    }
}
