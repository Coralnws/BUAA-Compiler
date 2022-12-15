package ParserAnalyse;

import Save.TreeNode;

//InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
//赋值 - exp or array{} , similar as ConstInitVal
public class InitVal extends SymbAnalyse{
    public InitVal(TreeNode parent){
        super("<InitVal>",parent);
        //System.out.println("Start <InitVal>");

        //1 - for array's value { }
        if(sym.content.equals("{")){ //数组
            parserList.add(sym);
            TreeNode LBraceNode = new TreeNode(sym);
            LBraceNode.addNode(this.node);
            nextSym();
            //2 - for sth kind of {{},{}}
            if(!sym.content.equals("}")){
                InitVal initVal = new InitVal(this.node);
                //2.2 - for comma in {} and btween {}
                while(sym.content.equals(",")){
                    parserList.add(sym);
                    TreeNode commaNode = new TreeNode(sym);
                    commaNode.addNode(this.node);
                    nextSym();
                    //2.3
                    initVal = new InitVal(this.node);
                }
            }
            //3
            if(sym.content.equals("}")){
                parserList.add(sym);
                TreeNode RBraceNode = new TreeNode(sym);
                RBraceNode.addNode(this.node);
                nextSym();

                //printout <InitVal>
                //save.addParserWord(listIndex,this.node.node);
                //listIndex++;
                //System.out.println("Printout <InitVal>");
                parserList.add(this.node.node);
            }
        }
        else if(AddExp.scanAhead() != ParserType.ERROR){
            Exp exp = new Exp(this.node);
            //printout <InitVal>
            //save.addParserWord(listIndex,this.node.node);
            //listIndex++;
            //System.out.println("Printout <InitVal>");
            parserList.add(this.node.node);
        }
        else{
            //System.out.println("InitVal Error : Not Match.");
        }
    }

}
