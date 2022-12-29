package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Error.Error;
import Error.ErrorRecord;
import Save.Word;
import Save.lexerWord;

import static Parser.Parser.testError;

//FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
public class FuncFParam extends SymbAnalyse{
    public FuncFParam(TreeNode parent){
        super("<FuncFParam>",parent);
        System.out.println("Start <FuncParam>");
        //1
        if(sym.content.equals("int")){
            parserList.add(sym);
            TreeNode intNode = new TreeNode(sym);
            intNode.addNode(this.node);
            nextSym();

            //2
            if(sym.typeCode.equals("IDENFR")){
                parserList.add(sym);
                TreeNode IdentNode = new TreeNode(sym);
                IdentNode.addNode(this.node);
                nextSym();

                //3
                if(sym.content.equals("[")){
                    parserList.add(sym);
                    TreeNode LBrackNode = new TreeNode(sym);
                    LBrackNode.addNode(this.node);
                    nextSym();
                    if (sym.content.equals("]")){
                        parserList.add(sym);
                        TreeNode RBrackNode = new TreeNode(sym);
                        RBrackNode.addNode(this.node);
                        nextSym();
                    }else if(testError){
                        Word word = save.getSym(listIndex-2);
                        System.out.println("Check word is what:" + word.content);
                        Error error = new Error(word.line,'k');
                        PcodeGenerator.errorRecord.addError(error);
                        word = new lexerWord("RBRACK",",", word.line);
                        TreeNode RBrackNode= new TreeNode(word);
                        RBrackNode.addNode(this.node);

                    }
                    //4
                    while(sym.content.equals("[")){
                        parserList.add(sym);
                        LBrackNode = new TreeNode(sym);
                        LBrackNode.addNode(this.node);
                        nextSym();

                        //4.1
                        ConstExp constExp = new ConstExp(this.node);

                        if(sym.content.equals("]")){
                            parserList.add(sym);
                            TreeNode RBrackNode = new TreeNode(sym);
                            RBrackNode.addNode(this.node);
                            nextSym();
                        }else if(testError){
                            Word word = save.getSym(listIndex-2);
                            System.out.println("Check word is what:" + word.content);
                            Error error = new Error(word.line,'k');
                            PcodeGenerator.errorRecord.addError(error);
                            word = new lexerWord("RBRACK",",", word.line);
                            TreeNode RBrackNode= new TreeNode(word);
                            RBrackNode.addNode(this.node);

                        }
                    }
                }
                //printout <FuncFParam>
                //save.addParserWord(listIndex,this.node.node);
                //listIndex++;
                System.out.println("Printout <FuncParam>");
                parserList.add(this.node.node);
            }
        }
    }
}
