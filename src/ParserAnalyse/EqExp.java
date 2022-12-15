package ParserAnalyse;

import Save.TreeNode;

//EqExp → RelExp | EqExp ('==' | '!=') RelExp
//左递归 : RelExp { ('==' | '!=') RelExp }
public class EqExp extends SymbAnalyse{
    public EqExp(TreeNode parent){
        super("<EqExp>",parent);
        //System.out.println("Start <EqExp>");

        //1
        RelExp relExp = new RelExp(this.node);
        //printout <EqExp>
        /*
        save.addParserWord(listIndex,this.node.node);
        listIndex++;

         */
        //System.out.println("Printout <EqExp>");
        parserList.add(this.node.node);
        //2
        while(sym.typeCode.equals("EQL") || sym.typeCode.equals("NEQ")){
            parserList.add(sym);
            TreeNode eqExpNode = new TreeNode(sym);
            eqExpNode.addNode(this.node);
            nextSym();

            //2.1
            relExp = new RelExp(this.node);

            //System.out.println("Printout <EqExp>");
            parserList.add(this.node.node);
        }
    }
}
