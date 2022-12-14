package ParserAnalyse;

import Save.TreeNode;

//FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
public class FuncDef extends SymbAnalyse{
    public FuncDef(TreeNode parent){
        super("<FuncDef>",parent);
        System.out.println("Start <FuncDef>");
        //1
        if(sym.content.equals("void") || sym.content.equals("int")){
            FuncType funcType = new FuncType(this.node);

            //2
            if(sym.typeCode.equals("IDENFR")){
                parserList.add(sym);
                TreeNode IdentNode = new TreeNode(sym);
                IdentNode.addNode(this.node);
                nextSym();
            }
            //3
            if(sym.content.equals("(")){
                parserList.add(sym);
                TreeNode LParentNode = new TreeNode(sym);
                LParentNode.addNode(this.node);
                nextSym();

                if(!sym.content.equals(")")){
                    FuncFParams funcFParams = new FuncFParams(this.node);
                }
                if (sym.content.equals(")")){
                    parserList.add(sym);
                    TreeNode RParentNode = new TreeNode(sym);
                    RParentNode.addNode(this.node);
                    nextSym();

                    Block block = new Block(this.node);
                }

                System.out.println("Printout <FuncDef>");
                parserList.add(this.node.node);
            }
        }
    }
}
