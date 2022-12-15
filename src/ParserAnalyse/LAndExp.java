package ParserAnalyse;

import Save.TreeNode;

//LAndExp → EqExp | LAndExp '&&' EqExp
//左递归 : EqExp {  '&&' EqExp }
public class LAndExp extends SymbAnalyse{
    public LAndExp(TreeNode parent){
        super("<LAndExp>",parent);
        //System.out.println("Start <LAndExp>");
        //1
        EqExp eqExp = new EqExp(this.node);

        //printout <LAndExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        //System.out.println("Printout <LAndExp>");
        parserList.add(this.node.node);

        //2
        while(sym.content.equals("&&")){
            parserList.add(sym);
            TreeNode LAndNode = new TreeNode(sym);
            LAndNode.addNode(this.node);
            nextSym();
            //2.1
            eqExp = new EqExp(this.node);

            //printout <LAndExp>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            //System.out.println("Printout <LAndExp>");
            parserList.add(this.node.node);
        }
    }
}
