package ParserAnalyse;

import Save.TreeNode;

//PrimaryExp → '(' Exp ')' | LVal | Number
public class PrimaryExp extends SymbAnalyse{
    public PrimaryExp(TreeNode parent){
        super("<PrimaryExp>",parent);
        ////System.out.println("start <PrimaryExp>");
        //1 (Exp)
        if(sym.content.equals("(")){
            parserList.add(sym);
            TreeNode LParentNode = new TreeNode(sym);
            LParentNode.addNode(this.node);
            nextSym();

            Exp exp = new Exp(this.node);

            if(sym.content.equals(")")){
                parserList.add(sym);
                TreeNode RParentNode = new TreeNode(sym);
                RParentNode.addNode(this.node);
                nextSym();
            }

        }
        else if(sym.typeCode.equals("INTCON")){
            Number number = new Number(this.node);
        }
        else if(sym.typeCode.equals("IDENFR")){
            LVal lVal = new LVal(this.node);
        }
        else{
            ////System.out.println("PrimaryExp error: Nothing match");
        }

        //printout <PrimaryExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        ////System.out.println("Printout <PrimaryExp>");
        parserList.add(this.node.node);
    }
}
