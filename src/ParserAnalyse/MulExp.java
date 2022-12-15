package ParserAnalyse;

import Save.*;

//MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
//左循环 UnaryExp { (* | / | %)  UnaryExp}
public class MulExp extends SymbAnalyse{
    public MulExp(TreeNode parent){
        super("<MulExp>",parent);
        //System.out.println("start <MulExp>");
        //1
        UnaryExp unaryExp = new UnaryExp(this.node);

        //printout<MulExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        //System.out.println("Printout <MulExp>");
        parserList.add(this.node.node);

        //2 loop for { (* | / | %)  UnaryExp}
        while(sym.content.equals("*") || sym.content.equals("/") || sym.content.equals("%")){
            parserList.add(sym);
            TreeNode mulNode = new TreeNode(sym);
            mulNode.addNode(this.node);
            nextSym();
            //2.1
            unaryExp = new UnaryExp(this.node);

            //printout<MulExp>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            //System.out.println("Printout <MulExp>");
            parserList.add(this.node.node);
        }
    }
}
