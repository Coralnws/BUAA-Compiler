package ParserAnalyse;

import Save.TreeNode;

//BlockItem → Decl | Stmt
//No need to print
public class BlockItem extends SymbAnalyse{
    public BlockItem(TreeNode parent){

        super("<BlockItem>",parent);
        System.out.println("Start <BlockItem>");
        //1
        if(sym.content.equals("const") || sym.content.equals("int")){
            Decl decl = new Decl(this.node);
        }
        //2
        else{
            Stmt stmt = new Stmt(this.node);
        }
    }
}
