package ParserAnalyse;

import Save.TreeNode;

//FuncType → 'void' | 'int'
public class FuncType extends SymbAnalyse{
    public FuncType(TreeNode parent){
        super("<FuncType>",parent);
        ////System.out.println("Start <FuncType>");
        //1
        if(sym.content.equals("void")){
            parserList.add(sym);
            TreeNode voidNode = new TreeNode(sym);
            voidNode.addNode(this.node);
            nextSym();

            ////System.out.println("Printout <FuncType>");
            parserList.add(this.node.node);
        }
        else if(sym.content.equals("int")){
            parserList.add(sym);
            TreeNode intNode = new TreeNode(sym);
            intNode.addNode(this.node);
            nextSym();

            ////System.out.println("Printout <FuncType>");
            parserList.add(this.node.node);
        }
        else{
            ////System.out.println("FuncType Error: Not match");
        }

    }

}
