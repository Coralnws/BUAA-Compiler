package ParserAnalyse;

import Save.TreeNode;
import Save.parserWord;

//VarDecl → 'int' VarDef { ',' VarDef } ';'
public class VarDecl extends SymbAnalyse{
    public VarDecl(TreeNode parent){
        super("<VarDecl>",parent);
        //System.out.println("start <VarDecl>");
        //1
        if(sym.content.equals("int")){
            parserList.add(sym);
            TreeNode intNode = new TreeNode(sym);
            intNode.addNode(this.node);
            nextSym();

            //2
            VarDef varDef = new VarDef(this.node);

            //3 loop for { , VarDef}
            while (sym.content.equals(",")){
                parserList.add(sym);
                TreeNode commaNode = new TreeNode(sym);
                commaNode.addNode(this.node);
                nextSym();

                //3.1
                varDef = new VarDef(this.node);
            }

            //4
            if(sym.content.equals(";")){
                parserList.add(sym);
                TreeNode semicnNode = new TreeNode(sym);
                semicnNode.addNode(this.node);
                nextSym();

                //System.out.println("Printout <VarDecl>");
                parserList.add(this.node.node);
            }
        }
    }
}
