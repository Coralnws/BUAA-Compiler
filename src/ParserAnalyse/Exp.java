package ParserAnalyse;

import Save.TreeNode;

//Exp → AddExp
public class Exp extends SymbAnalyse{
    public Exp(TreeNode parent){
        super("<Exp>",parent);
        //System.out.println("Start <Exp>");
        AddExp addExp = new AddExp(this.node);

        //printout <Exp>
        /*
        save.addParserWord(listIndex,this.node.node);
        listIndex++;

         */
        //System.out.println("Printout <Exp>");
        parserList.add(this.node.node);

    }
}
