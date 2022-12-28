package ParserAnalyse;

import Save.*;

//ConstExp → AddExp
public class ConstExp extends SymbAnalyse{  //for number
    public ConstExp(TreeNode parent){
        super("<ConstExp>",parent);
        ////System.out.println("start <ConstExp>");
        //1
        if(AddExp.scanAhead() != ParserType.ERROR){
            AddExp addExp = new AddExp(this.node);
            //printout <ConstExp>
        /*
        save.addParserWord(listIndex,this.node.node);
        listIndex++;

         */
            ////System.out.println("Printout <ConstExp>");
            parserList.add(this.node.node);
        }
    }
}
