package ParserAnalyse;

import Pcode.PcodeGenerator;
import Save.TreeNode;
import Save.Word;
import Save.lexerWord;
import Save.parserWord;
import Error.Error;
import Error.ErrorRecord;

//VarDecl → 'int' VarDef { ',' VarDef } ';'
public class VarDecl extends SymbAnalyse{
    public VarDecl(TreeNode parent){
        super("<VarDecl>",parent);
        System.out.println("start <VarDecl>");
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

                System.out.println("Printout <VarDecl>");
                parserList.add(this.node.node);
            }else{
                Word word = save.getSym(listIndex-2);
                System.out.println("Check word is what:" + word.content);
                Error error = new Error(word.line,'i');
                PcodeGenerator.errorRecord.addError(error);
                word = new lexerWord("SEMICN",",", word.line);
                TreeNode semicnNode = new TreeNode(word);
                semicnNode.addNode(this.node);

                System.out.println("Check sym is what:" + sym.content);
                System.out.println("Printout <stmt>");
                parserList.add(this.node.node);
            }
        }
    }
}
