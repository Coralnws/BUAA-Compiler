package ParserAnalyse;

import Save.TreeNode;

//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
public class ConstDef extends SymbAnalyse{
    public ConstDef(TreeNode parent){
        super("<ConstDef>",parent);
        System.out.println("start <ConstDef>");

        //1
        if(sym.typeCode.equals("IDENFR")){
            parserList.add(sym);
            TreeNode IdentNode = new TreeNode(sym);
            IdentNode.addNode(this.node);
            nextSym();
            //2 loop for array's dimensions : [][][]
            while(sym.content.equals("[")){
                parserList.add(sym);
                TreeNode LBrackNode = new TreeNode(sym);
                LBrackNode.addNode(this.node);
                nextSym();
                //2.1 - number in []
                ConstExp constExp = new ConstExp(this.node);
                //2.2
                if(sym.content.equals("]")){
                    parserList.add(sym);
                    TreeNode RBrackNode = new TreeNode(sym);
                    RBrackNode.addNode(this.node);
                    nextSym();
                }
            }
            //3
            if(sym.content.equals("=")){
                parserList.add(sym);
                TreeNode assignNode = new TreeNode(sym);
                assignNode.addNode(this.node);
                nextSym();

                //4
                ConstInitVal constInitVal = new ConstInitVal(this.node);
            }
            else{
                System.out.println("ConstDef Error:Not = ");
            }


            //printout <ConstDef>
            /*
            save.addParserWord(listIndex,this.node.node);
            listIndex++;

             */
            System.out.println("Printout <ConstDef>");
            parserList.add(this.node.node);
        }
    }
}
