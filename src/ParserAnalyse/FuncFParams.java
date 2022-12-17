package ParserAnalyse;

import Save.TreeNode;

//FuncFParams → FuncFParam { ',' FuncFParam }
public class FuncFParams extends SymbAnalyse{
    public FuncFParams(TreeNode parent){
        super("<FuncFParams>",parent);
        ////System.out.println("Start <FuncFParams>");

        if(sym.content.equals("int")){  //need to check if sym is "int", so that when it isn't,this function won't insert anything into wordlist
            FuncFParam funcFParam = new FuncFParam(this.node);
            while(sym.content.equals(",")){
                parserList.add(sym);
                TreeNode commaNode = new TreeNode(sym);
                commaNode.addNode(this.node);
                nextSym();

                funcFParam = new FuncFParam(this.node);
            }
            //printout <FuncFParams>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            ////System.out.println("Printout <FuncFParams>");
            parserList.add(this.node.node);
        }
    }
}
