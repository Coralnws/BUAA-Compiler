package ParserAnalyse;

import Save.*;

//Block → '{' { BlockItem } '}'
public class Block extends SymbAnalyse{
    public Block(TreeNode parent){
        super("<Block>",parent);
        System.out.println("Start <Block>");
        //1
        if(sym.content.equals("{")){
            parserList.add(sym);
            TreeNode LBraceNode = new TreeNode(sym);
            LBraceNode.addNode(this.node);
            nextSym();

            //2 loop for multiple blockItem
            while(!sym.content.equals("}")){
                BlockItem blockItem = new BlockItem(this.node);
            }

            //3
            System.out.println("To check here is } : "+ sym.content);
            if(sym.content.equals("}")){
                parserList.add(sym);
                TreeNode RBraceNode = new TreeNode(sym);
                RBraceNode.addNode(this.node);
                nextSym();
            }

            System.out.println("Printout <Block>");
            parserList.add(this.node.node);
        }
    }

}
