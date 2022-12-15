package ParserAnalyse;

import Save.TreeNode;

//MainFuncDef → 'int' 'main' '(' ')' Block
public class MainFuncDef extends SymbAnalyse {
    public MainFuncDef(TreeNode parent){
        super("<MainFuncDef>",parent);
        //System.out.println("Start <MainFUncDef>");

        //1
        if(sym.content.equals("int")){
            parserList.add(sym);
            TreeNode intNode = new TreeNode(sym);
            intNode.addNode(this.node);
            nextSym();

            //2
            if(sym.content.equals("main")){
                parserList.add(sym);
                TreeNode mainNode = new TreeNode(sym);
                mainNode.addNode(this.node);
                nextSym();

                //3
               if(sym.content.equals("(")){
                   parserList.add(sym);
                   TreeNode LParentNode = new TreeNode(sym);
                   LParentNode.addNode(this.node);
                   nextSym();
                    //4
                   if(sym.content.equals(")")){
                       parserList.add(sym);
                       TreeNode RParentNode = new TreeNode(sym);
                       RParentNode.addNode(this.node);
                       nextSym();

                       //5
                       Block block = new Block(this.node);
                   }
               }
            }
            //System.out.println("Printout <MainFUncDef>");
            parserList.add(this.node.node);
        }
    }
}

