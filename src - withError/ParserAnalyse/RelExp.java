package ParserAnalyse;

import Save.TreeNode;

//RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
//左递归 : AddExp { ('<' | '>' | '<=' | '>=') AddExp }
public class RelExp extends SymbAnalyse{
    public RelExp(TreeNode parent){
        super("<RelExp>",parent);
        System.out.println("start <RelExp>");

        //1
        AddExp addExp = new AddExp(this.node);

        //printout <RelExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        parserList.add(this.node.node);

        //2 loop for { ('<' | '>' | '<=' | '>=') AddExp }
        while(sym.content.equals("<") || sym.content.equals(">") || sym.content.equals(">=") || sym.content.equals("<=")){
            parserList.add(sym);
            TreeNode relNode = new TreeNode(sym);
            relNode.addNode(this.node);
            nextSym();

            //2.1
            addExp = new AddExp(this.node);

            //printout <RelExp>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            System.out.println("Printout <relExp>");
            parserList.add(this.node.node);
        }
    }
}
