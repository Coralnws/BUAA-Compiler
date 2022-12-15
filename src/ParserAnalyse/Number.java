package ParserAnalyse;

import Save.TreeNode;

//Number → IntConst
public class Number extends SymbAnalyse{
    public Number(TreeNode parent){
        super("<Number>",parent);
        //System.out.println("start <Number>");
        if(sym.typeCode.equals("INTCON")){
            parserList.add(sym);
            TreeNode numberNode = new TreeNode(sym);
            numberNode.addNode(this.node);
            nextSym();

            //printout <Number>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            //System.out.println("Printout <Number>");
            parserList.add(this.node.node);
        }
        else{
            //System.out.println("Number Error");
        }
    }
}
