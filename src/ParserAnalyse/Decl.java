package ParserAnalyse;

import Save.TreeNode;
import Save.parserWord;


//Decl → ConstDecl | VarDecl
//No need to print
public class Decl extends SymbAnalyse{
    public Decl(TreeNode parent){
        super("<Decl>",parent);
        //System.out.println("Start <Decl>");
        //1
        if(sym.content.equals("const")){
            ConstDecl constDecl = new ConstDecl(this.node);
        }
        //2
        else if(sym.content.equals("int")){
            VarDecl varDecl = new VarDecl(this.node);
        }
    }
}
