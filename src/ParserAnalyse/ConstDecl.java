package ParserAnalyse;


import Save.*;


//ConstDecl → 'const' 'int' ConstDef { ',' ConstDef } ';'
public class ConstDecl extends SymbAnalyse {
    public ConstDecl(TreeNode parent){
        super("<ConstDecl>",parent);
        System.out.println("start <ConstDecl>");

        //1
        if(sym.content.equals("const")){
            parserList.add(sym);
            TreeNode constNode = new TreeNode(sym);
            constNode.addNode(this.node);
            nextSym();
            //2
            if(sym.content.equals("int")) {
                parserList.add(sym);
                TreeNode intNode = new TreeNode(sym);
                intNode.addNode(this.node);  //当前ConstDecl作为父节点
                nextSym();
                //3
                ConstDef constDef = new ConstDef(this.node);
                //4 loop for  { , ConstDef }
                while(sym.content.equals(",")){
                    parserList.add(sym);
                    TreeNode commaNode = new TreeNode(sym);
                    commaNode.addNode(this.node);
                    nextSym();
                    constDef = new ConstDef(this.node);  //检查有没有nextSym
                }
                //5
                if(sym.content.equals(";")){
                    parserList.add(sym);
                    TreeNode semicnNode = new TreeNode(sym);
                    semicnNode.addNode(this.node);
                    nextSym();

                    //printout <ConstDecl>
                    /*
                    save.addParserWord(listIndex,this.node.node);
                    listIndex++;

                     */
                    System.out.println("Printout <ConstDecl>");
                    parserList.add(this.node.node);
                }
                else{
                    System.out.println("ConstDecl Error:Not ;");
                }
            }
            else{
                System.out.println("ConstDecl Error:Not int");
            }

        }
        else{
            System.out.println("ConstDecl Error:Not const");
        }
    }
}
