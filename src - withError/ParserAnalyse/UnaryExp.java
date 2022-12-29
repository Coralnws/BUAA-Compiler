package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Save.Word;
import Save.lexerWord;
import Error.Error;
import Error.ErrorRecord;

import static Parser.Parser.testError;

//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')'  | UnaryOp UnaryExp
public class UnaryExp extends SymbAnalyse{
    public UnaryExp(TreeNode parent){
        super("<UnaryExp>",parent);
        System.out.println("start <UnaryExp>");
        //1
        ParserType type = AddExp.scanAhead();
        if(type == ParserType.PrimaryExp || type == ParserType.LVal){
            PrimaryExp primaryExp = new PrimaryExp(this.node);
        }
        else if(type ==  ParserType.UnaryOp){
            UnaryOp unaryOp = new UnaryOp(this.node);
            UnaryExp unaryExp = new UnaryExp(this.node);
        }
        else if(type ==  ParserType.UnaryExp){
            if(sym.typeCode.equals("IDENFR")){
                parserList.add(sym);
                TreeNode IdentNode = new TreeNode(sym);
                IdentNode.addNode(this.node);
                nextSym();

                if(sym.content.equals("(")){
                    parserList.add(sym);
                    TreeNode LParentNode = new TreeNode(sym);
                    LParentNode.addNode(this.node);
                    nextSym();
                }
                if(!sym.content.equals(")")){
                    FuncRParams funcRParams = new FuncRParams(this.node);
                }

                if(sym.content.equals(")")){
                    parserList.add(sym);
                    sym.typeCode = "PARAEND";
                    TreeNode RParentNode = new TreeNode(sym);
                    RParentNode.addNode(this.node);
                    nextSym();
                }else if(testError){
                    Word word = save.getSym(listIndex-2);
                    System.out.println("Check word is what ,should sth before ):" + word.content);
                    Error error = new Error(word.line,'j');
                    PcodeGenerator.errorRecord.addError(error);
                    word = new lexerWord("PARAEND",")", word.line);
                    TreeNode RParentNode = new TreeNode(word);
                    RParentNode.addNode(this.node);
                    parserList.add(word);

                    System.out.println("Check sym is what:" + sym.content);
                }
            }
        }

        //printout <UnaryExp>
        //save.addParserWord(listIndex,this.node.node);
        //listIndex++;
        System.out.println("Printout <UnaryExp>");
        parserList.add(this.node.node);
    }
}
