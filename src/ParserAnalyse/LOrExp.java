package ParserAnalyse;

import Save.TreeNode;

//LOrExp → LAndExp | LOrExp '||' LAndExp
//左递归 : LAndExp { '||' LAndExp }
public class LOrExp extends SymbAnalyse{
    public LOrExp(TreeNode parent){
        super("<LOrExp>",parent);
        //System.out.println("Start <LOrExp>");
        //1
        LAndExp lAndExp = new LAndExp(this.node);

        //printout <LOrExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        //System.out.println("Printout <LOrExp>");
        parserList.add(this.node.node);

        //2
        while(sym.content.equals("||")){
            parserList.add(sym);
            TreeNode LOrNode = new TreeNode(sym);
            LOrNode.addNode(this.node);
            nextSym();
            //2.1
            lAndExp = new LAndExp(this.node);

            //printout <LOrExp>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            //System.out.println("Printout <LOrExp>");
            parserList.add(this.node.node);
        }
    }
}
