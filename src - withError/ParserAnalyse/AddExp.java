package ParserAnalyse;

import Save.TreeNode;

//AddExp → MulExp | AddExp ('+' | '−') MulExp
//左递归 : MulExp { ('+' | '−') MulExp }
public class AddExp extends SymbAnalyse {

    public AddExp(TreeNode parent) {
        super("<AddExp>", parent);
        System.out.println("start <AddExp>");

        //1
        MulExp mulExp = new MulExp(this.node); //print 1st

        //printout <AddExp>
        System.out.println("Printout <AddExp>");
        parserList.add(this.node.node);

        //2 loop for { ('+' | '−') MulExp }
        while (sym.content.equals("+") || sym.content.equals("-")) {
            parserList.add(sym);
            TreeNode addNode = new TreeNode(sym);
            addNode.addNode(this.node);
            nextSym();

            //2.1
            mulExp = new MulExp(this.node);

            //printout <AddExp>
            //save.addParserWord(listIndex, this.node.node);
            //listIndex++;
            System.out.println("Printout <AddExp>");
            parserList.add(this.node.node);
        }
    }


    public static ParserType scanAhead(){
        scanSym(listIndex-1);
        System.out.println("Start AddExp.scanAhead()");
        System.out.println("scanSym 1 : " + scanSym.content);

        if(scanSym.typeCode.equals("IDENFR")){ //UnaryExp or LVal
            System.out.println("ScanAhead : Ident");
            scanSym(listIndex);
            System.out.println("scanSym+1 :" + scanSym.content);

            if(scanSym.content.equals("(")){
                    System.out.println("scanAhead return UnaryExp");
                    return ParserType.UnaryExp;
            }
            else{
                    System.out.println("scanAhead return LVal");
                    return ParserType.LVal;
            }
        }
        if(scanSym.content.equals("(")){
            System.out.println("scanAhead return PrimaryExp");
            return ParserType.PrimaryExp;
        }
        if(scanSym.content.equals("+") || scanSym.content.equals("-") || scanSym.content.equals("!")){
            System.out.println("scanAhead return UnaryOp"); //under UnaryExp
            return ParserType.UnaryOp;
        }
        if(scanSym.typeCode.equals("INTCON")){
            System.out.println("scanAhead return PrimaryExp");
            return ParserType.PrimaryExp;
        }
        return ParserType.ERROR;
    }
}


//addExp -> addExp+mulExp(print 2nd) -> mulExp(print 1st) + mulExp