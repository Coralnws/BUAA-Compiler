package ParserAnalyse;

import Save.TreeNode;

//ConstInitVal → ConstExp 普通变量 | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'  数组的赋值
public class ConstInitVal extends SymbAnalyse{
    public ConstInitVal(TreeNode parent){
        super("<ConstInitVal>",parent);
        //System.out.println("start <ConstInitVal>");
        //System.out.println("start ConstInitVal");
        //System.out.println("here:" + sym.content);
        //1 - for array's value { }
        if(sym.content.equals("{")){ //数组
            //System.out.println("here:" + sym.content);
            parserList.add(sym);
            TreeNode LBraceNode = new TreeNode(sym);
            LBraceNode.addNode(this.node);
            nextSym();

            //System.out.println("here:" + sym.content);
            //2 - for sth kind of {{},{}}
            if(!sym.content.equals("}")){
                //System.out.println("not }");
                ConstInitVal constInitVal = new ConstInitVal(this.node);
                //2.2 - for comma in {} and btween {}
                while(sym.content.equals(",")){
                    parserList.add(sym);
                    TreeNode commaNode = new TreeNode(sym);
                    commaNode.addNode(this.node);
                    nextSym();
                    //2.3
                    constInitVal = new ConstInitVal(this.node);
                }
            }


            //3
            if(sym.content.equals("}")){
                parserList.add(sym);
                TreeNode RBraceNode = new TreeNode(sym);
                RBraceNode.addNode(this.node);
                nextSym();

                //System.out.println("Printout <ConstInitVal>");
                parserList.add(this.node.node);
            }else{
                //error
            }
        }
        else if(AddExp.scanAhead() != ParserType.ERROR){
            ConstExp constExp = new ConstExp(this.node);
            //printout <ConstInitVal>
            /*
            save.addParserWord(listIndex,this.node.node);
            listIndex++;
             */
            //System.out.println("Printout <ConstInitVal>");
            parserList.add(this.node.node);
        }
    }
}


//constInitVal -> {constInitVal,constInitVal} -> { {constExp} ,ConstExp}